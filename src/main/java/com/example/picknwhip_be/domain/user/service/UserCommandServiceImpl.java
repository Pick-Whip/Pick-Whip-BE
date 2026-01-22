package com.example.picknwhip_be.domain.user.service;

import com.example.picknwhip_be.domain.user.dto.req.UserRequestDTO;
import com.example.picknwhip_be.domain.user.entity.User;
import com.example.picknwhip_be.domain.user.entity.UserWithdrawal;
import com.example.picknwhip_be.domain.user.entity.WithdrawalReasonItem;
import com.example.picknwhip_be.domain.user.exception.UserException;
import com.example.picknwhip_be.domain.user.exception.code.UserErrorCode;
import com.example.picknwhip_be.domain.user.repository.UserRepository;
import com.example.picknwhip_be.domain.user.repository.UserWithdrawalRepository;
import com.example.picknwhip_be.domain.user.repository.WithdrawalReasonItemRepository;
import com.example.picknwhip_be.global.apiPayload.code.GeneralErrorCode;
import com.example.picknwhip_be.global.apiPayload.exception.GeneralException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.*;
import org.springframework.http.HttpEntity;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class UserCommandServiceImpl implements UserCommandService {

  private final UserRepository userRepository;
  private final UserWithdrawalRepository userWithdrawalRepository;
  private final WithdrawalReasonItemRepository reasonItemRepository;
  private final OAuth2AuthorizedClientService authorizedClientService;
  private final RestTemplate restTemplate = new RestTemplate();
  private static final int MAX_NICKNAME_GENERATION_ATTEMPTS = 20;

  // TODO: 프로필 사진 기본 이미지 설정 구현
  // private static final String DEFAULT_PROFILE_IMAGE = "//";

  @Override
  public User joinOrCreateUser(
      Long kakaoId, String email, String name, String phone, String profileImageUrl) {
    // 이미 가입된 카카오 유저인지 확인
    return userRepository
        .findByKakaoId(kakaoId)
        .orElseGet(
            () -> {
              // 가입된 적이 없으면 랜덤 닉네임 생성 후 등록
              String randomNickname = generateRandomNickname();
              User newUser = User.createKakaoUser(kakaoId, email, randomNickname);
              return userRepository.save(newUser);
            });
  }

  // 랜덤 닉네임 생성
  private String generateRandomNickname() {
    String[] adjectives = {"생크림", "딸기", "바닐라", "캐러멜", "쇼콜라"};
    java.util.concurrent.ThreadLocalRandom random =
        java.util.concurrent.ThreadLocalRandom.current();

    // 무한 루프를 방지하기 위해 최대 시도 횟수를 제한
    for (int attempt = 0; attempt < MAX_NICKNAME_GENERATION_ATTEMPTS; attempt++) {
      int randomNumber = random.nextInt(100, 1000);
      String nickname = adjectives[random.nextInt(adjectives.length)] + randomNumber;

      if (!userRepository.existsByNickname(nickname)) {
        return nickname;
      }
    }

    // 그래도 중복이면 UUID 기반으로 fallback (한 번만 DB 체크)
    String fallbackNickname =
        "user_" + java.util.UUID.randomUUID().toString().replace("-", "").substring(0, 10);
    if (!userRepository.existsByNickname(fallbackNickname)) {
      return fallbackNickname;
    }

    throw new UserException(UserErrorCode.NICKNAME_ALREADY_EXISTS);
  }

  @Override
  public void updateExtraInfo(Long userId, UserRequestDTO.ExtraInfoDTO request) {
    User user =
        userRepository
            .findById(userId)
            .orElseThrow(() -> new UserException(UserErrorCode.USER_NOT_FOUND));

    user.updateExtraInfo(request.getName(), request.getPhone());
  }

  @Override
  public User updateProfile(Long userId, UserRequestDTO.UpdateProfileDTO request) {
    User user =
        userRepository
            .findById(userId)
            .orElseThrow(() -> new UserException(UserErrorCode.USER_NOT_FOUND));

    // 닉네임 중복 확인 (닉네임 변경 요청이 있을 때만)
    String newNickname = request.getNickname();
    if (newNickname != null && !newNickname.isBlank()) {
      // 현재 닉네임과 다른 값으로 변경하려는 경우에만 중복 체크
      if (!newNickname.equals(user.getNickname()) && userRepository.existsByNickname(newNickname)) {
        throw new UserException(UserErrorCode.NICKNAME_ALREADY_EXISTS);
      }
    }

    user.updateProfile(newNickname, request.getProfileImageUrl());
    return user;
  }

  @Override
  @Transactional
  public void withdrawMember(Long userId, UserRequestDTO.WithdrawalDTO request) {
    User user =
        userRepository
            .findById(userId)
            .orElseThrow(() -> new UserException(UserErrorCode.USER_NOT_FOUND));

    // 탈퇴 기록 생성
    UserWithdrawal withdrawal =
        UserWithdrawal.builder().user(user).feedback(request.getFeedback()).build();
    userWithdrawalRepository.save(withdrawal);

    // 선택한 사유 저장
    if (request.getReasons() != null) {
      List<WithdrawalReasonItem> items =
          request.getReasons().stream()
              .map(
                  reason ->
                      WithdrawalReasonItem.builder()
                          .userWithdrawal(withdrawal)
                          .reasonType(reason)
                          .build())
              .toList();
      reasonItemRepository.saveAll(items);
    }

    // 직접 상태를 변경하는 대신 Repository의 delete를 호출
    userRepository.delete(user);
  }

  @Override
  public void logout(Long userId) {
    User user =
        userRepository
            .findById(userId)
            .orElseThrow(() -> new UserException(UserErrorCode.USER_NOT_FOUND));

    OAuth2AuthorizedClient client =
        authorizedClientService.loadAuthorizedClient("kakao", user.getKakaoId().toString());

    if (client != null && client.getAccessToken() != null) {
      String accessToken = client.getAccessToken().getTokenValue();

      HttpHeaders headers = new HttpHeaders();
      headers.set("Authorization", "Bearer " + accessToken);
      HttpEntity<String> entity = new HttpEntity<>(headers);

      try {
        // 카카오 로그아웃 REST API 호출
        restTemplate.postForEntity("https://kapi.kakao.com/v1/user/logout", entity, String.class);

        // 성공 시 서버 내 인증 정보 삭제
        authorizedClientService.removeAuthorizedClient("kakao", user.getKakaoId().toString());
        log.info("카카오 로그아웃 성공. userId: {}", userId);

      } catch (RestClientException e) {
        log.error("카카오 로그아웃 API 호출 실패. userId: {}, 에러: {}", userId, e.getMessage());
        throw new GeneralException(GeneralErrorCode.INTERNAL_SERVER_ERROR);
      }
    } else {
      log.warn("로그아웃을 시도했으나 세션에 카카오 토큰이 존재하지 않습니다. userId: {}", userId);
    }
  }
}

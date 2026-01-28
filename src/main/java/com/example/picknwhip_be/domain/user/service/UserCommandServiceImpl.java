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
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.http.HttpEntity;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
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

  @Value("${cloud.aws.s3.bucket}")
  private String bucket;

  @Value("${cloud.aws.region:ap-northeast-2}")
  private String region;

  @Override
  public User joinOrCreateUser(
      Long kakaoId,
      String email,
      String name,
      String phone,
      String birthdate,
      String profileImageUrl) {
    // 이미 가입된 카카오 유저인지 확인
    return userRepository
        .findByKakaoId(kakaoId)
        .orElseGet(
            () -> {
              // 가입된 적이 없으면 랜덤 닉네임 생성 후 등록
              NicknameInfo info = generateRandomNicknameWithInfo();
              String resolvedImageUrl =
                  (profileImageUrl != null && !profileImageUrl.isBlank())
                      ? profileImageUrl
                      : constructDefaultImageUrl(info.adjective);
              User newUser = User.createKakaoUser(kakaoId, email, info.nickname, resolvedImageUrl);
              return userRepository.save(newUser);
            });
  }

  // 랜덤 닉네임 생성
  private record NicknameInfo(String adjective, String nickname) {}

  private NicknameInfo generateRandomNicknameWithInfo() {
    String[] adjectives = {"생크림", "딸기", "바닐라", "캐러멜", "쇼콜라"}; //
    java.util.concurrent.ThreadLocalRandom random =
        java.util.concurrent.ThreadLocalRandom.current();

    for (int attempt = 0; attempt < MAX_NICKNAME_GENERATION_ATTEMPTS; attempt++) {
      String adj = adjectives[random.nextInt(adjectives.length)];
      int randomNumber = random.nextInt(100, 1000);
      String nickname = adj + randomNumber;

      if (!userRepository.existsByNickname(nickname)) {
        return new NicknameInfo(adj, nickname);
      }
    }
    // Fallback 로직
    String adj = "생크림";
    String nickname;
    do {
      nickname = adj + java.util.UUID.randomUUID().toString().substring(0, 10);
    } while (userRepository.existsByNickname(nickname));
    return new NicknameInfo(adj, nickname);
  }

  // URL 생성 헬퍼
  private String constructDefaultImageUrl(String adjective) {
    String fileName =
        switch (adjective) {
          case "생크림" -> "cream.png";
          case "딸기" -> "strawberry.png";
          case "바닐라" -> "vanilla.png";
          case "캐러멜" -> "caramel.png";
          case "쇼콜라" -> "chocolate.png";
          default -> "base.png";
        };
    return String.format(
        "https://%s.s3.%s.amazonaws.com/profile/default/%s", bucket, region, fileName);
  }

  @Override
  public void updateExtraInfo(Long userId, UserRequestDTO.ExtraInfoDTO request) {
    User user =
        userRepository
            .findById(userId)
            .orElseThrow(() -> new UserException(UserErrorCode.USER_NOT_FOUND));

    user.updateExtraInfo(request.getName(), request.getPhone(), request.getBirthdate());
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

  @Value("${kakao.admin-key}")
  private String adminKey;

  @Override
  public void logout(Long userId) {
    User user =
        userRepository
            .findById(userId)
            .orElseThrow(() -> new UserException(UserErrorCode.USER_NOT_FOUND));

    HttpHeaders headers = new HttpHeaders();

    // KakaoAK 스키마, Admin Key를 헤더에 담음
    headers.set("Authorization", "KakaoAK " + adminKey);
    headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

    MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
    params.add("target_id_type", "user_id");
    params.add("target_id", user.getKakaoId().toString()); // 엔티티의 카카오 고유 ID 사용

    HttpEntity<MultiValueMap<String, String>> entity = new HttpEntity<>(params, headers);

    try {
      // 카카오 로그아웃 API
      restTemplate.postForEntity("https://kapi.kakao.com/v1/user/logout", entity, String.class);
      log.info("카카오 서버 로그아웃 완료. userId: {}", userId);
    } catch (RestClientException e) {
      log.error("카카오 로그아웃 API 실패: {}", e.getMessage());
      throw new GeneralException(GeneralErrorCode.INTERNAL_SERVER_ERROR);
    }
  }
}

package com.example.picknwhip_be.domain.user.service;

import com.example.picknwhip_be.domain.user.dto.req.UserRequestDTO;
import com.example.picknwhip_be.domain.user.entity.User;
import com.example.picknwhip_be.domain.user.entity.UserStatus;
import com.example.picknwhip_be.domain.user.entity.UserWithdrawal;
import com.example.picknwhip_be.domain.user.entity.WithdrawalReasonItem;
import com.example.picknwhip_be.domain.user.exception.UserException;
import com.example.picknwhip_be.domain.user.exception.code.UserErrorCode;
import com.example.picknwhip_be.domain.user.repository.UserRepository;
import com.example.picknwhip_be.domain.user.repository.UserWithdrawalRepository;
import com.example.picknwhip_be.domain.user.repository.WithdrawalReasonItemRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class UserCommandServiceImpl implements UserCommandService {

  private final UserRepository userRepository;
  private final UserWithdrawalRepository userWithdrawalRepository;
  private final WithdrawalReasonItemRepository reasonItemRepository;

  // 프로필 사진 기본 이미지
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
              User newUser =
                  User.builder()
                      .kakaoId(kakaoId)
                      .email(email)
                      .name(name)
                      .nickname(randomNickname)
                      .phone(phone)
                      .profileImageUrl(profileImageUrl) // 기본 이미지 등록 시 DEFAULT_PROFILE_IMAGE로 수정 필요
                      .status(UserStatus.ACTIVE)
                      .build();
              return userRepository.save(newUser);
            });
  }

  // 랜덤 닉네임 생성
  private String generateRandomNickname() {
    String[] adjectives = {"생크림", "딸기", "바닐라", "캐러멜", "쇼콜라"};
    int randomNumber = (int) (Math.random() * 900) + 100;
    String nickname = adjectives[(int) (Math.random() * adjectives.length)] + randomNumber;

    // 중복 체크
    if (userRepository.existsByNickname(nickname)) {
      return generateRandomNickname();
    }
    return nickname;
  }

  @Override
  public User updateProfile(Long userId, UserRequestDTO.UpdateProfileDTO request) {
    User user =
        userRepository
            .findById(userId)
            .orElseThrow(() -> new UserException(UserErrorCode.USER_NOT_FOUND));

    // 닉네임 중복 확인
    if (!user.getNickname().equals(request.getNickname())
        && userRepository.existsByNickname(request.getNickname())) {
      throw new UserException(UserErrorCode.NICKNAME_ALREADY_EXISTS);
    }

    user.updateProfile(request.getNickname(), request.getPhone(), request.getProfileImageUrl());
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

    // 선택한 모든 사유 저장
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

    // 사용자 상태 변경 (Soft Delete)
    user.withdraw();
  }
}

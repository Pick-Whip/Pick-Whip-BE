package com.example.picknwhip_be.domain.user.service.command;

import com.example.picknwhip_be.domain.user.dto.req.UserReqDTO;
import com.example.picknwhip_be.domain.user.dto.res.UserResDTO;
import com.example.picknwhip_be.domain.user.entity.User;

public interface UserCommandService {
  User joinOrCreateUser(
      Long kakaoId,
      String email,
      String name,
      String phone,
      String birthdate,
      String profileImageUrl);

  void updateExtraInfo(Long userId, UserReqDTO.ExtraInfoDTO request);

  User updateProfile(Long userId, UserReqDTO.UpdateProfileDTO request);

  void withdrawMember(Long userId, UserReqDTO.WithdrawalDTO request);

  void logout(Long userId);

  UserResDTO.TokenResponseDTO refreshAccessToken(String refreshTokenRequest);
}

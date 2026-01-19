package com.example.picknwhip_be.domain.user.service;

import com.example.picknwhip_be.domain.user.dto.req.UserRequestDTO;
import com.example.picknwhip_be.domain.user.entity.User;

public interface UserCommandService {
  User joinOrCreateUser(
      Long kakaoId, String email, String name, String phone, String profileImageUrl);

  void updateExtraInfo(Long userId, UserRequestDTO.ExtraInfoDTO request);

  User updateProfile(Long userId, UserRequestDTO.UpdateProfileDTO request);

  void withdrawMember(Long userId, UserRequestDTO.WithdrawalDTO request);

  void logout(Long userId);
}

package com.example.picknwhip_be.domain.user.converter;

import com.example.picknwhip_be.domain.user.dto.res.UserResponseDTO;
import com.example.picknwhip_be.domain.user.entity.User;

public class UserConverter {

  public static UserResponseDTO.UserProfileDTO toUserProfileDTO(User user) {
    return UserResponseDTO.UserProfileDTO.builder()
        .userId(user.getUserId())
        .email(user.getEmail())
        .name(user.getName())
        .nickname(user.getNickname())
        .phone(user.getPhone())
        .profileImageUrl(user.getProfileImageUrl())
        .createdAt(user.getCreatedAt())
        .build();
  }

  public static UserResponseDTO.UpdateProfileResultDTO toUpdateProfileResultDTO(User user) {
    return UserResponseDTO.UpdateProfileResultDTO.builder()
        .userId(user.getUserId())
        .updatedAt(user.getUpdatedAt())
        .build();
  }
}

package com.example.picknwhip_be.domain.review.service.user.converter;

import com.example.picknwhip_be.domain.review.service.user.dto.res.UserResDTO;
import com.example.picknwhip_be.domain.review.service.user.entity.User;

public class UserConverter {

  public static UserResDTO.UserProfileDTO toUserProfileDTO(User user) {
    return UserResDTO.UserProfileDTO.builder()
        .userId(user.getUserId())
        .email(user.getEmail())
        .name(user.getName())
        .nickname(user.getNickname())
        .phone(user.getPhone())
        .birthdate(user.getBirthdate())
        .profileImageUrl(user.getProfileImageUrl())
        .createdAt(user.getCreatedAt())
        .build();
  }

  public static UserResDTO.UpdateProfileResultDTO toUpdateProfileResultDTO(User user) {
    return UserResDTO.UpdateProfileResultDTO.builder()
        .userId(user.getUserId())
        .updatedAt(user.getUpdatedAt())
        .build();
  }
}

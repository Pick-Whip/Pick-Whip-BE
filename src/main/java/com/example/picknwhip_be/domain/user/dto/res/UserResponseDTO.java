package com.example.picknwhip_be.domain.user.dto.res;

import java.time.LocalDateTime;
import lombok.*;

public class UserResponseDTO {

  @Builder
  @Getter
  @NoArgsConstructor
  @AllArgsConstructor
  public static class UserProfileDTO {
    private Long userId;
    private String email;
    private String name;
    private String nickname;
    private String phone;
    private String birthdate;
    private String profileImageUrl;
    private LocalDateTime createdAt;
  }

  @Builder
  @Getter
  @NoArgsConstructor
  @AllArgsConstructor
  public static class UpdateProfileResultDTO {
    private Long userId;
    private LocalDateTime updatedAt;
  }
}

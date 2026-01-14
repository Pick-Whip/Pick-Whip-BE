package com.example.picknwhip_be.domain.user.dto.req;

import lombok.Getter;

public class UserRequestDTO {

  @Getter
  public static class UpdateProfileDTO {
    private String nickname;
    private String phone;
    private String profileImageUrl;
  }
}

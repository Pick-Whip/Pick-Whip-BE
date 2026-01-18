package com.example.picknwhip_be.domain.user.dto.auth;

public interface OAuth2UserInfo {
  Long getKakaoId();

  String getEmail();
}

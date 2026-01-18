package com.example.picknwhip_be.domain.user.dto.auth;

import java.util.Map;

public class KakaoUserInfo implements OAuth2UserInfo {
  private Map<String, Object> attributes;

  public KakaoUserInfo(Map<String, Object> attributes) {
    this.attributes = attributes;
  }

  @Override
  public Long getKakaoId() {
    Object id = attributes.get("id");
    if (id instanceof Number) {
      return ((Number) id).longValue();
    }
    return Long.parseLong(String.valueOf(id));
  }

  @Override
  public String getEmail() {
    Object kakaoAccountObj = attributes.get("kakao_account");
    if (kakaoAccountObj instanceof Map) {
      Map<String, Object> kakaoAccount = (Map<String, Object>) kakaoAccountObj;
      return (String) kakaoAccount.get("email");
    }
    return null;
  }
}

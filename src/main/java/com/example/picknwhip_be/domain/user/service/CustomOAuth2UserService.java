package com.example.picknwhip_be.domain.user.service;

import com.example.picknwhip_be.domain.user.dto.auth.KakaoUserInfo;
import java.util.Collections;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

  private final UserCommandService userCommandService;

  @Override
  public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
    OAuth2User oAuth2User = super.loadUser(userRequest);
    KakaoUserInfo kakaoUserInfo = new KakaoUserInfo(oAuth2User.getAttributes());

    // 이메일만으로 우선 가입
    userCommandService.joinOrCreateUser(
        kakaoUserInfo.getKakaoId(), kakaoUserInfo.getEmail(), null, null, null, null);

    return new DefaultOAuth2User(
        Collections.singleton(new SimpleGrantedAuthority("ROLE_USER")),
        oAuth2User.getAttributes(),
        "id");
  }
}

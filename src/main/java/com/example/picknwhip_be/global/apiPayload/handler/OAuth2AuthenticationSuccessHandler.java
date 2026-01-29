package com.example.picknwhip_be.global.apiPayload.handler;

import com.example.picknwhip_be.domain.user.dto.auth.KakaoUserInfo;
import com.example.picknwhip_be.domain.user.entity.User;
import com.example.picknwhip_be.domain.user.repository.UserRepository;
import com.example.picknwhip_be.global.apiPayload.util.JwtTokenProvider;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

@Component
@RequiredArgsConstructor
public class OAuth2AuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

  private final UserRepository userRepository;
  private final JwtTokenProvider jwtTokenProvider;

  @Override
  public void onAuthenticationSuccess(
      HttpServletRequest request, HttpServletResponse response, Authentication authentication)
      throws IOException {
    OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();

    KakaoUserInfo kakaoUserInfo = new KakaoUserInfo(oAuth2User.getAttributes());
    Long kakaoId = kakaoUserInfo.getKakaoId();

    User user =
        userRepository
            .findByKakaoId(kakaoId)
            .orElseThrow(() -> new IllegalStateException("사용자를 찾을 수 없습니다. kakaoId: " + kakaoId));

    // JWT 토큰 생성
    String accessToken = jwtTokenProvider.createToken(user.getUserId());

    // 리다이렉트 경로 설정 및 토큰 전달
    String targetUrl;
    if (user.getName() == null || user.getPhone() == null || user.getBirthdate() == null) {
      // 추가 정보 입력이 필요한 경우 TODO: 프론트엔드에서 리다이렉트 주소 받기
      targetUrl =
          UriComponentsBuilder.fromUriString("http://localhost:3000/signup/extra")
              .queryParam("accessToken", accessToken)
              .build()
              .toUriString();
    } else {
      // 이미 가입된 유저인 경우 홈으로 이동
      targetUrl =
          UriComponentsBuilder.fromUriString("http://localhost:3000/")
              .queryParam("accessToken", accessToken)
              .build()
              .toUriString();
    }

    getRedirectStrategy().sendRedirect(request, response, targetUrl);
  }
}

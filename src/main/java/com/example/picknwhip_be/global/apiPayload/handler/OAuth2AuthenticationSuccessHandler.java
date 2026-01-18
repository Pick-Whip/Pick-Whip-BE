package com.example.picknwhip_be.global.apiPayload.handler;

import com.example.picknwhip_be.domain.user.dto.auth.KakaoUserInfo;
import com.example.picknwhip_be.domain.user.entity.User;
import com.example.picknwhip_be.domain.user.repository.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class OAuth2AuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

  private final UserRepository userRepository;

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

    // 이름이나 전화번호가 없으면 무조건 추가 정보 입력 페이지로 리다이렉트
    if (user.getName() == null || user.getPhone() == null) {
      getRedirectStrategy().sendRedirect(request, response, "http://localhost:3000/signup/extra");
    } else {
      getRedirectStrategy().sendRedirect(request, response, "/");
    }
  }
}

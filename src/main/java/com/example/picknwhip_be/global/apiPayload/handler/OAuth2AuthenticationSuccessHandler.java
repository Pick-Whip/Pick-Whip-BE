package com.example.picknwhip_be.global.apiPayload.handler;

import com.example.picknwhip_be.domain.user.dto.auth.KakaoUserInfo;
import com.example.picknwhip_be.domain.user.entity.RefreshToken;
import com.example.picknwhip_be.domain.user.entity.User;
import com.example.picknwhip_be.domain.user.repository.RefreshTokenRepository;
import com.example.picknwhip_be.domain.user.repository.UserRepository;
import com.example.picknwhip_be.global.apiPayload.util.CookieUtils;
import com.example.picknwhip_be.global.apiPayload.util.JwtTokenProvider;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

@Component
@RequiredArgsConstructor
public class OAuth2AuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

  private final UserRepository userRepository;
  private final RefreshTokenRepository refreshTokenRepository;
  private final JwtTokenProvider jwtTokenProvider;

  @Value("${app.frontend-url}")
  private String frontendUrl;

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

    String accessToken = jwtTokenProvider.createToken(user.getUserId());

    String refreshTokenValue = jwtTokenProvider.createRefreshToken(user.getUserId());
    saveOrUpdateRefreshToken(user, refreshTokenValue);

    int cookieMaxAge = 60 * 60 * 24 * 14;
    CookieUtils.addCookie(response, "refreshToken", refreshTokenValue, cookieMaxAge);

    String targetUrl;
    if (user.getName() == null || user.getPhone() == null || user.getBirthdate() == null) {
      targetUrl =
          UriComponentsBuilder.fromUriString(frontendUrl + "/signup/extra")
              .queryParam("accessToken", accessToken)
              .build()
              .toUriString();
    } else {
      targetUrl =
          UriComponentsBuilder.fromUriString(frontendUrl + "/")
              .queryParam("accessToken", accessToken)
              .build()
              .toUriString();
    }

    getRedirectStrategy().sendRedirect(request, response, targetUrl);
  }

  private void saveOrUpdateRefreshToken(User user, String tokenValue) {
    RefreshToken refreshToken =
        refreshTokenRepository
            .findByUserId(user.getUserId())
            .map(
                entity -> {
                  entity.updateToken(tokenValue, LocalDateTime.now().plusDays(14));
                  return entity;
                })
            .orElseGet(
                () ->
                    RefreshToken.builder()
                        .token(tokenValue)
                        .userId(user.getUserId())
                        .expiryDate(LocalDateTime.now().plusDays(14))
                        .build());

    refreshTokenRepository.save(refreshToken);
  }
}

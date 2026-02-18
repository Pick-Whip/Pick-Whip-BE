package com.example.picknwhip_be.global.apiPayload.handler;

import com.example.picknwhip_be.domain.user.dto.auth.KakaoUserInfo;
import com.example.picknwhip_be.domain.user.entity.User;
import com.example.picknwhip_be.domain.user.repository.UserRepository;
import com.example.picknwhip_be.domain.user.service.command.UserCommandService;
import com.example.picknwhip_be.global.apiPayload.util.JwtTokenProvider;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class OAuth2AuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

  private final UserRepository userRepository;
  private final UserCommandService userCommandService;
  private final JwtTokenProvider jwtTokenProvider;

  @Value("${app.frontend-url}")
  private String frontendUrl;

  @Value("${app.cookie-domain:}")
  private String cookieDomain;

  @Value("${jwt.refresh-token-validity:1209600000}")
  private long refreshTokenValidityInMilliseconds; // 14일 기본값 (ms)

  @Value("${jwt.access-token-validity:3600000}")
  private long accessTokenValidityInMilliseconds; // 1시간 기본값 (ms)

  private static final String ACCESS_TOKEN_COOKIE_NAME = "accessToken";

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
    userCommandService.saveOrUpdateRefreshToken(user.getUserId(), refreshTokenValue);

    int cookieMaxAge = (int) (refreshTokenValidityInMilliseconds / 1000);

    var refreshCookieBuilder =
        org.springframework.http.ResponseCookie.from("refreshToken", refreshTokenValue)
            .httpOnly(true)
            .secure(true)
            .path("/")
            .maxAge(cookieMaxAge)
            .sameSite("None");
    if (cookieDomain != null && !cookieDomain.isBlank()) {
      refreshCookieBuilder.domain(cookieDomain);
    }
    response.addHeader("Set-Cookie", refreshCookieBuilder.build().toString());

    int accessTokenMaxAge = (int) (accessTokenValidityInMilliseconds / 1000);
    var accessTokenCookieBuilder =
        org.springframework.http.ResponseCookie.from(ACCESS_TOKEN_COOKIE_NAME, accessToken)
            .httpOnly(true)
            .secure(true)
            .path("/")
            .maxAge(accessTokenMaxAge)
            .sameSite("None");
    if (cookieDomain != null && !cookieDomain.isBlank()) {
      accessTokenCookieBuilder.domain(cookieDomain);
    }
    response.addHeader("Set-Cookie", accessTokenCookieBuilder.build().toString());

    String encodedToken = URLEncoder.encode(accessToken, StandardCharsets.UTF_8);
    String basePath =
        (user.getName() == null || user.getPhone() == null || user.getBirthdate() == null)
            ? "/signup/extra"
            : "/";
    String targetUrl = frontendUrl + basePath + "?accessToken=" + encodedToken;

    getRedirectStrategy().sendRedirect(request, response, targetUrl);
  }
}

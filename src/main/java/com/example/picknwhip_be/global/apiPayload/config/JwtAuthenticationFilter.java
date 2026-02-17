package com.example.picknwhip_be.global.apiPayload.config;

import com.example.picknwhip_be.global.apiPayload.exception.GeneralException;
import com.example.picknwhip_be.global.apiPayload.handler.OAuth2AuthenticationSuccessHandler;
import com.example.picknwhip_be.global.apiPayload.util.JwtTokenProvider;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

  private final JwtTokenProvider jwtTokenProvider;

  @Override
  protected boolean shouldNotFilter(HttpServletRequest request) {
    String path = request.getServletPath();
    return path.startsWith("/v3/api-docs")
        || path.startsWith("/swagger-ui")
        || path.startsWith("/swagger-resources")
        || path.startsWith("/webjars");
  }

  @Override
  protected void doFilterInternal(
      HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
      throws ServletException, IOException {

    // JWT 토큰 추출
    String token = resolveToken(request);

    // 토큰 유효성 검사
    try {
      if (token != null && jwtTokenProvider.validateToken(token)) {
        Authentication auth = jwtTokenProvider.getAuthentication(token);
        SecurityContextHolder.getContext().setAuthentication(auth);
      }
    } catch (GeneralException e) {
      request.setAttribute("exception", e.getCode());
    }
    filterChain.doFilter(request, response);
  }

  private String resolveToken(HttpServletRequest request) {
    // 1) Authorization 헤더 (Bearer)
    String bearerToken = request.getHeader("Authorization");
    if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
      return bearerToken.substring(7);
    }
    // 2) accessToken 쿠키 (OAuth 로그인 후 리다이렉트 시 설정된 쿠키)
    if (request.getCookies() != null) {
      for (jakarta.servlet.http.Cookie cookie : request.getCookies()) {
        if (OAuth2AuthenticationSuccessHandler.ACCESS_TOKEN_COOKIE_NAME.equals(cookie.getName())) {
          return cookie.getValue();
        }
      }
    }
    return null;
  }
}

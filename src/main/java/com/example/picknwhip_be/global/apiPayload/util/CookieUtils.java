package com.example.picknwhip_be.global.apiPayload.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.time.Duration;
import java.util.Base64;
import java.util.Optional;
import org.springframework.http.ResponseCookie;
import org.springframework.security.jackson2.SecurityJackson2Modules;
import org.springframework.security.oauth2.client.jackson2.OAuth2ClientJackson2Module;

public class CookieUtils {

  public static final String ACCESS_TOKEN_COOKIE_NAME = "accessToken";

  private static final ObjectMapper objectMapper = new ObjectMapper();

  static {
    objectMapper.registerModules(
        SecurityJackson2Modules.getModules(CookieUtils.class.getClassLoader()));
    objectMapper.registerModule(new OAuth2ClientJackson2Module());
  }

  public static Optional<Cookie> getCookie(HttpServletRequest request, String name) {
    Cookie[] cookies = request.getCookies();
    if (cookies != null && cookies.length > 0) {
      for (Cookie cookie : cookies) {
        if (cookie.getName().equals(name)) {
          return Optional.of(cookie);
        }
      }
    }
    return Optional.empty();
  }

  public static void addCookie(
      HttpServletResponse response, String name, String value, int maxAge) {
    Cookie cookie = new Cookie(name, value);
    cookie.setPath("/");
    cookie.setHttpOnly(true);
    cookie.setSecure(true);
    cookie.setMaxAge(maxAge);
    response.addCookie(cookie);
  }

  /** 리프레시 토큰용 쿠키 추가. SameSite=Lax 설정으로 CSRF 취약점 방지. */
  public static void addRefreshTokenCookie(
      HttpServletResponse response, String name, String value, int maxAgeSeconds) {
    response.addHeader("Set-Cookie", buildHttpOnlySecureCookie(name, value, maxAgeSeconds));
  }

  /** 액세스 토큰용 쿠키 추가 (리프레시 응답 등에서 사용). */
  public static void addAccessTokenCookie(
      HttpServletResponse response, String value, int maxAgeSeconds) {
    response.addHeader(
        "Set-Cookie", buildHttpOnlySecureCookie(ACCESS_TOKEN_COOKIE_NAME, value, maxAgeSeconds));
  }

  private static String buildHttpOnlySecureCookie(String name, String value, int maxAgeSeconds) {
    return ResponseCookie.from(name, value)
        .path("/")
        .httpOnly(true)
        .secure(true)
        .sameSite("Lax")
        .maxAge(Duration.ofSeconds(maxAgeSeconds))
        .build()
        .toString();
  }

  public static void deleteCookie(
      HttpServletRequest request, HttpServletResponse response, String name) {
    Cookie[] cookies = request.getCookies();
    if (cookies != null && cookies.length > 0) {
      for (Cookie cookie : cookies) {
        if (cookie.getName().equals(name)) {
          cookie.setValue("");
          cookie.setPath("/");
          cookie.setMaxAge(0);
          response.addCookie(cookie);
        }
      }
    }
  }

  public static String serialize(Object object) {
    try {
      byte[] jsonBytes = objectMapper.writeValueAsBytes(object);
      return Base64.getUrlEncoder().encodeToString(jsonBytes);
    } catch (JsonProcessingException e) {
      throw new IllegalArgumentException("쿠키 직렬화에 실패했습니다.", e);
    }
  }

  public static <T> T deserialize(Cookie cookie, Class<T> cls) {
    try {
      byte[] decoded = Base64.getUrlDecoder().decode(cookie.getValue());
      return objectMapper.readValue(decoded, cls);
    } catch (Exception e) {
      throw new IllegalArgumentException("쿠키 역직렬화에 실패했습니다.", e);
    }
  }
}

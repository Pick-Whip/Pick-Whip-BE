package com.example.picknwhip_be.global.apiPayload.util;

import com.nimbusds.oauth2.sdk.util.StringUtils;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.net.URI;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.oauth2.client.web.AuthorizationRequestRepository;
import org.springframework.security.oauth2.core.endpoint.OAuth2AuthorizationRequest;
import org.springframework.stereotype.Component;

@Component
public class HttpCookieOAuth2AuthorizationRequestRepository
    implements AuthorizationRequestRepository<OAuth2AuthorizationRequest> {

  private static final Logger log =
      LoggerFactory.getLogger(HttpCookieOAuth2AuthorizationRequestRepository.class);

  public static final String OAUTH2_AUTHORIZATION_REQUEST_COOKIE_NAME = "oauth2_auth_request";
  public static final String REDIRECT_URI_PARAM_COOKIE_NAME = "redirect_uri";
  private static final int cookieExpireSeconds = 180;

  @Value("${app.frontend-url}")
  private String frontendUrl;

  @Override
  public OAuth2AuthorizationRequest loadAuthorizationRequest(HttpServletRequest request) {
    try {
      var result =
          CookieUtils.getCookie(request, OAUTH2_AUTHORIZATION_REQUEST_COOKIE_NAME)
              .map(cookie -> CookieUtils.deserialize(cookie, OAuth2AuthorizationRequest.class))
              .orElse(null);
      if (result == null && request.getRequestURI().contains("/login/oauth2/code")) {
        boolean hasCookie =
            CookieUtils.getCookie(request, OAUTH2_AUTHORIZATION_REQUEST_COOKIE_NAME).isPresent();
        log.warn(
            "[OAuth2] authorization_request_not_found. "
                + "프론트엔드가 반드시 https://www.picknwhip.shop/oauth2/authorization/kakao 로 진입해야 함. "
                + "path={}, 쿠키존재={}",
            request.getRequestURI(),
            hasCookie);
      }
      return result;
    } catch (Exception e) {
      if (request.getRequestURI().contains("/login/oauth2/code")) {
        log.warn("[OAuth2] 쿠키 역직렬화 실패: {}", e.getMessage());
      }
      return null;
    }
  }

  @Override
  public OAuth2AuthorizationRequest removeAuthorizationRequest(
      HttpServletRequest request, HttpServletResponse response) {
    OAuth2AuthorizationRequest authorizationRequest = this.loadAuthorizationRequest(request);
    removeAuthorizationRequestCookies(request, response);
    return authorizationRequest;
  }

  @Override
  public void saveAuthorizationRequest(
      OAuth2AuthorizationRequest authorizationRequest,
      HttpServletRequest request,
      HttpServletResponse response) {
    if (authorizationRequest == null) {
      CookieUtils.deleteCookie(request, response, OAUTH2_AUTHORIZATION_REQUEST_COOKIE_NAME);
      CookieUtils.deleteCookie(request, response, REDIRECT_URI_PARAM_COOKIE_NAME);
      return;
    }

    CookieUtils.addOAuth2RequestCookie(
        response,
        OAUTH2_AUTHORIZATION_REQUEST_COOKIE_NAME,
        CookieUtils.serialize(authorizationRequest),
        cookieExpireSeconds);
    log.info("[OAuth2] authorization_request 쿠키 저장 완료 (정상 흐름: 카카오 리다이렉트 후 콜백에서 사용)");

    String redirectUriAfterLogin = request.getParameter(REDIRECT_URI_PARAM_COOKIE_NAME);
    if (StringUtils.isNotBlank(redirectUriAfterLogin)
        && isAuthorizedRedirectUri(redirectUriAfterLogin)) {
      CookieUtils.addOAuth2RequestCookie(
          response, REDIRECT_URI_PARAM_COOKIE_NAME, redirectUriAfterLogin, cookieExpireSeconds);
    }
  }

  public void removeAuthorizationRequestCookies(
      HttpServletRequest request, HttpServletResponse response) {
    CookieUtils.deleteCookie(request, response, OAUTH2_AUTHORIZATION_REQUEST_COOKIE_NAME);
    CookieUtils.deleteCookie(request, response, REDIRECT_URI_PARAM_COOKIE_NAME);
  }

  private boolean isAuthorizedRedirectUri(String uri) {
    try {
      URI redirectUri = URI.create(uri);
      URI authorizedUri = URI.create(frontendUrl);

      return authorizedUri.getHost().equalsIgnoreCase(redirectUri.getHost())
          && authorizedUri.getScheme().equalsIgnoreCase(redirectUri.getScheme());
    } catch (IllegalArgumentException e) {
      return false;
    }
  }
}

package com.example.picknwhip_be.global.apiPayload.annotation.resolver;

import com.example.picknwhip_be.global.apiPayload.annotation.ExtractPayload;
import org.springframework.core.MethodParameter;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

@Component
public class ExtractPayloadArgumentResolver implements HandlerMethodArgumentResolver {

  @Override
  public boolean supportsParameter(MethodParameter parameter) {
    return parameter.hasParameterAnnotation(ExtractPayload.class)
        && parameter.getParameterType().equals(Long.class);
  }

  @Override
  public Object resolveArgument(
      MethodParameter parameter,
      ModelAndViewContainer mavContainer,
      NativeWebRequest webRequest,
      WebDataBinderFactory binderFactory) {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

    if (authentication == null || authentication.getName() == null) {
      throw new IllegalStateException("인증 정보가 없습니다.");
    }

    try {
      return Long.parseLong(authentication.getName());
    } catch (NumberFormatException e) {
      throw new IllegalArgumentException("유효하지 않은 유저 ID 형식입니다.", e);
    }
  }
}

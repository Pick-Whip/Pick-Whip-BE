package com.example.picknwhip_be.global.apiPayload.annotation.resolver;

import com.example.picknwhip_be.global.apiPayload.annotation.ExtractPayload;
import org.springframework.core.MethodParameter;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageDeliveryException;
import org.springframework.messaging.handler.invocation.HandlerMethodArgumentResolver;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

@Component
public class ExtractPayloadMessageArgumentResolver implements HandlerMethodArgumentResolver {

  @Override
  public boolean supportsParameter(MethodParameter parameter) {
    // @ExtractPayload 어노테이션이 있고 타입이 Long인 경우 지원
    return parameter.hasParameterAnnotation(ExtractPayload.class)
        && parameter.getParameterType().equals(Long.class);
  }

  @Override
  public Object resolveArgument(MethodParameter parameter, Message<?> message) {
    StompHeaderAccessor accessor = StompHeaderAccessor.wrap(message);
    Authentication authentication = (Authentication) accessor.getUser();

    if (authentication == null) {
      throw new MessageDeliveryException("인증 정보가 없습니다.");
    }

    try {
      // JWT에서 추출되어 Principal(Name)에 저장된 String ID를 Long으로 변환
      return Long.parseLong(authentication.getName());
    } catch (NumberFormatException e) {
      // 파싱 실패 시 명시적인 예외를 던져 안전하게 차단
      throw new MessageDeliveryException("유효하지 않은 유저 ID 형식입니다.");
    }
  }
}

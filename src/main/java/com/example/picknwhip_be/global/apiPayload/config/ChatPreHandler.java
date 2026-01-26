package com.example.picknwhip_be.global.apiPayload.config;

import com.example.picknwhip_be.global.apiPayload.code.AuthErrorCode;
import com.example.picknwhip_be.global.apiPayload.util.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.MessageDeliveryException;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ChatPreHandler implements ChannelInterceptor {

  private final JwtTokenProvider jwtTokenProvider;

  @Override
  public Message<?> preSend(Message<?> message, MessageChannel channel) {
    StompHeaderAccessor accessor =
        MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);
    if (accessor == null) {
      return message;
    }

    if (StompCommand.CONNECT.equals(accessor.getCommand())
        || StompCommand.SEND.equals(accessor.getCommand())) {
      String authorizationHeader = accessor.getFirstNativeHeader("Authorization");

      // 헤더가 없는 경우
      if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
        throw new MessageDeliveryException(AuthErrorCode.TOKEN_NOT_FOUND.getMessage());
      }

      String token = authorizationHeader.substring(7);

      // 토큰 검증
      if (jwtTokenProvider.validateToken(token)) {
        Authentication authentication = jwtTokenProvider.getAuthentication(token);
        accessor.setUser(authentication);
      } else {
        // 유효하지 않은 토큰
        throw new MessageDeliveryException(AuthErrorCode.TOKEN_EXPIRED.getMessage());
      }
    }
    return message;
  }
}

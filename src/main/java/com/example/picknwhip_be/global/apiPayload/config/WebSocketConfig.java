package com.example.picknwhip_be.global.apiPayload.config;

import com.example.picknwhip_be.global.apiPayload.annotation.resolver.ExtractPayloadMessageArgumentResolver;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.handler.invocation.HandlerMethodArgumentResolver;
import org.springframework.messaging.simp.config.ChannelRegistration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.*;

@Configuration
@EnableWebSocketMessageBroker
@RequiredArgsConstructor
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {
  private final ExtractPayloadMessageArgumentResolver extractPayloadMessageArgumentResolver;
  private final ChatPreHandler chatPreHandler;

  @Value("${app.frontend-url}")
  private String frontendUrl;

  @Override
  public void registerStompEndpoints(StompEndpointRegistry registry) {
    registry
        .addEndpoint("/ws")
        .setAllowedOriginPatterns(frontendUrl, "http://localhost:3000", "http://localhost:5173")
        .withSockJS();
  }

  @Override
  public void configureMessageBroker(MessageBrokerRegistry registry) {
    // 메시지 구독 요청
    registry.enableSimpleBroker("/topic");
    // 메시지 발행 요청
    registry.setApplicationDestinationPrefixes("/app");
  }

  @Override
  public void configureClientInboundChannel(ChannelRegistration registration) {
    registration.interceptors(chatPreHandler); // 인터셉터 등록
  }

  @Override
  public void addArgumentResolvers(List<HandlerMethodArgumentResolver> argumentResolvers) {
    // 웹소켓용 리졸버 등록
    argumentResolvers.add(extractPayloadMessageArgumentResolver);
  }
}

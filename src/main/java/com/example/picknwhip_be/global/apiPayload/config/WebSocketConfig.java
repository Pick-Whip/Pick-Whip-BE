package com.example.picknwhip_be.global.apiPayload.config;

import com.example.picknwhip_be.global.apiPayload.annotation.resolver.ExtractPayloadMessageArgumentResolver;
import java.util.List;
import lombok.RequiredArgsConstructor;
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

  @Override
  public void registerStompEndpoints(StompEndpointRegistry registry) {
    // TODO: 프론트엔드 애플리케이션 실제 도메인만 허용하도록 설정하기
    registry.addEndpoint("/ws").setAllowedOriginPatterns("*").withSockJS();
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

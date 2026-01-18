package com.example.picknwhip_be.global.apiPayload.config;

import com.example.picknwhip_be.domain.user.service.CustomOAuth2UserService;
import com.example.picknwhip_be.global.apiPayload.handler.OAuth2AuthenticationSuccessHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

  private final CustomOAuth2UserService customOAuth2UserService; // 주입
  private final OAuth2AuthenticationSuccessHandler oAuth2AuthenticationSuccessHandler; // 주입

  @Bean
  public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
    http.csrf(AbstractHttpConfigurer::disable) // CSRF 보안 비활성화
        .formLogin(AbstractHttpConfigurer::disable) // 기본 로그인 폼 비활성화
        .httpBasic(AbstractHttpConfigurer::disable) // HTTP Basic 인증 비활성화
        .authorizeHttpRequests(
            authorize ->
                authorize
                    // Swagger UI 및 API 관련 경로는 모두 허용
                    .requestMatchers("/v3/api-docs/**", "/swagger-ui/**", "/swagger-ui.html")
                    .permitAll()
                    // 그 외의 모든 요청은 일단 허용 (나중에 로그인이 완성되면 인증 필요로 변경)
                    // TODO: 배포 전 반드시 authenticated()로 변경할 것
                    .anyRequest()
                    .permitAll())
        // OAuth2 로그인 설정
        .oauth2Login(
            oauth2 ->
                oauth2
                    .userInfoEndpoint(userInfo -> userInfo.userService(customOAuth2UserService))
                    .successHandler(oAuth2AuthenticationSuccessHandler));

    return http.build();
  }
}

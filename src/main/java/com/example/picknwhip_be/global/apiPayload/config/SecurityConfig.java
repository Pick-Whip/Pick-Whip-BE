package com.example.picknwhip_be.global.apiPayload.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

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
                    .permitAll());

    return http.build();
  }
}

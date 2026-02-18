package com.example.picknwhip_be.global.apiPayload.config;

import com.example.picknwhip_be.domain.user.service.CustomOAuth2UserService;
import com.example.picknwhip_be.global.apiPayload.handler.OAuth2AuthenticationSuccessHandler;
import com.example.picknwhip_be.global.apiPayload.util.HttpCookieOAuth2AuthorizationRequestRepository;
import com.example.picknwhip_be.global.apiPayload.util.JwtTokenProvider;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {
  private final CustomAuthenticationEntryPoint customAuthenticationEntryPoint;
  private final CustomOAuth2UserService customOAuth2UserService;
  private final OAuth2AuthenticationSuccessHandler oAuth2AuthenticationSuccessHandler;
  private final JwtTokenProvider jwtTokenProvider;

  @Value("${app.frontend-url}")
  private String frontendUrl;

  @Bean
  public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

    http.cors(cors -> cors.configurationSource(corsConfigurationSource()))
        .csrf(AbstractHttpConfigurer::disable) // CSRF 보안 비활성화
        .formLogin(AbstractHttpConfigurer::disable) // 기본 로그인 폼 비활성화
        .httpBasic(AbstractHttpConfigurer::disable) // HTTP Basic 인증 비활성화
        .sessionManagement(
            session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
        .exceptionHandling(
            exception -> exception.authenticationEntryPoint(customAuthenticationEntryPoint))
        .authorizeHttpRequests(
            authorize ->
                authorize
                    .requestMatchers(org.springframework.web.cors.CorsUtils::isPreFlightRequest)
                    .permitAll()
                    .requestMatchers(
                        "/v3/api-docs/**",
                        "/swagger-ui/**",
                        "/swagger-ui.html",
                        "/swagger-ui/index.html",
                        "/swagger-resources/**",
                        "/webjars/**",
                        "/login/**",
                        "/oauth2/**")
                    .permitAll()
                    .requestMatchers("/api/users/extra/info")
                    .authenticated()
                    .requestMatchers("/api/test/**")
                    .permitAll()
                    .requestMatchers("/api/users/refresh")
                    .permitAll()
                    // 그 외 모든 API는 인증 필요
                    .anyRequest()
                    .authenticated())
        .oauth2Login(
            oauth2 ->
                oauth2
                    .userInfoEndpoint(userInfo -> userInfo.userService(customOAuth2UserService))
                    .authorizationEndpoint(
                        authorization ->
                            authorization
                                .baseUri("/oauth2/authorization")
                                .authorizationRequestRepository(
                                    new HttpCookieOAuth2AuthorizationRequestRepository()))
                    .successHandler(oAuth2AuthenticationSuccessHandler)
                    .failureHandler(
                        (request, response, exception) -> {
                          System.out.println("OAuth2 Login Failure: " + exception.getMessage());
                          exception.printStackTrace();

                          String errorMessage =
                              java.net.URLEncoder.encode(exception.getMessage(), "UTF-8");
                          response.sendRedirect(frontendUrl + "/login?error=" + errorMessage);
                        }))
        .addFilterBefore(
            new JwtAuthenticationFilter(jwtTokenProvider),
            UsernamePasswordAuthenticationFilter.class);

    return http.build();
  }

  @Bean
  public CorsConfigurationSource corsConfigurationSource() {
    CorsConfiguration configuration = new CorsConfiguration();
    // Swagger UI와 로컬 프론트엔드 주소 허용
    configuration.setAllowedOriginPatterns(
        List.of(
            "http://localhost:8080",
            "http://localhost:3000",
            "http://localhost:5173",
            "https://pick-whip.vercel.app",
            "https://*.vercel.app",
            "https://www.picknwhip.shop"));
    configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS", "PATCH"));
    configuration.setAllowedHeaders(List.of("*"));
    configuration.setAllowCredentials(true);

    UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
    source.registerCorsConfiguration("/**", configuration);
    return source;
  }
}

package com.example.picknwhip_be.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import java.util.List;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Profile("!test")
@Configuration
public class SwaggerConfig {

  @Value("${springdoc.server-url:http://localhost:8080}")
  private String serverUrl;

  @Bean
  public OpenAPI openAPI() {
    Info info =
        new Info().title("Pick&Whip API").description("Pick&Whip 백엔드 API 문서입니다.").version("1.0.0");

    // Authorize 버튼부분
    SecurityScheme bearerAuth =
        new SecurityScheme().type(SecurityScheme.Type.HTTP).scheme("bearer").bearerFormat("JWT");

    return new OpenAPI()
        .info(info)
        .servers(List.of(new Server().url(serverUrl)))
        .components(new Components().addSecuritySchemes("bearerAuth", bearerAuth));
  }
}

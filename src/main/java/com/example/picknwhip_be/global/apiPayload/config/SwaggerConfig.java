package com.example.picknwhip_be.global.apiPayload.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
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
    String securitySchemeName = "bearerAuth";

    SecurityRequirement securityRequirement = new SecurityRequirement().addList(securitySchemeName);

    Components components =
        new Components()
            .addSecuritySchemes(
                securitySchemeName,
                new SecurityScheme()
                    .name(securitySchemeName)
                    .type(SecurityScheme.Type.HTTP)
                    .scheme("bearer")
                    .bearerFormat("JWT"));

    Server prodServer =
        new Server().url("https://www.api.picknwhip.shop").description("운영 서버 (HTTPS)");
    Server localServer = new Server().url("http://localhost:8080").description("로컬 개발 서버");

    return new OpenAPI()
        .info(info)
        .addSecurityItem(securityRequirement)
        .components(components)
        .addServersItem(prodServer)
        .addServersItem(localServer);
  }
}

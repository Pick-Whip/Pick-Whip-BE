package com.example.picknwhip_be.domain.test.controller;

import com.example.picknwhip_be.global.apiPayload.ApiResponse;
import com.example.picknwhip_be.global.apiPayload.code.GeneralSuccessCode;
import com.example.picknwhip_be.global.apiPayload.util.JwtTokenProvider;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

// TODO: 배포 시 이 파일 지우기!!!
@Tag(name = "Test Auth", description = "테스트용 인증 API")
@RestController
@RequestMapping("/api/test")
@RequiredArgsConstructor
public class TestAuthController {

  private final JwtTokenProvider jwtTokenProvider;

  @GetMapping("/token")
  public ApiResponse<String> getTestToken(@RequestParam Long userId) {

    String token = jwtTokenProvider.createToken(userId);
    return ApiResponse.of(GeneralSuccessCode.OK, token);
  }
}

package com.example.picknwhip_be.global.apiPayload.config;

import com.example.picknwhip_be.global.apiPayload.ApiResponse;
import com.example.picknwhip_be.global.apiPayload.code.AuthErrorCode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

@Component
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {
  private final ObjectMapper objectMapper = new ObjectMapper();

  @Override
  public void commence(
      HttpServletRequest request,
      HttpServletResponse response,
      AuthenticationException authException)
      throws IOException {
    AuthErrorCode errorCode = (AuthErrorCode) request.getAttribute("exception");
    if (errorCode == null) {
      errorCode = AuthErrorCode.TOKEN_NOT_FOUND;
    }

    response.setContentType("application/json;charset=UTF-8");
    response.setStatus(errorCode.getStatus().value());

    ApiResponse<Object> apiResponse = ApiResponse.onFailure(errorCode, null);
    response.getWriter().write(objectMapper.writeValueAsString(apiResponse));
  }
}

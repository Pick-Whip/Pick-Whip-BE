package com.example.picknwhip_be.global.apiPayload.code;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum AuthErrorCode implements BaseErrorCode {
  LOGIN_REQUIRED(HttpStatus.UNAUTHORIZED, "AUTH401_1", "로그인이 필요합니다."),
  TOKEN_EXPIRED(HttpStatus.UNAUTHORIZED, "AUTH401_2", "토큰이 만료되었습니다."),
  INVALID_TOKEN(HttpStatus.UNAUTHORIZED, "AUTH401_3", "유효하지 않은 토큰입니다."),
  TOKEN_NOT_FOUND(HttpStatus.UNAUTHORIZED, "AUTH401_4", "인증 헤더에 토큰이 없습니다."),
  FORBIDDEN(HttpStatus.FORBIDDEN, "AUTH403_1", "해당 리소스에 대한 접근 권한이 없습니다.");
  ;

  private final HttpStatus status;
  private final String code;
  private final String message;
}

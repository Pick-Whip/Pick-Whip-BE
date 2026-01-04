package com.example.picknwhip_be.domain.test.exception.code;

import com.example.picknwhip_be.global.apiPayload.code.BaseErrorCode;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum TestErrorCode implements BaseErrorCode {
  TEST_EXCEPTION(HttpStatus.BAD_REQUEST, "TEST400_1", "테스트 입니다"),
  ;

  private final HttpStatus status;
  private final String code;
  private final String message;
}

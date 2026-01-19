package com.example.picknwhip_be.domain.custom.exception.code;

import com.example.picknwhip_be.global.apiPayload.code.BaseErrorCode;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum CustomOptionErrorCode implements BaseErrorCode {
  OPTION_NOT_FOUND(HttpStatus.NOT_FOUND, "OPTION404_1", "해당 옵션을 찾을 수 없습니다.");

  private final HttpStatus status;
  private final String code;
  private final String message;
}

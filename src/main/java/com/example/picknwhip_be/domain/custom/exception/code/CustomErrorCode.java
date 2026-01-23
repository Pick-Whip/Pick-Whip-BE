package com.example.picknwhip_be.domain.custom.exception.code;

import com.example.picknwhip_be.global.apiPayload.code.BaseErrorCode;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum CustomErrorCode implements BaseErrorCode {
  DRAFT_NOT_FOUND(HttpStatus.NOT_FOUND, "DRAFT404_1", "해당 임시저장을 찾을 수 없습니다."),
  OPTION_NOT_FOUND(HttpStatus.NOT_FOUND, "OPTION404_1", "해당 옵션을 찾을 수 없습니다."),
  FORBIDDEN(HttpStatus.NOT_FOUND, "OPTION404_2", "본인의 임시저장 디자인이 아닙니다.");

  private final HttpStatus status;
  private final String code;
  private final String message;
}

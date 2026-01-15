package com.example.picknwhip_be.domain.report.exception.code;

import com.example.picknwhip_be.global.apiPayload.code.BaseErrorCode;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ReportErrorCode implements BaseErrorCode {
  REPORT_FAILED(HttpStatus.BAD_REQUEST, "REPORT400_1", "신고 처리에 실패했습니다.");

  private final HttpStatus status;
  private final String code;
  private final String message;
}

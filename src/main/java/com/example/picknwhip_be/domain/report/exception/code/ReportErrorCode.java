package com.example.picknwhip_be.domain.report.exception.code;

import com.example.picknwhip_be.global.apiPayload.code.BaseErrorCode;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ReportErrorCode implements BaseErrorCode {
  DUPLICATE_REPORT(HttpStatus.BAD_REQUEST, "REPORT400_1", "이미 해당 대상을 신고하셨습니다."),
  INVALID_REPORT_TARGET(HttpStatus.BAD_REQUEST, "REPORT400_2", "본인의 게시글이나 자신은 신고할 수 없습니다."),
  TARGET_NOT_FOUND(HttpStatus.NOT_FOUND, "REPORT404_1", "신고하려는 대상을 찾을 수 없습니다."),
  REPORT_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, "REPORT500_1", "신고 처리 중 서버 오류가 발생했습니다."),
  ;

  private final HttpStatus status;
  private final String code;
  private final String message;
}

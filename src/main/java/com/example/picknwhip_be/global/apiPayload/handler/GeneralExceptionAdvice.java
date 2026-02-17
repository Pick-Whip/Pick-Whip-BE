package com.example.picknwhip_be.global.apiPayload.handler;

import com.example.picknwhip_be.global.apiPayload.ApiResponse;
import com.example.picknwhip_be.global.apiPayload.code.BaseErrorCode;
import com.example.picknwhip_be.global.apiPayload.code.GeneralErrorCode;
import com.example.picknwhip_be.global.apiPayload.exception.GeneralException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class GeneralExceptionAdvice {

  @ExceptionHandler(GeneralException.class)
  public ResponseEntity<ApiResponse<Void>> handleException(GeneralException ex) {

    return ResponseEntity.status(ex.getCode().getStatus())
        .body(ApiResponse.onFailure(ex.getCode(), null));
  }

  @ExceptionHandler(DataIntegrityViolationException.class)
  public ResponseEntity<ApiResponse<Object>> handleDbException(DataIntegrityViolationException ex) {
    log.error("DB Error: {}", ex.getMessage());

    BaseErrorCode code = GeneralErrorCode.BAD_REQUEST;
    return ResponseEntity.status(code.getStatus())
        .body(ApiResponse.onFailure(code, "데이터 처리 중 오류가 발생했습니다. (참조 ID 등을 확인하세요)"));
  }

  /** 인증 없이 접근 시 @ExtractPayload에서 발생 → 401 반환 (500 방지) */
  @ExceptionHandler({IllegalStateException.class, IllegalArgumentException.class})
  public ResponseEntity<ApiResponse<Void>> handleAuthRelated(RuntimeException ex) {
    if (ex.getMessage() != null
        && (ex.getMessage().contains("인증 정보가 없습니다") || ex.getMessage().contains("유효하지 않은 유저 ID"))) {
      return ResponseEntity.status(401)
          .body(ApiResponse.onFailure(GeneralErrorCode.UNAUTHORIZED, null));
    }
    throw ex;
  }

  @ExceptionHandler(Exception.class)
  public ResponseEntity<ApiResponse<String>> handleException(Exception ex) {

    log.error("Unexpected Error Occurred: ", ex);
    BaseErrorCode code = GeneralErrorCode.INTERNAL_SERVER_ERROR;
    return ResponseEntity.status(code.getStatus()).body(ApiResponse.onFailure(code, null));
  }
}

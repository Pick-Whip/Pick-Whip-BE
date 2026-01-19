package com.example.picknwhip_be.domain.S3.exception.code;

import com.example.picknwhip_be.global.apiPayload.code.BaseErrorCode;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum S3ErrorCode implements BaseErrorCode {
  TOO_MANY_FILES(HttpStatus.BAD_REQUEST, "S3400_1", "업로드 가능한 파일 개수를 초과했습니다."),

  INVALID_FILE_NAME(HttpStatus.BAD_REQUEST, "S3400_2", "유효하지 않은 파일 이름입니다."),

  PRESIGNED_URL_GENERATION_FAILED(
      HttpStatus.INTERNAL_SERVER_ERROR, "S3500_1", "파일 업로드 URL 생성에 실패했습니다."),

  AWS_SDK_CLIENT_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "S3500_2", "AWS 서비스와 통신 중 오류가 발생했습니다."),

  S3_OPERATION_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, "S3500_3", "S3 처리 중 오류가 발생했습니다."),

  S3_BUCKET_NOT_CONFIGURED(HttpStatus.INTERNAL_SERVER_ERROR, "S3500_4", "S3 버킷 설정이 올바르지 않습니다.");

  private final HttpStatus status;
  private final String code;
  private final String message;
}

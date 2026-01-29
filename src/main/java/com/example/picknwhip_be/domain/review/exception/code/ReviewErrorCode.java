package com.example.picknwhip_be.domain.review.exception.code;

import com.example.picknwhip_be.global.apiPayload.code.BaseErrorCode;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ReviewErrorCode implements BaseErrorCode {
  INVALID_REVIEW_IMAGE_KEY(HttpStatus.BAD_REQUEST, "REVIEW400_1", "유효하지 않은 이미지 키입니다."),

  REVIEW_KEYWORD_DUPLICATED(HttpStatus.BAD_REQUEST, "REVIEW400_2", "중복된 리뷰 키워드가 포함되어 있습니다."),

  INVALID_REVIEW_KEYWORD_CODE(
      HttpStatus.BAD_REQUEST, "REVIEW400_3", "유효하지 않은 리뷰 키워드 코드가 포함되어 있습니다."),

  REVIEW_WRITE_NOT_ALLOWED(HttpStatus.FORBIDDEN, "REVIEW403_1", "해당 주문에 대한 리뷰 작성 권한이 없습니다."),

  ORDER_NOT_FOUND(HttpStatus.NOT_FOUND, "REVIEW404_1", "존재하지 않는 주문입니다."),

  REVIEW_ALREADY_EXISTS(HttpStatus.CONFLICT, "REVIEW409_1", "이미 해당 주문에 대한 리뷰가 존재합니다."),

  REVIEW_IMAGE_VALIDATION_FAILED(
      HttpStatus.INTERNAL_SERVER_ERROR, "REVIEW500_1", "이미지 S3 검증 중 오류가 발생했습니다.");

  private final HttpStatus status;
  private final String code;
  private final String message;
}

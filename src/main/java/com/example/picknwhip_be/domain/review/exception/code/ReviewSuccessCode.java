package com.example.picknwhip_be.domain.review.exception.code;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ReviewSuccessCode {
  REVIEW_CREATED(HttpStatus.CREATED, "REVIEW200_1", "성공적으로 리뷰가 등록되었습니다.");

  private final HttpStatus status;
  private final String code;
  private final String message;
}

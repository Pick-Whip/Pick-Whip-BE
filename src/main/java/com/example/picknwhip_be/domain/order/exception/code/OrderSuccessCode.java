package com.example.picknwhip_be.domain.order.exception.code;

import com.example.picknwhip_be.global.apiPayload.code.BaseSuccessCode;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum OrderSuccessCode implements BaseSuccessCode { // [수정] 인터페이스 구현 추가
  PICKUP_UPDATED(HttpStatus.OK, "PICKUP200_1", "픽업 시간이 변경되었습니다.");

  private final HttpStatus status;
  private final String code;
  private final String message;
}

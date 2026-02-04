package com.example.picknwhip_be.domain.order.exception.code;

import com.example.picknwhip_be.global.apiPayload.code.BaseErrorCode;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum OrderErrorCode implements BaseErrorCode {
  ORDER_NOT_FOUND(HttpStatus.NOT_FOUND, "ORDER404_1", "해당 주문을 찾을 수 없습니다."),
  SLOT_ALREADY_FULL(HttpStatus.CONFLICT, "ORDER409_1", "선택하신 픽업 시간은 이미 마감되었습니다. 다른 시간을 선택해주세요."),
  INVALID_ORDER_STATUS(HttpStatus.BAD_REQUEST, "ORDER400_1", "유효하지 않은 주문 상태입니다."),
  INVALID_ORDER_HISTORY_TYPE(
      HttpStatus.BAD_REQUEST, "ORDER400_2", "유효하지 않은 조회 타입입니다. (REQUEST 또는 COMPLETE)");
  private final HttpStatus status;
  private final String code;
  private final String message;
}

package com.example.picknwhip_be.domain.order.exception.code;

import com.example.picknwhip_be.global.apiPayload.code.BaseErrorCode;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum OrderErrorCode implements BaseErrorCode {
  FORBIDDEN_ACCESS(HttpStatus.FORBIDDEN, "ORDER403", "해당 주문 정보에 접근 권한이 없습니다."),
  ORDER_NOT_FOUND(HttpStatus.NOT_FOUND, "ORDER404_1", "해당 주문을 찾을 수 없습니다."),
  SLOT_ALREADY_FULL(HttpStatus.CONFLICT, "ORDER409_1", "선택하신 픽업 시간은 이미 마감되었습니다. 다른 시간을 선택해주세요."),
  INVALID_ORDER_STATUS(HttpStatus.BAD_REQUEST, "ORDER400_1", "유효하지 않은 주문 상태입니다."),
  INVALID_ORDER_CONTEXT(HttpStatus.BAD_REQUEST, "ORDER400", "해당 가게의 주문 정보가 아닙니다."),
  INVALID_ORDER_HISTORY_TYPE(
      HttpStatus.BAD_REQUEST, "ORDER400_2", "유효하지 않은 조회 타입입니다. (REQUEST 또는 COMPLETE)"),
  INVALID_CURSOR_PARAMS(
      HttpStatus.BAD_REQUEST, "ORDER400_3", "커서 파라미터가 유효하지 않습니다. (ID가 존재하면 상태점수와 픽업시간도 필수입니다)");
  ;

  private final HttpStatus status;
  private final String code;
  private final String message;
}

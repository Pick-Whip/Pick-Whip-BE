package com.example.picknwhip_be.domain.order.dto.req;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

@Getter
@Setter
@NoArgsConstructor
public class OrderCursorReqDTO {

  @Schema(description = "한 번에 가져올 개수 (기본값: 10, 최대: 50)", example = "10")
  @Min(value = 1, message = "limit은 1 이상이어야 합니다.")
  @Max(value = 50, message = "limit은 50을 초과할 수 없습니다.")
  private int limit = 10;

  @Schema(description = "조회 타입 (REQUEST: 요청내역 / COMPLETE: 완료내역)", example = "REQUEST")
  private String type = "REQUEST";

  @Schema(description = "마지막으로 조회된 주문의 상태 점수 (응답의 nextCursor.statusScore)", nullable = true)
  private Integer lastStatusScore;

  @Schema(description = "마지막으로 조회된 주문의 픽업 일시 (응답의 nextCursor.pickupDatetime)", nullable = true)
  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
  private LocalDateTime lastPickupDatetime;

  @Schema(description = "마지막으로 조회된 주문의 ID (응답의 nextCursor.orderId)", nullable = true)
  private Long lastOrderId;
}

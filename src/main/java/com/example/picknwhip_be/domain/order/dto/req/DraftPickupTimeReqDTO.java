package com.example.picknwhip_be.domain.order.dto.req;

import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class DraftPickupTimeReqDTO {
  @NotNull(message = "픽업 시간은 필수입니다.")
  private LocalDateTime pickupDatetime;
}

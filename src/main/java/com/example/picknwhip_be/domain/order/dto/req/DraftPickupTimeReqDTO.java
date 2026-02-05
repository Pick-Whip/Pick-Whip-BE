package com.example.picknwhip_be.domain.order.dto.req;

import java.time.LocalDateTime;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class DraftPickupTimeReqDTO {
  private LocalDateTime pickupDatetime;
}

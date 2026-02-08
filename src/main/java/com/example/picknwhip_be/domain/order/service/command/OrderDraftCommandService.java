package com.example.picknwhip_be.domain.order.service.command;

import java.time.LocalDateTime;

public interface OrderDraftCommandService {
  void updatePickupTime(Long userId, Long draftId, LocalDateTime newPickupTime);
}

package com.example.picknwhip_be.domain.order.service.command;

import com.example.picknwhip_be.domain.custom.entity.OrderDraft;
import com.example.picknwhip_be.domain.custom.repository.OrderDraftRepository;
import com.example.picknwhip_be.domain.order.exception.OrderException;
import com.example.picknwhip_be.domain.order.exception.code.OrderErrorCode;
import com.example.picknwhip_be.domain.order.repository.OrderRepository;
import com.example.picknwhip_be.domain.shop.repository.ShopBusinessHourRepository;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class OrderDraftCommandServiceImpl implements OrderDraftCommandService {

  private final OrderDraftRepository orderDraftRepository;
  private final ShopBusinessHourRepository shopBusinessHourRepository;
  private final OrderRepository orderRepository;

  @Override
  public void updatePickupTime(Long userId, Long draftId, LocalDateTime newPickupTime) {
    OrderDraft draft =
        orderDraftRepository
            .findById(draftId)
            .orElseThrow(() -> new OrderException(OrderErrorCode.DRAFT_NOT_FOUND));

    if (!draft.getUser().getUserId().equals(userId)) {
      throw new OrderException(OrderErrorCode.FORBIDDEN_ACCESS);
    }
    draft.updatePickupDatetime(newPickupTime);
  }
}

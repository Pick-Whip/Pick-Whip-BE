package com.example.picknwhip_be.domain.order.service.command;

import com.example.picknwhip_be.domain.custom.entity.OrderDraft;
import com.example.picknwhip_be.domain.custom.repository.OrderDraftRepository;
import com.example.picknwhip_be.domain.order.entity.enums.Status;
import com.example.picknwhip_be.domain.order.exception.OrderException;
import com.example.picknwhip_be.domain.order.exception.code.OrderErrorCode;
import com.example.picknwhip_be.domain.order.repository.OrderRepository;
import com.example.picknwhip_be.domain.shop.entity.Shop;
import com.example.picknwhip_be.domain.shop.entity.ShopBusinessHour;
import com.example.picknwhip_be.domain.shop.entity.enums.ScheduleType;
import com.example.picknwhip_be.domain.shop.repository.ShopBusinessHourRepository;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
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
    validatePickupTime(draft.getShop(), newPickupTime);

    draft.updatePickupDatetime(newPickupTime);
  }

  /** 픽업 시간 유효성 검증 로직 */
  private void validatePickupTime(Shop shop, LocalDateTime pickupTime) {
    // A. 과거 시간 체크
    if (pickupTime.isBefore(LocalDateTime.now())) {
      throw new OrderException(OrderErrorCode.INVALID_PICKUP_TIME);
    }

    LocalDate date = pickupTime.toLocalDate();
    LocalTime time = pickupTime.toLocalTime();
    ShopBusinessHour hour =
        shopBusinessHourRepository
            .findByShopIdAndDateAndScheduleType(shop.getId(), date, ScheduleType.DATE)
            .orElseGet(
                () ->
                    shopBusinessHourRepository
                        .findByShopIdAndDayOfWeekAndScheduleType(
                            shop.getId(), date.getDayOfWeek().getValue(), ScheduleType.WEEKLY)
                        .orElse(null));

    if (hour == null || hour.isClosed()) {
      throw new OrderException(OrderErrorCode.SHOP_CLOSED_DAY);
    }

    if (time.isBefore(hour.getOpenTime()) || !time.isBefore(hour.getCloseTime())) {
      throw new OrderException(OrderErrorCode.SHOP_CLOSED_TIME);
    }

    long currentOrders =
        orderRepository.countByShopAndPickupTime(shop.getId(), pickupTime, Status.IMPOSSIBLE);
    int maxCapacity = shop.getMaxOrdersPerSlot() > 0 ? shop.getMaxOrdersPerSlot() : 2;

    if (currentOrders >= maxCapacity) {
      throw new OrderException(OrderErrorCode.SLOT_ALREADY_FULL);
    }
  }
}

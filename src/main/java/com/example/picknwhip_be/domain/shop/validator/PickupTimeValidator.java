package com.example.picknwhip_be.domain.shop.validator;

import com.example.picknwhip_be.domain.order.entity.enums.Status;
import com.example.picknwhip_be.domain.order.exception.OrderException;
import com.example.picknwhip_be.domain.order.exception.code.OrderErrorCode;
import com.example.picknwhip_be.domain.order.repository.OrderRepository;
import com.example.picknwhip_be.domain.shop.entity.Shop;
import com.example.picknwhip_be.domain.shop.entity.ShopBusinessHour;
import com.example.picknwhip_be.domain.shop.entity.enums.ScheduleType;
import com.example.picknwhip_be.domain.shop.repository.ShopBusinessHourRepository;
import java.time.Clock;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PickupTimeValidator {

  private final ShopBusinessHourRepository shopBusinessHourRepository;
  private final OrderRepository orderRepository;

  // kstClock만 주입받도록 명시 (기존 clock Bean 영향 없음)
  @Qualifier("kstClock")
  private final Clock clock;

  public void validate(Shop shop, LocalDateTime pickupTime) {
    if (pickupTime.isBefore(LocalDateTime.now(clock))) {
      throw new OrderException(OrderErrorCode.INVALID_PICKUP_TIME);
    }

    int interval = shop.getSlotIntervalMinutes() > 0 ? shop.getSlotIntervalMinutes() : 30;
    if (pickupTime.getMinute() % interval != 0
        || pickupTime.getSecond() != 0
        || pickupTime.getNano() != 0) {
      throw new OrderException(OrderErrorCode.INVALID_PICKUP_TIME);
    }

    LocalDate date = pickupTime.toLocalDate();
    LocalTime time = pickupTime.toLocalTime();

    List<ShopBusinessHour> hours = shopBusinessHourRepository.findAllByShopId(shop.getId());
    ShopBusinessHour hour = getAppliedHour(hours, date);

    if (hour == null || hour.isClosed()) {
      throw new OrderException(OrderErrorCode.SHOP_CLOSED_DAY);
    }

    if (time.isBefore(hour.getOpenTime()) || !time.isBefore(hour.getCloseTime())) {
      throw new OrderException(OrderErrorCode.SHOP_CLOSED_TIME);
    }

    List<Status> excludedStatuses = Arrays.asList(Status.CANCELED_BY_SHOP, Status.PAYMENT_FAILED);

    long currentOrders =
        orderRepository.countByShopAndPickupTime(shop.getId(), pickupTime, excludedStatuses);

    int maxCapacity = shop.getMaxOrdersPerSlot() > 0 ? shop.getMaxOrdersPerSlot() : 2;

    if (currentOrders >= maxCapacity) {
      throw new OrderException(OrderErrorCode.SLOT_ALREADY_FULL);
    }
  }

  private ShopBusinessHour getAppliedHour(List<ShopBusinessHour> hours, LocalDate date) {
    Map<String, ShopBusinessHour> map = new HashMap<>();
    for (ShopBusinessHour h : hours) {
      if (h.getScheduleType() == ScheduleType.DATE) {
        map.put("DATE:" + h.getDate(), h);
      } else {
        map.put("WEEKLY:" + h.getDayOfWeek(), h);
      }
    }

    if (map.containsKey("DATE:" + date)) return map.get("DATE:" + date);
    return map.get("WEEKLY:" + date.getDayOfWeek().getValue());
  }
}

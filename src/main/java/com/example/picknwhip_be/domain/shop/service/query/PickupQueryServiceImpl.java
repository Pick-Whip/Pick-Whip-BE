package com.example.picknwhip_be.domain.shop.service.query;

import com.example.picknwhip_be.domain.order.entity.enums.Status;
import com.example.picknwhip_be.domain.order.repository.OrderRepository;
import com.example.picknwhip_be.domain.shop.converter.PickupConverter;
import com.example.picknwhip_be.domain.shop.dto.res.PickupResDTO;
import com.example.picknwhip_be.domain.shop.entity.Shop;
import com.example.picknwhip_be.domain.shop.entity.ShopBusinessHour;
import com.example.picknwhip_be.domain.shop.entity.enums.ScheduleType;
import com.example.picknwhip_be.domain.shop.exception.ShopException;
import com.example.picknwhip_be.domain.shop.exception.code.ShopErrorCode;
import com.example.picknwhip_be.domain.shop.repository.ShopBusinessHourRepository;
import com.example.picknwhip_be.domain.shop.repository.ShopRepository;
import java.time.Clock;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PickupQueryServiceImpl implements PickupQueryService {

  private final ShopRepository shopRepository;
  private final ShopBusinessHourRepository businessHourRepository;
  private final OrderRepository orderRepository;
  private final PickupConverter pickupConverter;

  // kstClock만 주입받도록 명시 (기존 clock Bean 영향 없음)
  @Qualifier("kstClock")
  private final Clock clock;

  /** 월간 캘린더 조회 (날짜별 휴무 여부) */
  @Override
  public List<PickupResDTO.MonthlyStatusDTO> getMonthlyAvailability(
      Long shopId, int year, int month) {
    if (!shopRepository.existsById(shopId)) {
      throw new ShopException(ShopErrorCode.SHOP_NOT_FOUND);
    }

    LocalDate start = LocalDate.of(year, month, 1);
    LocalDate end = start.withDayOfMonth(start.lengthOfMonth());
    Map<String, ShopBusinessHour> rules = getRulesMap(shopId);
    List<PickupResDTO.MonthlyStatusDTO> result = new ArrayList<>();

    LocalDate current = start;
    while (!current.isAfter(end)) {
      boolean isClosed = isDayClosed(rules, current);
      result.add(pickupConverter.toMonthlyStatus(current, isClosed));
      current = current.plusDays(1);
    }
    return result;
  }

  /** 일간 상세 조회 (30분 단위 슬롯) */
  @Override
  public PickupResDTO.DailySlotsDTO getDailySlots(Long shopId, LocalDate date) {
    Shop shop =
        shopRepository
            .findById(shopId)
            .orElseThrow(() -> new ShopException(ShopErrorCode.SHOP_NOT_FOUND));

    Map<String, ShopBusinessHour> rules = getRulesMap(shopId);
    ShopBusinessHour hour = getAppliedHour(rules, date);

    if (hour == null || hour.isClosed()) {
      return pickupConverter.toDailySlots(date, true, Collections.emptyList());
    }
    List<Status> excluded = List.of(Status.CANCELED_BY_SHOP, Status.PAYMENT_FAILED);
    List<Object[]> counts =
        orderRepository.countOrdersByShopAndDateRange(
            shopId, date.atStartOfDay(), date.atTime(LocalTime.MAX), excluded);

    Map<LocalTime, Long> reservedMap =
        counts.stream()
            .collect(
                Collectors.toMap(
                    obj -> ((LocalDateTime) obj[0]).toLocalTime().withSecond(0).withNano(0),
                    obj -> (Long) obj[1],
                    Long::sum));

    List<PickupResDTO.TimeSlotDTO> slotDTOs = new ArrayList<>();
    LocalTime current = hour.getOpenTime();
    LocalDateTime now = LocalDateTime.now(clock);

    int interval = shop.getSlotIntervalMinutes() > 0 ? shop.getSlotIntervalMinutes() : 30;
    int maxCapacity = shop.getMaxOrdersPerSlot() > 0 ? shop.getMaxOrdersPerSlot() : 2;

    while (current.isBefore(hour.getCloseTime())) {
      LocalDateTime slotDateTime = LocalDateTime.of(date, current);

      boolean isPast = slotDateTime.isBefore(now);

      long booked = reservedMap.getOrDefault(current, 0L);
      boolean isFull = booked >= maxCapacity;

      boolean isAvailable = !isPast && !isFull;
      String reason = "AVAILABLE";
      if (isPast) reason = "PAST";
      else if (isFull) reason = "FULL";

      slotDTOs.add(pickupConverter.toTimeSlot(current, interval, isAvailable, reason));

      current = current.plusMinutes(interval);
    }

    return pickupConverter.toDailySlots(date, false, slotDTOs);
  }

  private Map<String, ShopBusinessHour> getRulesMap(Long shopId) {
    List<ShopBusinessHour> hours = businessHourRepository.findAllByShopId(shopId);
    Map<String, ShopBusinessHour> map = new HashMap<>();
    for (ShopBusinessHour h : hours) {
      if (h.getScheduleType() == ScheduleType.DATE) {
        map.put("DATE:" + h.getDate(), h);
      } else {
        map.put("WEEKLY:" + h.getDayOfWeek(), h);
      }
    }
    return map;
  }

  private ShopBusinessHour getAppliedHour(Map<String, ShopBusinessHour> rules, LocalDate date) {
    if (rules.containsKey("DATE:" + date)) return rules.get("DATE:" + date);
    return rules.get("WEEKLY:" + date.getDayOfWeek().getValue());
  }

  private boolean isDayClosed(Map<String, ShopBusinessHour> rules, LocalDate date) {
    ShopBusinessHour hour = getAppliedHour(rules, date);
    return hour == null || hour.isClosed();
  }
}

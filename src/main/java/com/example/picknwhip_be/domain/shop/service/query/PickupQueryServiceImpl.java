package com.example.picknwhip_be.domain.shop.service.query;

import com.example.picknwhip_be.domain.order.repository.OrderRepository;
import com.example.picknwhip_be.domain.shop.converter.PickupConverter;
import com.example.picknwhip_be.domain.shop.dto.res.PickupResDTO;
import com.example.picknwhip_be.domain.shop.entity.Shop;
import com.example.picknwhip_be.domain.shop.entity.ShopBusinessHour;
import com.example.picknwhip_be.domain.shop.entity.enums.ScheduleType;
import com.example.picknwhip_be.domain.shop.exception.code.ShopErrorCode;
import com.example.picknwhip_be.domain.shop.exception.ShopException;
import com.example.picknwhip_be.domain.shop.repository.ShopBusinessHourRepository;
import com.example.picknwhip_be.domain.shop.repository.ShopRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PickupQueryServiceImpl implements PickupQueryService {

    private final ShopRepository shopRepository;
    private final ShopBusinessHourRepository businessHourRepository;
    private final OrderRepository orderRepository;
    private final PickupConverter pickupConverter;

    @Override
    public List<PickupResDTO.MonthlyStatusDTO> getMonthlyAvailability(Long shopId, int year, int month) {
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

    @Override
    public PickupResDTO.DailySlotsDTO getDailySlots(Long shopId, LocalDate date) {
        Shop shop = shopRepository.findById(shopId)
                .orElseThrow(() -> new ShopException(ShopErrorCode.SHOP_NOT_FOUND));

        Map<String, ShopBusinessHour> rules = getRulesMap(shopId);
        ShopBusinessHour hour = getAppliedHour(rules, date);

        if (hour == null || hour.isClosed()) {
            return pickupConverter.toDailySlots(date, true, Collections.emptyList());
        }

        List<Object[]> counts = orderRepository.countOrdersByShopAndDateRange(
                shopId, date.atStartOfDay(), date.atTime(LocalTime.MAX));

        Map<LocalTime, Long> reservedMap = counts.stream().collect(Collectors.toMap(
                obj -> ((LocalDateTime) obj[0]).toLocalTime().withSecond(0).withNano(0),
                obj -> (Long) obj[1], Long::sum
        ));

        List<PickupResDTO.TimeSlotDTO> slotDTOs = new ArrayList<>();
        LocalTime current = hour.getOpenTime();
        LocalDateTime now = LocalDateTime.now();

        while (current.isBefore(hour.getCloseTime())) {
            boolean isPast = LocalDateTime.of(date, current).isBefore(now);
            long booked = reservedMap.getOrDefault(current, 0L);
            boolean isFull = booked >= shop.getMaxOrdersPerSlot();

            boolean isAvailable = !isPast && !isFull;
            String reason = isPast ? "PAST" : (isFull ? "FULL" : "AVAILABLE");

            slotDTOs.add(pickupConverter.toTimeSlot(
                    current, shop.getSlotIntervalMinutes(), isAvailable, reason));

            current = current.plusMinutes(shop.getSlotIntervalMinutes());
        }

        return pickupConverter.toDailySlots(date, false, slotDTOs);
    }
    private Map<String, ShopBusinessHour> getRulesMap(Long shopId) {
        List<ShopBusinessHour> hours = businessHourRepository.findAllByShopId(shopId);
        Map<String, ShopBusinessHour> map = new HashMap<>();
        for (ShopBusinessHour h : hours) {
            if (h.getScheduleType() == ScheduleType.DATE) map.put("DATE:" + h.getDate(), h);
            else map.put("WEEKLY:" + h.getDayOfWeek(), h);
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
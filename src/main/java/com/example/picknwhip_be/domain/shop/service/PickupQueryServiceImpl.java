package com.example.picknwhip_be.domain.shop.service;

import com.example.picknwhip_be.domain.order.repository.OrderRepository;
import com.example.picknwhip_be.domain.shop.dto.res.PickupResDTO;
import com.example.picknwhip_be.domain.shop.entity.ShopBusinessHour;
import com.example.picknwhip_be.domain.shop.entity.enums.ScheduleType;
import com.example.picknwhip_be.domain.shop.repository.ShopBusinessHourRepository;
import com.example.picknwhip_be.domain.shop.service.PickupQueryService;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PickupQueryServiceImpl implements PickupQueryService {

    private final ShopBusinessHourRepository businessHourRepository;
    private final OrderRepository orderRepository;

    // 30분 단위 슬롯 당 최대 수용량 (추후 Shop 엔티티로 이동 가능)
    private static final int SLOT_CAPACITY = 1;

    @Override
    public PickupResDTO.PickupCalendarDTO getAvailableSlots(Long shopId, LocalDate date) {
        ShopBusinessHour hour = businessHourRepository
                .findByShopIdAndDateAndScheduleType(shopId, date, ScheduleType.DATE)
                .orElseGet(() -> businessHourRepository
                        .findByShopIdAndDayOfWeekAndScheduleType(shopId, date.getDayOfWeek().getValue(), ScheduleType.WEEKLY)
                        .orElse(null));

        if (hour == null || hour.isClosed()) {
            return PickupResDTO.PickupCalendarDTO.builder()
                    .date(date.toString())
                    .isClosed(true)
                    .slots(Collections.emptyList())
                    .build();
        }

        List<Object[]> counts = orderRepository.countOrdersByShopAndDate(shopId, date);
        Map<LocalTime, Long> reservedCounts = counts.stream()
                .collect(Collectors.toMap(
                        obj -> ((LocalDateTime) obj[0]).toLocalTime(),
                        obj -> (Long) obj[1]
                ));

        List<PickupResDTO.TimeSlotDTO> slots = new ArrayList<>();
        LocalTime current = hour.getOpenTime();

        while (current.isBefore(hour.getCloseTime())) {
            long currentCount = reservedCounts.getOrDefault(current, 0L);
            boolean isAvailable = currentCount < SLOT_CAPACITY;

            slots.add(new PickupResDTO.TimeSlotDTO(current, isAvailable));
            current = current.plusMinutes(30);
        }

        return PickupResDTO.PickupCalendarDTO.builder()
                .date(date.toString())
                .isClosed(false)
                .slots(slots)
                .build();
    }
}
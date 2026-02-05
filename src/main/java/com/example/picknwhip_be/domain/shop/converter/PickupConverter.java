package com.example.picknwhip_be.domain.shop.converter;

import com.example.picknwhip_be.domain.shop.dto.res.PickupResDTO;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;
import org.springframework.stereotype.Component;

@Component
public class PickupConverter {

  // 2025.12.10 (수)
  private static final DateTimeFormatter DATE_FMT =
      DateTimeFormatter.ofPattern("yyyy.MM.dd (E)", Locale.KOREAN);

  // 10:30
  private static final DateTimeFormatter TIME_FMT = DateTimeFormatter.ofPattern("HH:mm");

  public PickupResDTO.MonthlyStatusDTO toMonthlyStatus(LocalDate date, boolean isClosed) {
    return PickupResDTO.MonthlyStatusDTO.builder().date(date).isClosed(isClosed).build();
  }

  public PickupResDTO.DailySlotsDTO toDailySlots(
      LocalDate date, boolean isClosed, List<PickupResDTO.TimeSlotDTO> slots) {

    return PickupResDTO.DailySlotsDTO.builder()
        .date(date.toString())
        .formattedDate(date.format(DATE_FMT))
        .isClosed(isClosed)
        .slots(slots)
        .build();
  }

  public PickupResDTO.TimeSlotDTO toTimeSlot(
      LocalTime startTime, int intervalMinutes, boolean isAvailable, String reason) {

    LocalTime endTime = startTime.plusMinutes(intervalMinutes);
    String label = startTime.format(TIME_FMT) + "~" + endTime.format(TIME_FMT);

    return PickupResDTO.TimeSlotDTO.builder()
        .time(startTime)
        .timeLabel(label)
        .isAvailable(isAvailable)
        .reason(reason)
        .build();
  }
}

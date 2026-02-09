package com.example.picknwhip_be.domain.shop.service.query;

import com.example.picknwhip_be.domain.shop.dto.res.PickupResDTO;
import java.time.LocalDate;
import java.util.List;

public interface PickupQueryService {
  List<PickupResDTO.MonthlyStatusDTO> getMonthlyAvailability(Long shopId, int year, int month);

  PickupResDTO.DailySlotsDTO getDailySlots(Long shopId, LocalDate date);
}

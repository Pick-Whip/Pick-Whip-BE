package com.example.picknwhip_be.domain.shop.service;

import com.example.picknwhip_be.domain.shop.dto.res.PickupResDTO;
import java.time.LocalDate;

public interface PickupQueryService {
  PickupResDTO.PickupCalendarDTO getAvailableSlots(Long shopId, LocalDate date);
}

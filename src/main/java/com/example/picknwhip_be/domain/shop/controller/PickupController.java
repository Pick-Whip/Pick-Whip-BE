package com.example.picknwhip_be.domain.shop.controller;

import com.example.picknwhip_be.domain.shop.dto.res.PickupResDTO;
import com.example.picknwhip_be.domain.shop.service.PickupQueryService;
import com.example.picknwhip_be.global.apiPayload.ApiResponse;
import com.example.picknwhip_be.global.apiPayload.code.GeneralSuccessCode;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.time.LocalDate;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Pickup API", description = "픽업 캘린더 관련 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/shops")
public class PickupController {

    private final PickupQueryService pickupQueryService;

    @Operation(summary = "픽업 가능 시간 조회", description = "특정 날짜의 예약 가능 시간을 조회합니다.")
    @GetMapping("/{shopId}/pickup-availability")
    public ApiResponse<PickupResDTO.PickupCalendarDTO> getPickupAvailability(
            @PathVariable Long shopId,
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate date) {

        PickupResDTO.PickupCalendarDTO result = pickupQueryService.getAvailableSlots(shopId, date);
        return ApiResponse.of(GeneralSuccessCode.OK, result);
    }
}
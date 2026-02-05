package com.example.picknwhip_be.domain.shop.controller;

import com.example.picknwhip_be.domain.shop.dto.res.PickupResDTO;
import com.example.picknwhip_be.domain.shop.service.query.PickupQueryService;
import com.example.picknwhip_be.global.apiPayload.ApiResponse;
import com.example.picknwhip_be.global.apiPayload.code.GeneralSuccessCode;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import java.time.LocalDate;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Pickup API", description = "픽업 캘린더 조회 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/shops")
@Validated
public class PickupController {

  private final PickupQueryService pickupQueryService;

  @Operation(summary = "월간 영업일 조회", description = "해당 월의 날짜별 영업/휴무 여부를 반환합니다.")
  @GetMapping("/{shopId}/calendar")
  public ApiResponse<List<PickupResDTO.MonthlyStatusDTO>> getMonthlyStatus(
      @PathVariable Long shopId, @RequestParam int year, @RequestParam @Min(1) @Max(12) int month) {
    return ApiResponse.of(
        GeneralSuccessCode.OK, pickupQueryService.getMonthlyAvailability(shopId, year, month));
  }

  @Operation(summary = "일간 시간 슬롯 조회", description = "특정 날짜의 예약 가능한 시간대(30분 단위)를 반환합니다. (요일 표기 포함)")
  @GetMapping("/{shopId}/slots")
  public ApiResponse<PickupResDTO.DailySlotsDTO> getDailySlots(
      @PathVariable Long shopId,
      @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate date) {
    return ApiResponse.of(GeneralSuccessCode.OK, pickupQueryService.getDailySlots(shopId, date));
  }
}

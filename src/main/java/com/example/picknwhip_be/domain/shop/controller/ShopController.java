package com.example.picknwhip_be.domain.shop.controller;

import com.example.picknwhip_be.domain.shop.dto.res.ShopPreviewResponseDto;
import com.example.picknwhip_be.domain.shop.service.ShopService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/shops")
@RequiredArgsConstructor
@Tag(name = "Shop API", description = "가게 관련 API")
public class ShopController {

  private final ShopService shopService;

  @Operation(
      summary = "내 주변 가게 조회",
      description = "현재 위치(lat, lon)를 기준으로 특정 반경(radius) 내의 가게 목록을 조회합니다.")
  @GetMapping("/nearby")
  public ResponseEntity<List<ShopPreviewResponseDto>> getNearbyShops(
      @RequestParam double lat,
      @RequestParam double lon,
      @RequestParam(defaultValue = "1000") double radius) {
    return ResponseEntity.ok(shopService.getNearbyShops(lat, lon, radius));
  }
}

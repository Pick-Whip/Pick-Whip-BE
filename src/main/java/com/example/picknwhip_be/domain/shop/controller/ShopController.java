package com.example.picknwhip_be.domain.shop.controller;

import com.example.picknwhip_be.domain.shop.converter.ShopConverter;
import com.example.picknwhip_be.domain.shop.dto.ShopResDTO;
import com.example.picknwhip_be.domain.shop.dto.res.ShopPreviewResponseDto;
import com.example.picknwhip_be.domain.shop.dto.res.ShopReqDTO;
import com.example.picknwhip_be.domain.shop.service.ShopQueryService;
import com.example.picknwhip_be.domain.shop.service.ShopService;
import com.example.picknwhip_be.global.apiPayload.ApiResponse;
import com.example.picknwhip_be.global.apiPayload.annotation.ExtractPayload;
import com.example.picknwhip_be.global.apiPayload.code.GeneralSuccessCode;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/shops")
@RequiredArgsConstructor
@Tag(name = "Shop API", description = "가게 관련 API")
public class ShopController {

  private final ShopService shopService;
  private final ShopQueryService shopQueryService;
  private final ShopConverter shopConverter;

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

  @Operation(
      summary = "뷰포트 내 가게 조회",
      description = "현재 뷰포트 내 가게들 정보를 보여줍니다. isPicked로 마이픽에 있는 가게인지 알 수 있습니다.")
  @GetMapping("/maps")
  public ApiResponse<ShopResDTO.ShopInMapListDTO> getShopOnMapList(
      @Parameter(description = "최소 위도 = 하단") @RequestParam Double lowLat,
      @Parameter(description = "최대 위도 = 상단") @RequestParam Double highLat,
      @Parameter(description = "최소 경도 = 좌측") @RequestParam Double lowLon,
      @Parameter(description = "최대 경도 = 우측") @RequestParam Double highLon,
      @ExtractPayload Long userId) {

    ShopResDTO.ShopInMapListDTO result =
        shopQueryService.findShopInMap(lowLat, highLat, lowLon, highLon, userId);
    return ApiResponse.of(GeneralSuccessCode.OK, result);
  }

  @GetMapping("/search")
  public ApiResponse<ShopResDTO.ShopListDTO> searchShops(
      @ModelAttribute ShopReqDTO.ShopSearchReqDTO request // 쿼리 스트링 자동 매핑
      ) {
    ShopResDTO.ShopListDTO shops = shopQueryService.searchShops(request);
    return ApiResponse.of(GeneralSuccessCode.OK, shops);
  }
}

package com.example.picknwhip_be.domain.shop.controller;

import com.example.picknwhip_be.domain.design.dto.res.DesignResDTO;
import com.example.picknwhip_be.domain.design.service.query.DesignQueryService;
import com.example.picknwhip_be.domain.review.dto.req.ReviewReqDTO;
import com.example.picknwhip_be.domain.review.dto.res.ReviewResDTO;
import com.example.picknwhip_be.domain.review.service.query.ReviewQueryService;
import com.example.picknwhip_be.domain.shop.dto.ShopResDTO;
import com.example.picknwhip_be.domain.shop.dto.res.ShopDetailResDTO;
import com.example.picknwhip_be.domain.shop.dto.res.ShopInfoResDTO;
import com.example.picknwhip_be.domain.shop.dto.res.ShopPreviewResDTO;
import com.example.picknwhip_be.domain.shop.service.command.ShopService;
import com.example.picknwhip_be.domain.shop.service.query.ShopQueryService;
import com.example.picknwhip_be.global.apiPayload.ApiResponse;
import com.example.picknwhip_be.global.apiPayload.annotation.ExtractPayload;
import com.example.picknwhip_be.global.apiPayload.code.GeneralSuccessCode;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/shops")
@RequiredArgsConstructor
@Validated
@Tag(name = "Shop API", description = "가게 관련 API")
public class ShopController {

  private final ShopService shopService;
  private final ShopQueryService shopQueryService;
  private final ReviewQueryService reviewQueryService;
  private final DesignQueryService designQueryService;

  @Operation(
      summary = "내 주변 가게 조회",
      description = "현재 위치(lat, lon)를 기준으로 특정 반경(radius) 내의 가게 목록을 조회합니다.")
  @GetMapping("/nearby")
  public ResponseEntity<List<ShopPreviewResDTO>> getNearbyShops(
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

  @Operation(
      summary = "가게 상세 조회",
      description = "가게 ID와 현재 위치(lat, lon)를 받아 가게 상세 정보(거리 포함)를 조회합니다.")
  @GetMapping("/{shopId}")
  public ResponseEntity<ShopDetailResDTO> getShopDetail(
      @PathVariable Long shopId,
      @Parameter(description = "현재 위치 위도", required = true) @RequestParam double lat,
      @Parameter(description = "현재 위치 경도", required = true) @RequestParam double lon) {
    return ResponseEntity.ok(shopService.getShopDetail(shopId, lat, lon));
  }

  @Operation(
      summary = "가게 리뷰 목록 조회 by 슝/하승연",
      description = "가게 리뷰를 최신순/도움순/별점높은순/별점낮은순으로 정렬하여 조회하는 기능입니다.")
  @GetMapping("/{shopId}/reviews")
  public ApiResponse<ReviewResDTO.ShopReviewListDTO> getShopReviewList(
      @PathVariable Long shopId,
      @ParameterObject @Valid @ModelAttribute ReviewReqDTO.ShopReviewListDTO dto,
      @Parameter(hidden = true) @ExtractPayload Long userId) {
    return ApiResponse.of(
        GeneralSuccessCode.OK, reviewQueryService.searchShopReviews(shopId, dto, userId));
  }

  @Operation(
      summary = "가게 리뷰 통계 조회 by 슝/하승연",
      description = "가게 리뷰 상단에서 평균 별점과 리뷰 개수, 키워드별 순위를 조회하는 기능입니다.")
  @GetMapping("/{shopId}/reviews/summary")
  public ApiResponse<ReviewResDTO.ShopReviewSummaryDTO> getShopReviewSummary(
      @PathVariable Long shopId) {
    return ApiResponse.of(
        GeneralSuccessCode.OK, reviewQueryService.searchShopReviewSummary(shopId));
  }

  @Operation(
      summary = "가게 리뷰 필터 디자인 목록 조회 by 슝/하승연",
      description = "가게 리뷰 필터에서 디자인 갤러리의 디자인 명단을 조회하는 기능입니다.")
  @GetMapping("/{shopId}/designs")
  public ApiResponse<DesignResDTO.DesignListDTO> getShopDesignNameList(@PathVariable Long shopId) {
    return ApiResponse.of(GeneralSuccessCode.OK, designQueryService.findShopDesignNameList(shopId));
  }

    @Operation(
            summary = "가게 매장 정보 탭 조회",
            description = "가게 상세 페이지의 '매장 정보' 탭 데이터(가격/사이즈/픽업/결제/이벤트)를 조회합니다."
    )
    @GetMapping("/{shopId}/info")
    public ApiResponse<ShopInfoResDTO> getShopInfoTab(@PathVariable Long shopId) {
        return ApiResponse.of(GeneralSuccessCode.OK, shopService.getShopInfoTab(shopId));
    }
}

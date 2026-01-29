package com.example.picknwhip_be.domain.favorite.controller;

import com.example.picknwhip_be.domain.favorite.dto.res.FavoriteShopListResponse;
import com.example.picknwhip_be.domain.favorite.dto.res.FavoriteShopResponse;
import com.example.picknwhip_be.domain.favorite.exception.code.FavoriteShopSuccessCode;
import com.example.picknwhip_be.domain.favorite.service.FavoriteShopService;
import com.example.picknwhip_be.global.apiPayload.ApiResponse;
import com.example.picknwhip_be.global.apiPayload.annotation.ExtractPayload;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/shops")
@RequiredArgsConstructor
@Tag(name = "Favorite Shop", description = "마이픽(가게 찜) 관련 API")
public class FavoriteShopController {

  private final FavoriteShopService favoriteShopService;

  // TODO: shopId 토큰 필요

  @Operation(summary = "마이픽 가게 등록", description = "특정 가게를 마이픽(즐겨찾기)에 추가합니다.")
  @PostMapping("/{shopId}/favorite")
  public ApiResponse<FavoriteShopResponse> addFavoriteShop(
      @PathVariable Long shopId, @ExtractPayload Long userId) {
    FavoriteShopResponse result = favoriteShopService.addFavoriteShop(userId, shopId);

    return ApiResponse.of(FavoriteShopSuccessCode.FAVORITE_CREATED, result);
  }

  @Operation(summary = "마이픽 가게 취소", description = "마이픽에 등록된 가게를 삭제합니다.")
  @DeleteMapping("/{shopId}/favorite")
  public ApiResponse<FavoriteShopResponse> removeFavoriteShop(
      @PathVariable Long shopId, @ExtractPayload Long userId) {
    FavoriteShopResponse result = favoriteShopService.removeFavoriteShop(userId, shopId);

    return ApiResponse.of(FavoriteShopSuccessCode.FAVORITE_DELETED, result);
  }

  @Operation(summary = "마이픽 가게 목록 조회", description = "커서 페이징을 적용하여 찜한 가게 목록을 조회합니다.")
  @GetMapping("/favorite")
  public ApiResponse<FavoriteShopListResponse> getMyPickShops(
      @Parameter(description = "사용자 ID (토큰 적용 전 임시)", required = true) @RequestParam Long userId,
      @Parameter(description = "커서 ID (이전 페이지 마지막 favoriteId, 첫 페이지는 null)", required = false)
          @RequestParam(required = false)
          Long cursor,
      @Parameter(description = "한 페이지에 가져올 개수 (기본 10)", example = "10")
          @RequestParam(defaultValue = "10")
          Integer limit) {
    FavoriteShopListResponse result = favoriteShopService.getMyPickShops(userId, cursor, limit);
    return ApiResponse.of(FavoriteShopSuccessCode.FAVORITE_LIST_FETCHED, result);
  }
}

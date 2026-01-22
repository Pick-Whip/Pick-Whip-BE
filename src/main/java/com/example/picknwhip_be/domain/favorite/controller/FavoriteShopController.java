package com.example.picknwhip_be.domain.favorite.controller;

import com.example.picknwhip_be.domain.favorite.dto.res.FavoriteShopResponse;
import com.example.picknwhip_be.domain.favorite.exception.code.FavoriteShopSuccessCode;
import com.example.picknwhip_be.domain.favorite.service.FavoriteShopService;
import com.example.picknwhip_be.global.apiPayload.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/shops")
@RequiredArgsConstructor
@Tag(name = "Favorite Shop", description = "마이픽(가게 찜) 관련 API")
public class FavoriteShopController {

  private final FavoriteShopService favoriteShopService;

  // TODO: 실제 구현 시 userId는 토큰(Security)에서 추출해야 합니다.
  // 테스트 편의를 위해 일단 @RequestParam으로 받거나, 하드코딩된 값(1L)을 사용한다고 가정합니다.

  @Operation(summary = "마이픽 가게 등록", description = "특정 가게를 마이픽(즐겨찾기)에 추가합니다.")
  @PostMapping("/{shopId}/favorite")
  public ApiResponse<FavoriteShopResponse> addFavoriteShop(
      @PathVariable Long shopId, @RequestParam Long userId) {
    FavoriteShopResponse result = favoriteShopService.addFavoriteShop(userId, shopId);

    return ApiResponse.of(FavoriteShopSuccessCode.FAVORITE_CREATED, result);
  }

  @Operation(summary = "마이픽 가게 취소", description = "마이픽에 등록된 가게를 삭제합니다.")
  @DeleteMapping("/{shopId}/favorite")
  public ApiResponse<FavoriteShopResponse> removeFavoriteShop(
      @PathVariable Long shopId, @RequestParam Long userId) {
    FavoriteShopResponse result = favoriteShopService.removeFavoriteShop(userId, shopId);

    return ApiResponse.of(FavoriteShopSuccessCode.FAVORITE_DELETED, result);
  }
}

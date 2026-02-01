package com.example.picknwhip_be.domain.favorite.dto.res;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "가게 찜 등록/취소 응답")
public record FavoriteShopResponseDTO(
    @Schema(description = "가게 ID", example = "12") Long shopId,
    @Schema(description = "현재 찜 상태", example = "true") boolean isFavorited) {}

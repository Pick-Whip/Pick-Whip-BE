package com.example.picknwhip_be.domain.favorite.dto.res;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class FavoriteShopDto {
  private Long favoriteId; // 찜 ID
  private Long shopId; // 가게 ID
  private String shopName;
  private String shopImageUrl;
  private Double averageRating;
  private Integer minPrice;
}

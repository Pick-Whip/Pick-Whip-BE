package com.example.picknwhip_be.domain.favorite.converter;

import com.example.picknwhip_be.domain.favorite.dto.res.FavoriteShopResponse;

public class FavoriteShopConverter {

  private FavoriteShopConverter() {}

  public static FavoriteShopResponse toResponse(Long shopId, boolean isFavorited) {
    return new FavoriteShopResponse(shopId, isFavorited);
  }
}

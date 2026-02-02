package com.example.picknwhip_be.domain.favorite.converter;

import com.example.picknwhip_be.domain.favorite.dto.res.FavoriteShopDTO;
import com.example.picknwhip_be.domain.favorite.dto.res.FavoriteShopListResDTO;
import com.example.picknwhip_be.domain.favorite.dto.res.FavoriteShopResDTO;
import com.example.picknwhip_be.domain.favorite.entity.FavoriteShop;
import java.util.List;

public class FavoriteShopConverter {

  private FavoriteShopConverter() {}

  public static FavoriteShopResDTO toResponse(Long shopId, boolean isFavorited) {
    return new FavoriteShopResDTO(shopId, isFavorited);
  }

  public static FavoriteShopDTO toDto(FavoriteShop favoriteShop) {
    return FavoriteShopDTO.builder()
        .favoriteId(favoriteShop.getId())
        .shopId(favoriteShop.getShop().getId())
        .shopName(favoriteShop.getShop().getShopName())
        .shopImageUrl(favoriteShop.getShop().getShopImageUrl())
        .averageRating(
            favoriteShop.getShop().getAverageRating() != null
                ? favoriteShop.getShop().getAverageRating()
                : 0.0)
        .minPrice(
            favoriteShop.getShop().getMinPrice() != null ? favoriteShop.getShop().getMinPrice() : 0)
        .build();
  }

  public static FavoriteShopListResDTO toListResponse(
      List<FavoriteShopDTO> shopList, Long nextCursor, boolean hasNext) {
    return FavoriteShopListResDTO.builder()
        .shopList(shopList)
        .nextCursor(nextCursor)
        .hasNext(hasNext)
        .build();
  }
}

package com.example.picknwhip_be.domain.favorite.converter;

import com.example.picknwhip_be.domain.favorite.dto.res.FavoriteShopDto;
import com.example.picknwhip_be.domain.favorite.dto.res.FavoriteShopListResponse;
import com.example.picknwhip_be.domain.favorite.dto.res.FavoriteShopResponse;
import com.example.picknwhip_be.domain.favorite.entity.FavoriteShop;
import java.util.List;

public class FavoriteShopConverter {

    private FavoriteShopConverter() {}

    public static FavoriteShopResponse toResponse(Long shopId, boolean isFavorited) {
        return new FavoriteShopResponse(shopId, isFavorited);
    }

    public static FavoriteShopDto toDto(FavoriteShop favoriteShop) {
        return FavoriteShopDto.builder()
                .favoriteId(favoriteShop.getId())
                .shopId(favoriteShop.getShop().getId())
                .shopName(favoriteShop.getShop().getShopName())
                .shopImageUrl(favoriteShop.getShop().getShopImageUrl())
                .averageRating(favoriteShop.getShop().getAverageRating() != null ? favoriteShop.getShop().getAverageRating() : 0.0)
                .minPrice(favoriteShop.getShop().getMinPrice() != null ? favoriteShop.getShop().getMinPrice() : 0)
                .build();
    }

    public static FavoriteShopListResponse toListResponse(List<FavoriteShopDto> shopList, Long nextCursor, boolean hasNext) {
        return FavoriteShopListResponse.builder()
                .shopList(shopList)
                .nextCursor(nextCursor)
                .hasNext(hasNext)
                .build();
    }
}
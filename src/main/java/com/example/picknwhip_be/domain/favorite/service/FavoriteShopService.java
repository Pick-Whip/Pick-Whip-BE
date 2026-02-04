package com.example.picknwhip_be.domain.favorite.service;

import com.example.picknwhip_be.domain.favorite.dto.res.FavoriteShopListResDTO;
import com.example.picknwhip_be.domain.favorite.dto.res.FavoriteShopResDTO;

public interface FavoriteShopService {
  FavoriteShopResDTO addFavoriteShop(Long userId, Long shopId);

  FavoriteShopResDTO removeFavoriteShop(Long userId, Long shopId);

  FavoriteShopListResDTO getMyPickShops(Long userId, Long cursor, int limit);
}

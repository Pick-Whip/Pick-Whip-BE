package com.example.picknwhip_be.domain.shop.service.query;

import com.example.picknwhip_be.domain.favorite.repository.FavoriteShopRepository;
import com.example.picknwhip_be.domain.shop.converter.ShopConverter;
import com.example.picknwhip_be.domain.shop.dto.ShopResDTO;
import com.example.picknwhip_be.domain.shop.dto.req.ShopReqDTO;
import com.example.picknwhip_be.domain.shop.entity.Shop;
import com.example.picknwhip_be.domain.shop.repository.ShopRepository;
import java.util.List;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class ShopQueryService {

  private final ShopRepository shopRepository;
  private final FavoriteShopRepository favoriteShopRepository;
  private final ShopConverter shopConverter;

  public ShopResDTO.ShopInMapListDTO findShopInMap(
      Double lowLat, Double highLat, Double lowLon, Double highLon, Long userId) {

    List<Shop> shops = shopRepository.findShopsInBoundary(lowLat, highLat, lowLon, highLon);

    Set<Long> pickedShopIds =
        (userId != null)
            ? favoriteShopRepository.findShopIdsByUserId(userId)
            : java.util.Collections.emptySet();

    return shopConverter.toShopInMapListDTO(shops, pickedShopIds);
  }

  public Page<ShopResDTO.ShopSearchResDTO> searchShops(
      ShopReqDTO.ShopSearchCondition condition, Pageable pageable) {
    Page<Shop> shopPage = shopRepository.searchShops(condition, pageable);

    return shopPage.map(ShopResDTO.ShopSearchResDTO::from);
  }
}

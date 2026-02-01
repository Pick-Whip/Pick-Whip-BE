package com.example.picknwhip_be.domain.shop.service;

import com.example.picknwhip_be.domain.shop.dto.ShopResDTO;
import com.example.picknwhip_be.domain.shop.entity.Shop;
import com.example.picknwhip_be.domain.shop.repository.ShopRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class ShopQueryService {

  private final ShopRepository shopRepository;

  public ShopResDTO.ShopInMapListDTO findShopInMap(
      Double lowLat, Double highLat, Double lowLon, Double highLon) {

    List<Shop> shop = shopRepository.findShopsInBoundary(lowLat, highLat, lowLon, highLon);

    return null;
  }
}

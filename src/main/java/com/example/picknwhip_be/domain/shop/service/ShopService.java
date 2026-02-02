package com.example.picknwhip_be.domain.shop.service;

import com.example.picknwhip_be.domain.shop.dto.res.ShopDetailResDTO;
import com.example.picknwhip_be.domain.shop.dto.res.ShopPreviewResDTO;
import java.util.List;

public interface ShopService {
  // 주변 가게 조회
  List<ShopPreviewResDTO> getNearbyShops(double lat, double lon, double radius);

  // 가게 상세 조회
  ShopDetailResDTO getShopDetail(Long shopId, double lat, double lon);
}

package com.example.picknwhip_be.domain.shop.service;

import com.example.picknwhip_be.domain.shop.dto.res.ShopDetailResponseDTO;
import com.example.picknwhip_be.domain.shop.dto.res.ShopPreviewResponseDTO;
import java.util.List;

public interface ShopService {
    // 주변 가게 조회
    List<ShopPreviewResponseDTO> getNearbyShops(double lat, double lon, double radius);
    // 가게 상세 조회
    ShopDetailResponseDTO getShopDetail(Long shopId, double lat, double lon);
}
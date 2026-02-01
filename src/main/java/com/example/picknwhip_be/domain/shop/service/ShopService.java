package com.example.picknwhip_be.domain.shop.service;

import com.example.picknwhip_be.domain.shop.dto.res.ShopPreviewResponseDto;
import java.util.List;

public interface ShopService {
    // 주변 가게 조회
    List<ShopPreviewResponseDto> getNearbyShops(double lat, double lon, double radius);
}
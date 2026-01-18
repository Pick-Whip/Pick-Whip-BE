package com.example.picknwhip_be.domain.shop.service;

import com.example.picknwhip_be.domain.shop.converter.ShopConverter;
import com.example.picknwhip_be.domain.shop.dto.res.ShopPreviewResponseDto;
import com.example.picknwhip_be.domain.shop.repository.ShopRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ShopService {

    private final ShopRepository shopRepository;
    private final ShopConverter shopConverter;

    /**
     * 주변 가게 조회
     * @param lat 위도
     * @param lon 경도
     * @param radius 반경 (미터 단위)
     */
    public List<ShopPreviewResponseDto> getNearbyShops(double lat, double lon, double radius) {
        List<ShopRepository.ShopPreviewInfo> shops = shopRepository.findNearbyShops(lat, lon, radius);
        return shops.stream()
                .map(shopConverter::toPreviewDto)
                .collect(Collectors.toList());
    }
}
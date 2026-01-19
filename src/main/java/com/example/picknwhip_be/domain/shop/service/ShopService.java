package com.example.picknwhip_be.domain.shop.service;

import com.example.picknwhip_be.domain.shop.converter.ShopConverter;
import com.example.picknwhip_be.domain.shop.dto.res.ShopPreviewResponseDto;
import com.example.picknwhip_be.domain.shop.repository.ShopRepository;
import com.example.picknwhip_be.global.apiPayload.code.GeneralErrorCode;
import com.example.picknwhip_be.global.apiPayload.exception.GeneralException;
import com.example.picknwhip_be.global.apiPayload.util.GeoUtils;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ShopService {

  private static final double MAX_RADIUS_M = 3000.0;
  private static final int DEFAULT_LIMIT = 50;

  private final ShopRepository shopRepository;
  private final ShopConverter shopConverter;

  public List<ShopPreviewResponseDto> getNearbyShops(double lat, double lon, double radius) {

    // 좌표 검증
    if (lat < -90.0 || lat > 90.0 || lon < -180.0 || lon > 180.0) {
      throw new GeneralException(GeneralErrorCode.INVALID_COORDINATE);
    }

    // radius 검증 (3000 초과면 예외)
    if (radius < 0.0 || radius > MAX_RADIUS_M) {
      throw new GeneralException(GeneralErrorCode.INVALID_RADIUS);
    }

    GeoUtils.BoundingBox box = GeoUtils.normalize(GeoUtils.boundingBox(lat, lon, radius));

    try {
      return shopRepository
          .findNearbyShops(
              lat,
              lon,
              box.minLat(),
              box.maxLat(),
              box.minLon(),
              box.maxLon(),
              radius,
              DEFAULT_LIMIT)
          .stream()
          .map(shopConverter::toPreviewDto)
          .toList();
    } catch (Exception e) {
      throw new GeneralException(GeneralErrorCode.SHOP_NEARBY_QUERY_FAILED);
    }
  }
}

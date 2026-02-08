package com.example.picknwhip_be.domain.shop.service.command;

import com.example.picknwhip_be.domain.shop.converter.ShopConverter;
import com.example.picknwhip_be.domain.shop.dto.res.ShopDetailResDTO;
import com.example.picknwhip_be.domain.shop.dto.res.ShopPreviewResDTO;
import com.example.picknwhip_be.domain.shop.exception.ShopException;
import com.example.picknwhip_be.domain.shop.exception.code.ShopErrorCode;
import com.example.picknwhip_be.domain.shop.repository.ShopRepository;
import com.example.picknwhip_be.global.apiPayload.util.GeoUtils;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ShopServiceImpl implements ShopService {

  private static final double MAX_RADIUS_M = 3000.0;
  private static final int DEFAULT_LIMIT = 50;

  private final ShopRepository shopRepository;
  private final ShopConverter shopConverter;

  @Override
  public List<ShopPreviewResDTO> getNearbyShops(double lat, double lon, double radius) {
    validateCoordinate(lat, lon);

    if (radius < 0.0 || radius > MAX_RADIUS_M) {
      throw new ShopException(ShopErrorCode.INVALID_RADIUS);
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
      throw new ShopException(ShopErrorCode.SHOP_NEARBY_QUERY_FAILED);
    }
  }

  @Override
  public ShopDetailResDTO getShopDetail(Long shopId, double lat, double lon) {
    validateCoordinate(lat, lon);
    return shopRepository
        .findShopDetailById(shopId, lat, lon)
            .map(shopConverter::toDetailDto)
        .orElseThrow(() -> new ShopException(ShopErrorCode.SHOP_NOT_FOUND));
  }

  private void validateCoordinate(double lat, double lon) {
    if (lat < -90.0 || lat > 90.0 || lon < -180.0 || lon > 180.0) {
      throw new ShopException(ShopErrorCode.INVALID_COORDINATE);
    }
  }
}

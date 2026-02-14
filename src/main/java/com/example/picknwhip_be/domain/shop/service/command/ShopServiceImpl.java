package com.example.picknwhip_be.domain.shop.service.command;

import com.example.picknwhip_be.domain.shop.converter.ShopConverter;
import com.example.picknwhip_be.domain.shop.converter.ShopInfoConverter;
import com.example.picknwhip_be.domain.shop.dto.res.ShopDetailResDTO;
import com.example.picknwhip_be.domain.shop.dto.res.ShopInfoResDTO;
import com.example.picknwhip_be.domain.shop.dto.res.ShopPreviewResDTO;
import com.example.picknwhip_be.domain.shop.entity.Shop;
import com.example.picknwhip_be.domain.shop.entity.ShopBusinessHour;
import com.example.picknwhip_be.domain.shop.entity.ShopCakeSize;
import com.example.picknwhip_be.domain.shop.entity.ShopEvent;
import com.example.picknwhip_be.domain.shop.exception.ShopException;
import com.example.picknwhip_be.domain.shop.exception.code.ShopErrorCode;
import com.example.picknwhip_be.domain.shop.repository.*;
import com.example.picknwhip_be.global.apiPayload.util.GeoUtils;
import java.time.LocalDate;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ShopServiceImpl implements ShopService {

  private static final double MAX_RADIUS_M = 3000.0;
  private static final int DEFAULT_LIMIT = 50;

  private final ShopRepository shopRepository;
  private final ShopConverter shopConverter;
  private final ShopCakeSizeRepository shopCakeSizeRepository;
  private final ShopBusinessHourRepository shopBusinessHourRepository;
  private final ShopEventRepository shopEventRepository;
  private final BankAccountRepository bankAccountRepository;
  private final ShopInfoConverter shopInfoConverter;

  @Override
  public List<ShopPreviewResDTO> getNearbyShops(double lat, double lon, double radius) {
    validateCoordinate(lat, lon);

    if (radius < 0.0 || radius > MAX_RADIUS_M) {
      throw new ShopException(ShopErrorCode.INVALID_RADIUS);
    }

    GeoUtils.BoundingBox box = GeoUtils.normalize(GeoUtils.boundingBox(lat, lon, radius));
    log.debug(
        "BOX minLat={}, maxLat={}, minLon={}, maxLon={}",
        box.minLat(),
        box.maxLat(),
        box.minLon(),
        box.maxLon());
    try {
      return shopRepository
          .findNearbyShops(
              lon,
              lat,
              box.minLon(),
              box.minLat(),
              box.maxLat(),
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

  @Override
  public ShopInfoResDTO getShopInfoTab(Long shopId) {
    Shop shop =
        shopRepository
            .findById(shopId)
            .orElseThrow(() -> new ShopException(ShopErrorCode.SHOP_NOT_FOUND));

    List<ShopCakeSize> sizes = shopCakeSizeRepository.findByShopId(shopId);
    List<ShopBusinessHour> hours = shopBusinessHourRepository.findAllByShopId(shopId);
    boolean hasBankAccount = !bankAccountRepository.findByShopId(shopId).isEmpty();

    ShopEvent activeEvent =
        shopEventRepository.findFirstActiveEvent(shopId, LocalDate.now()).orElse(null);
    return shopInfoConverter.toInfoResDTO(shop, sizes, hours, hasBankAccount, activeEvent);
  }

  private void validateCoordinate(double lat, double lon) {
    if (lat < -90.0 || lat > 90.0 || lon < -180.0 || lon > 180.0) {
      throw new ShopException(ShopErrorCode.INVALID_COORDINATE);
    }
  }
}

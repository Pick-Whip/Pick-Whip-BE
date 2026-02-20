package com.example.picknwhip_be.domain.shop.converter;

import com.example.picknwhip_be.domain.S3.service.S3Service;
import com.example.picknwhip_be.domain.shop.dto.ShopResDTO;
import com.example.picknwhip_be.domain.shop.dto.res.ShopDetailResDTO;
import com.example.picknwhip_be.domain.shop.dto.res.ShopPreviewResDTO;
import com.example.picknwhip_be.domain.shop.entity.Shop;
import com.example.picknwhip_be.domain.shop.repository.ShopRepository;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ShopConverter {

  private final ObjectMapper objectMapper;
  private final S3Service s3Service;

  private List<String> parseTags(String rawTags) {
    if (rawTags == null || rawTags.isBlank()) {
      return Collections.emptyList();
    }
    try {
      List<String> tagList = objectMapper.readValue(rawTags, new TypeReference<List<String>>() {});
      return tagList.stream()
          .filter(Objects::nonNull)
          .map(String::trim)
          .filter(s -> !s.isBlank())
          .toList();
    } catch (Exception ignored) {
      return Collections.emptyList();
    }
  }

  private String toImageUrlOrPresigned(String raw) {
    if (raw == null || raw.isBlank()) {
      return raw;
    }

    // 이미 완성 URL(https://...)이면 그대로 반환 (시드/더미가 URL일 수 있음)
    if (raw.startsWith("http://") || raw.startsWith("https://")) {
      return raw;
    }

    // DB에 keyName 저장된 경우 presigned GET URL 생성
    return s3Service.createPresignedDownloadUrl(raw);
  }

  public ShopPreviewResDTO toPreviewDto(ShopRepository.ShopPreviewInfo info) {
    Double distanceInKm = (info.getDistance() != null) ? info.getDistance() / 1000.0 : 0.0;

    return ShopPreviewResDTO.builder()
        .shopId(info.getShopId())
        .shopName(info.getShopName())
        .shopImageUrl(toImageUrlOrPresigned(info.getShopImageUrl()))
        .averageRating(info.getAverageRating())
        .minPrice(info.getMinPrice())
        .maxPrice(info.getMaxPrice())
        .distance(distanceInKm)
        .lat(info.getLat())
        .lon(info.getLon())
        .tags(parseTags(info.getTags()))
        .build();
  }

  public ShopDetailResDTO toDetailDto(ShopRepository.ShopDetailInfo info, boolean isMyPick) {

    Double distanceKm = (info.getDistanceKm() != null) ? info.getDistanceKm() : 0.0;

    return ShopDetailResDTO.builder()
        .shopId(info.getShopId())
        .shopName(info.getShopName())
        .shopImageUrl(toImageUrlOrPresigned(info.getShopImageUrl()))
        .averageRating(info.getAverageRating())
        .reviewCount(info.getReviewCount())
        .distanceKm(distanceKm)
        .address(info.getAddress())
        .lat(info.getLat())
        .lon(info.getLon())
        .phone(info.getPhone())
        .keywords(parseTags(info.getKeywords()))
        .isMyPick(isMyPick)
        .build();
  }

  public ShopResDTO.ShopInMapListDTO toShopInMapListDTO(List<Shop> shop, Set<Long> pickedShopIds) {
    return ShopResDTO.ShopInMapListDTO.builder()
        .shops(
            shop.stream()
                .map(
                    item ->
                        ShopResDTO.ShopInMap.builder()
                            .shopId(item.getId())
                            .shopName(item.getShopName())
                            .latitude(item.getLocation().getY())
                            .longitude(item.getLocation().getX())
                            .isPicked(pickedShopIds.contains(item.getId()))
                            .build())
                .toList())
        .build();
  }
}

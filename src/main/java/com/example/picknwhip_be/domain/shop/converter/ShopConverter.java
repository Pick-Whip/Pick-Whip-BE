package com.example.picknwhip_be.domain.shop.converter;

import com.example.picknwhip_be.domain.shop.dto.ShopResDTO;
import com.example.picknwhip_be.domain.shop.dto.res.ShopDetailResponseDTO;
import com.example.picknwhip_be.domain.shop.dto.res.ShopPreviewResponseDTO;
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

  public ShopPreviewResponseDTO toPreviewDto(ShopRepository.ShopPreviewInfo info) {
    return ShopPreviewResponseDTO.builder()
        .shopId(info.getShopId())
        .shopName(info.getShopName())
        .shopImageUrl(info.getShopImageUrl())
        .averageRating(info.getAverageRating())
        .minPrice(info.getMinPrice())
        .distance(info.getDistance())
        .tags(parseTags(info.getTags()))
        .build();
  }

  public ShopDetailResponseDTO toDetailDto(ShopRepository.ShopDetailInfo info) {
    Double distanceInKm = (info.getDistance() != null) ? info.getDistance() / 1000.0 : 0.0;

    return ShopDetailResponseDTO.builder()
        .shopId(info.getShopId())
        .shopName(info.getShopName())
        .shopImageUrl(info.getShopImageUrl())
        .averageRating(info.getAverageRating())
        .reviewCount(info.getReviewCount())
        .distance(distanceInKm)
        .address(info.getAddress())
        .phone(info.getPhone())
        .keywords(parseTags(info.getKeywords()))
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

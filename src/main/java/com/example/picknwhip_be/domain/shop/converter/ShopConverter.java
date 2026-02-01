package com.example.picknwhip_be.domain.shop.converter;

import com.example.picknwhip_be.domain.shop.dto.ShopResDTO;
import com.example.picknwhip_be.domain.shop.dto.res.ShopPreviewResponseDto;
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

  public ShopPreviewResponseDto toPreviewDto(ShopRepository.ShopPreviewInfo info) {

    List<String> tagList = Collections.emptyList();
    String rawTags = info.getTags();

    if (rawTags != null && !rawTags.isBlank()) {
      try {
        tagList = objectMapper.readValue(rawTags, new TypeReference<List<String>>() {});
        tagList =
            tagList.stream()
                .filter(Objects::nonNull)
                .map(String::trim)
                .filter(s -> !s.isBlank())
                .toList();
      } catch (Exception ignored) {
        tagList = Collections.emptyList();
      }
    }

    return ShopPreviewResponseDto.builder()
        .shopId(info.getShopId())
        .shopName(info.getShopName())
        .shopImageUrl(info.getShopImageUrl())
        .averageRating(info.getAverageRating())
        .minPrice(info.getMinPrice())
        .distance(info.getDistance())
        .tags(tagList)
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

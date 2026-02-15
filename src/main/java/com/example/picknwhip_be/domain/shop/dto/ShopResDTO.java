package com.example.picknwhip_be.domain.shop.dto;

import com.example.picknwhip_be.domain.shop.entity.Shop;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class ShopResDTO {

  @Builder
  @Getter
  @NoArgsConstructor
  @AllArgsConstructor
  public static class ShopInMapListDTO {
    private List<ShopInMap> shops;
  }

  @Builder
  @Getter
  @NoArgsConstructor
  @AllArgsConstructor
  public static class ShopInMap {
    private Long shopId;
    private String shopName;
    private Double latitude;
    private Double longitude;

    @JsonProperty("isPicked")
    private boolean isPicked;
  }

  @Getter
  @Builder
  @NoArgsConstructor
  @AllArgsConstructor
  public static class ShopSearchResDTO {

    private Long shopId;
    private String shopName;
    private String shopImageUrl;
    private Double averageRating;
    private String address;
    private Double longitude;
    private Double latitude;

    private Integer minPrice;
    private Integer maxPrice;

    private List<String> keywords;

    // 추후 converter로 뺄 예정
    public static ShopSearchResDTO from(Shop shop) {
      return ShopSearchResDTO.builder()
          .shopId(shop.getId())
          .shopName(shop.getShopName())
          .shopImageUrl(shop.getShopImageUrl())
          .averageRating(shop.getAverageRating() != null ? shop.getAverageRating() : 0.0)
          .address(shop.getAddress())
          .longitude(shop.getLocation().getX())
          .latitude(shop.getLocation().getY())
          .minPrice(shop.getMinPrice())
          .maxPrice(shop.getMaxPrice())
          // ShopKeywordMapping 엔티티에서 태그 이름만 추출
          .keywords(
              shop.getKeywordMappings().stream()
                  .map(mapping -> mapping.getKeyword().getKeywordText())
                  .collect(Collectors.toList()))
          .build();
    }
  }
}

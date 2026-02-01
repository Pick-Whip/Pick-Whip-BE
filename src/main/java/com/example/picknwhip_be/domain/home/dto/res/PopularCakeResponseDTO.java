package com.example.picknwhip_be.domain.home.dto.res;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class PopularCakeResponseDTO {
  private Integer rank;

  private Long designId;
  private String cakeName;
  private String cakeImageUrl;

  private Long shopId;
  private String shopName;
  private Double averageRating;
  private Integer minPrice;

  private boolean isMyPick;

  private Long orderCount;
}

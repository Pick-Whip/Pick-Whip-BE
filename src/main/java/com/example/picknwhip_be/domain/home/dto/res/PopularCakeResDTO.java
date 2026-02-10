package com.example.picknwhip_be.domain.home.dto.res;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PopularCakeResDTO {
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

  public PopularCakeResDTO(
      Integer rank,
      Long designId,
      String cakeName,
      String cakeImageUrl,
      Long shopId,
      String shopName,
      Double averageRating,
      Integer minPrice,
      Long orderCount) {
    this.rank = rank;
    this.designId = designId;
    this.cakeName = cakeName;
    this.cakeImageUrl = cakeImageUrl;
    this.shopId = shopId;
    this.shopName = shopName;
    this.averageRating = averageRating;
    this.minPrice = minPrice;
    this.orderCount = orderCount;
    this.isMyPick = false;
  }
}

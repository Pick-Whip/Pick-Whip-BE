package com.example.picknwhip_be.domain.shop.dto;

import java.util.List;
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
    private boolean isPicked;
  }
}

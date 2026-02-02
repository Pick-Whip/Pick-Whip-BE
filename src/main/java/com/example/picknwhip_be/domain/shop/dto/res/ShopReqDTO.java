package com.example.picknwhip_be.domain.shop.dto.res;

import java.util.List;
import lombok.Getter;
import lombok.Setter;

public class ShopReqDTO {

  @Getter
  @Setter
  public class ShopSearchReqDTO {
    private String keyword;
    private String region;
    private List<String> styles;
    private Integer minPrice;
    private Integer maxPrice;
    private Double lat;
    private Double lon;
  }
}

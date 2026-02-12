package com.example.picknwhip_be.domain.shop.dto.req;

import com.example.picknwhip_be.domain.design.enums.Purpose;
import com.example.picknwhip_be.domain.design.enums.Style;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

public class ShopReqDTO {

  @Data
  @NoArgsConstructor
  @AllArgsConstructor
  public static class ShopSearchCondition {
    private String keyword;
    private String city;
    private List<String> subAreas;
    private List<Style> styles;
    private List<Purpose> purposes;
    private Integer minPrice;
    private Integer maxPrice;
  }
}

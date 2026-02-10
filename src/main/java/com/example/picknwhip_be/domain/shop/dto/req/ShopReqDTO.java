package com.example.picknwhip_be.domain.shop.dto.req;

import com.example.picknwhip_be.domain.design.enums.Style;
import java.util.List;
import lombok.Data;

public class ShopReqDTO {

  @Data
  public class ShopSearchCondition {
    private String keyword;
    private List<Style> styles;
    private List<String> purposes;
    private Integer minPrice;
    private Integer maxPrice;
  }
}

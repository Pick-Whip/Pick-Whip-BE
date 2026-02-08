package com.example.picknwhip_be.domain.shop.dto.res;

import java.util.List;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ShopDetailResDTO {
  private Long shopId;
  private String shopName;
  private String shopImageUrl;
  private Double averageRating;
  private Integer reviewCount;
  private Double distance;
  private String address;
  private String phone;
  private List<String> keywords;
    private Double lat;
    private Double lon;
}

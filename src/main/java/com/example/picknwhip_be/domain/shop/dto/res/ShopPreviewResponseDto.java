package com.example.picknwhip_be.domain.shop.dto.res;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class ShopPreviewResponseDto {
    private Long shopId;
    private String shopName;
    private String shopImageUrl;
    private Double averageRating;
    private Integer reviewCount;
    private Integer minPrice;
    private Integer distance;
    private List<String> tags;
}

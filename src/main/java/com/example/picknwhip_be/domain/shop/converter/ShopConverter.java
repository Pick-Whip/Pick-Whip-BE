package com.example.picknwhip_be.domain.shop.converter;

import com.example.picknwhip_be.domain.shop.dto.res.ShopPreviewResponseDto;
import com.example.picknwhip_be.domain.shop.repository.ShopRepository;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Component
public class ShopConverter {

    public ShopPreviewResponseDto toPreviewDto(ShopRepository.ShopPreviewInfo info) {
        // GROUP_CONCAT으로 가져온 "태그1,태그2" 문자열을 리스트로 변환
        List<String> tagList = Collections.emptyList();
        if (info.getTags() != null && !info.getTags().isBlank()) {
            tagList = Arrays.asList(info.getTags().split(","));
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
}
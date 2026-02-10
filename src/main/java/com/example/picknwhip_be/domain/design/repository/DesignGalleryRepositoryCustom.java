package com.example.picknwhip_be.domain.design.repository;

import com.example.picknwhip_be.domain.design.dto.res.DesignResDTO;
import com.example.picknwhip_be.domain.design.enums.Style;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface DesignGalleryRepositoryCustom {
  List<DesignResDTO.DesignNameDTO> fetchDesignNamesByShopId(Long shopId);

    Page<DesignResDTO.GalleryItemDTO> searchGallery(
            List<Style> categories,
            String sortType,
            String district, // 행정구역(구) 필터
            Double lat,
            Double lon,
            Long userId,
            Pageable pageable
    );
}

package com.example.picknwhip_be.domain.design.converter;

import com.example.picknwhip_be.domain.design.dto.DesignResDTO;
import com.example.picknwhip_be.domain.design.entity.DesignGallery;
import java.util.List;

public class DesignConverter {

  public static DesignResDTO.DesignPreviewDTO toDesignPreviewDTO(DesignGallery designGallery) {
    return DesignResDTO.DesignPreviewDTO.builder()
        .cakeName(designGallery.getDesignName())
        .price(designGallery.getBasePrice())
        .imageUrl(designGallery.getImageUrl())
        .keywords(designGallery.getKeywords())
        .build();
  }

  public static DesignResDTO.GetDesignListDTO toDesignListDTO(
      List<DesignGallery> designGalleryList) {

    List<DesignResDTO.DesignPreviewDTO> designDTOs =
        designGalleryList.stream().map(DesignConverter::toDesignPreviewDTO).toList();

    return DesignResDTO.GetDesignListDTO.builder().designs(designDTOs).build();
  }
}

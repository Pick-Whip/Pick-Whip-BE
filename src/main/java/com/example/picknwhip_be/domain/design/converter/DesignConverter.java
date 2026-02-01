package com.example.picknwhip_be.domain.design.converter;

import com.example.picknwhip_be.domain.custom.dto.CustomResDTO;
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

  public static DesignResDTO.GetDesignDetailDTO toDesignDetailDTO(DesignGallery design) {
    return DesignResDTO.GetDesignDetailDTO.builder()
        .cakeName(design.getDesignName())
        .cakeSize(design.getShopCakeSize().getSizeName())
        .price(design.getBasePrice())
        .imageUrl(design.getImageUrl())
        .allergyInfo(design.getAllergyInfo())
        .description(design.getDescription())
        .letteringText(design.getLetteringText())
        .letteringAlignment(design.getLetteringAlignment())
        .letteringLineCount(design.getLetteringLineCount())
        .keywords(design.getKeywords())
        .toppings(
            design.getOptions().stream()
                .filter(item -> item.getPositionX() != null)
                .map(
                    item ->
                        CustomResDTO.Topping.builder()
                            .optionId(item.getCustomOption().getId())
                            .name(item.getCustomOption().getOptionName())
                            .x(item.getPositionX())
                            .y(item.getPositionY())
                            .build())
                .toList())
        .options(
            design.getOptions().stream()
                .filter(item -> item.getPositionX() == null)
                .map(
                    item ->
                        CustomResDTO.Option.builder()
                            .optionId(item.getCustomOption().getId())
                            .name(item.getCustomOption().getOptionName())
                            .category(item.getCustomOption().getCategory())
                            .build())
                .toList())
        .build();
  }
}

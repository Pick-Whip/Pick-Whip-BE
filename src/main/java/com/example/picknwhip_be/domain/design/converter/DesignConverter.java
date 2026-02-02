package com.example.picknwhip_be.domain.design.converter;

import com.example.picknwhip_be.domain.custom.converter.CustomConverter;
import com.example.picknwhip_be.domain.custom.dto.CustomResDTO;
import com.example.picknwhip_be.domain.design.dto.DesignResDTO;
import com.example.picknwhip_be.domain.design.entity.DesignGallery;
import com.example.picknwhip_be.domain.design.entity.mapping.DesignOption;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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

    List<DesignOption> rawOptions = design.getOptions() != null ? design.getOptions() : List.of();

    Map<Boolean, List<DesignOption>> partitionedOptions =
        rawOptions.stream().collect(Collectors.partitioningBy(item -> item.getPositionX() != null));

    List<CustomResDTO.Topping> toppings =
        partitionedOptions.get(true).stream()
            .map(
                item ->
                    CustomConverter.createToppingDTO(
                        item.getCustomOption(), item.getPositionX(), item.getPositionY()))
            .toList();

    List<CustomResDTO.Option> options =
        partitionedOptions.get(false).stream()
            .map(item -> CustomConverter.createOptionDTO(item.getCustomOption()))
            .toList();

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
        .toppings(toppings)
        .options(options)
        .build();
  }
}

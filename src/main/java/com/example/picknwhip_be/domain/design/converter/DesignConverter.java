package com.example.picknwhip_be.domain.design.converter;

import com.example.picknwhip_be.domain.custom.converter.CustomConverter;
import com.example.picknwhip_be.domain.custom.dto.res.CustomResDTO;
import com.example.picknwhip_be.domain.design.dto.res.DesignResDTO;
import com.example.picknwhip_be.domain.design.entity.DesignGallery;
import com.example.picknwhip_be.domain.design.entity.mapping.DesignOption;
import com.example.picknwhip_be.domain.design.enums.Style;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class DesignConverter {

  public static DesignResDTO.DesignPreviewDTO toDesignPreviewDTO(DesignGallery designGallery) {
    return DesignResDTO.DesignPreviewDTO.builder()
        .designId(designGallery.getId())
        .cakeName(designGallery.getDesignName())
        .price(designGallery.getBasePrice())
        .imageUrl(designGallery.getImageUrl())
        .keywords(designGallery.getKeywords().stream().map(Style::getLabel).toList())
        .build();
  }

  public static DesignResDTO.GetDesignListDTO toDesignListDTO(
      List<DesignGallery> designGalleryList) {

    List<DesignResDTO.DesignPreviewDTO> designDTOs =
        designGalleryList.stream().map(DesignConverter::toDesignPreviewDTO).toList();

    return DesignResDTO.GetDesignListDTO.builder().designs(designDTOs).build();
  }

  public static DesignResDTO.GetDesignDetailDTO toDesignDetailDTO(DesignGallery design) {

    Collection<DesignOption> rawOptions =
        design.getOptions() != null ? design.getOptions() : List.of();

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
        .letteringColor(design.getLetteringColor())
        .availOptions(
            design.getAvailOptions().stream()
                .map(
                    avail ->
                        DesignResDTO.AvailOptionDTO.builder()
                            .category(avail.getCustomOption().getCategory())
                            .name(avail.getCustomOption().getOptionName())
                            .price(avail.getCustomOption().getAdditionalPrice())
                            .colorCode(avail.getCustomOption().getColorRgbCode())
                            .build())
                .toList())
        .keywords(design.getKeywords().stream().map(Style::getLabel).toList())
        .toppings(toppings)
        .options(options)
        .build();
  }

  public static DesignResDTO.DesignListDTO toShopDesignNameListDTO(
      List<DesignResDTO.DesignNameDTO> items) {
    return DesignResDTO.DesignListDTO.builder().items(items).build();
  }
}

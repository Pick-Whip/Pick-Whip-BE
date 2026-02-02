package com.example.picknwhip_be.domain.custom.converter;

import com.example.picknwhip_be.domain.custom.dto.CustomResDTO;
import com.example.picknwhip_be.domain.custom.entity.OrderDraft;
import com.example.picknwhip_be.domain.custom.entity.OrderDraftItem;
import com.example.picknwhip_be.domain.shop.entity.CustomOption;
import com.example.picknwhip_be.domain.shop.entity.ShopCakeSize;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


public class CustomConverter {

  public static CustomResDTO.GetDraftDetailDTO toDraftDetailDTO(OrderDraft draft) {

    List<OrderDraftItem> rawOptions = draft.getItems() != null ? draft.getItems() : List.of();

    Map<Boolean, List<OrderDraftItem>> partitionedOptions =
        rawOptions.stream().collect(Collectors.partitioningBy(item -> item.getPositionX() != null));

    List<CustomResDTO.Topping> toppings =
        partitionedOptions.get(true).stream()
            .map(
                item ->
                    createToppingDTO(
                        item.getCustomOption(), item.getPositionX(), item.getPositionY()))
            .toList();

    List<CustomResDTO.Option> options =
        partitionedOptions.get(false).stream()
            .map(item -> createOptionDTO(item.getCustomOption()))
            .toList();

    return CustomResDTO.GetDraftDetailDTO.builder()
        .draftId(draft.getId())
        .shopCakeSizeId(draft.getShopCakeSize().getId())
        .pickupDatetime(draft.getPickupDatetime())
        .letteringText(draft.getLetteringText())
        .letteringLineCount(draft.getLetteringLineCount())
        .letteringAlignment(draft.getLetteringAlignment())
        .additionalRequest(draft.getAdditionalRequest())
        .referenceImageUrl(draft.getReferenceImageUrl())
        .options(options)
        .toppings(toppings)
        .build();
  }

  public static CustomResDTO.GetDesignOptionDTO toDesignOptionDTO(
      Long shopId, List<ShopCakeSize> shopCakeSizes, List<CustomOption> customOptions) {
    return CustomResDTO.GetDesignOptionDTO.builder()
        .shopId(shopId)
        .cakeSizes(shopCakeSizes.stream().map(ShopCakeSize::getSizeName).toList())
        .customOptions(
            customOptions.stream()
                .map(
                    item ->
                        CustomResDTO.Option.builder()
                            .optionId(item.getId())
                            .category(item.getCategory())
                            .optionName(item.getOptionName())
                            .additionalPrice(item.getAdditionalPrice())
                            .colorRgbCode(item.getColorRgbCode())
                            .build())
                .toList())
        .build();
  }


  public static CustomResDTO.Topping createToppingDTO(CustomOption option, Double x, Double y) {
    return CustomResDTO.Topping.builder()
        .optionId(option.getId())
        .name(option.getOptionName())
        .additionalPrice(option.getAdditionalPrice())
        .colorRgbCode(option.getColorRgbCode())
        .x(x)
        .y(y)
        .build();
  }

  public static CustomResDTO.Option createOptionDTO(CustomOption option) {
    return CustomResDTO.Option.builder()
        .optionId(option.getId())
        .optionName(option.getOptionName())
        .category(option.getCategory())
        .additionalPrice(option.getAdditionalPrice())
        .colorRgbCode(option.getColorRgbCode())
  public static CustomResDTO.GetDesignOptionDTO toDesignOptionDTO(
      Long shopId, List<ShopCakeSize> shopCakeSizes, List<CustomOption> customOptions) {
    return CustomResDTO.GetDesignOptionDTO.builder()
        .shopId(shopId)
        .cakeSizes(shopCakeSizes.stream().map(ShopCakeSize::getSizeName).toList())
        .customOptions(
            customOptions.stream()
                .map(
                    item ->
                        CustomResDTO.OptionList.builder()
                            .optionId(item.getId())
                            .category(item.getCategory())
                            .optionName(item.getOptionName())
                            .additionalPrice(item.getAdditionalPrice())
                            .colorRgbCode(item.getColorRgbCode())
                            .build())
                .toList())
        .build();
  }
}

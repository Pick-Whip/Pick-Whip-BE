package com.example.picknwhip_be.domain.custom.converter;

import com.example.picknwhip_be.domain.custom.dto.CustomResDTO;
import com.example.picknwhip_be.domain.custom.entity.OrderDraft;
import com.example.picknwhip_be.domain.shop.entity.CustomOption;
import com.example.picknwhip_be.domain.shop.entity.ShopCakeSize;
import java.util.List;

public class CustomConverter {

  public static CustomResDTO.GetDraftDetailDTO toDraftDetailDTO(OrderDraft draft) {
    return CustomResDTO.GetDraftDetailDTO.builder()
        .draftId(draft.getId())
        .shopCakeSizeId(draft.getShopCakeSize().getId())
        .pickupDatetime(draft.getPickupDatetime())
        .letteringText(draft.getLetteringText())
        .letteringLineCount(draft.getLetteringLineCount())
        .letteringAlignment(draft.getLetteringAlignment())
        .additionalRequest(draft.getAdditionalRequest())
        .referenceImageUrl(draft.getReferenceImageUrl())
        .customOptionIds(
            draft.getItems().stream()
                .filter(item -> item.getPositionX() == null)
                .map(item -> item.getCustomOption().getId())
                .toList())
        .toppings(
            draft.getItems().stream()
                .filter(item -> item.getPositionX() != null)
                .map(
                    item ->
                        CustomResDTO.Toppings.builder()
                            .optionId(item.getCustomOption().getId())
                            .x(item.getPositionX())
                            .y(item.getPositionY())
                            .build())
                .toList())
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

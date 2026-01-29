package com.example.picknwhip_be.domain.custom.converter;

import com.example.picknwhip_be.domain.custom.dto.CustomResDTO;
import com.example.picknwhip_be.domain.custom.entity.OrderDraft;

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
}

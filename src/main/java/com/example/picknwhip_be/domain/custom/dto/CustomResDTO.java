package com.example.picknwhip_be.domain.custom.dto;

import com.example.picknwhip_be.domain.order.entity.enums.LetteringAlignment;
import com.example.picknwhip_be.domain.order.entity.enums.LetteringLineCount;
import com.example.picknwhip_be.domain.shop.entity.enums.OptionCategory;
import java.time.LocalDateTime;
import java.util.List;
import lombok.*;

public class CustomResDTO {

  @Builder
  public record CustomCreateDTO(Long customId, LocalDateTime createAt) {}

  @Builder
  @Getter
  @NoArgsConstructor
  @AllArgsConstructor
  public static class GetDraftListDTO {
    private Long draftId;
    private String shopCakeSize;
    private LocalDateTime pickupDatetime;
    private String sheetName;
    private Long progressPercentage;
    private String presentStatus;
    private LocalDateTime updateAt;
  }

  @Builder
  @Getter
  @NoArgsConstructor
  @AllArgsConstructor
  public static class GetDraftDetailDTO {
    private Long draftId;
    private Long shopCakeSizeId;
    private LocalDateTime pickupDatetime;
    private String letteringText;
    private LetteringLineCount letteringLineCount;
    private LetteringAlignment letteringAlignment;
    private String additionalRequest;
    private String referenceImageUrl;

    private List<Long> customOptionIds;
    private List<Topping> toppings;
  }

  @Builder
  @Getter
  @NoArgsConstructor
  @AllArgsConstructor
  public static class Topping {
    private Long optionId;
    private String name;
    private double x;
    private double y;
  }

  @Builder
  @Getter
  @NoArgsConstructor
  @AllArgsConstructor
  public static class Option {
    private Long optionId;
    private OptionCategory category;
    private String name;
  }

  @Builder
  @Getter
  @NoArgsConstructor
  @AllArgsConstructor
  public static class DeleteDraftDTO {
    private Long draftId;
  }
}

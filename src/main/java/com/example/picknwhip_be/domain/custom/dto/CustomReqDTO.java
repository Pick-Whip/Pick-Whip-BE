package com.example.picknwhip_be.domain.custom.dto;

import com.example.picknwhip_be.domain.order.entity.enums.LetteringAlignment;
import com.example.picknwhip_be.domain.order.entity.enums.LetteringLineCount;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.List;

public class CustomReqDTO {

  public record CustomCreateDTO(
      @NotNull Long shopId,
      @NotNull Long shopCakeSizeId,
      @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
          LocalDateTime pickupDatetime,
      String letteringText,
      LetteringLineCount letteringLineCount,
      LetteringAlignment letteringAlignment,
      String additionalRequest,
      String referenceImageUrl,
      String paymentMethod,
      List<Long> customOptionIds,
      List<Toppings> toppings) {

    public record Toppings(Long optionId, Double x, Double y) {}
  }
}

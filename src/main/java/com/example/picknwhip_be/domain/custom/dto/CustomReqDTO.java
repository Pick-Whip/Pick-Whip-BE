package com.example.picknwhip_be.domain.custom.dto;

import com.example.picknwhip_be.domain.order.entity.enums.LetteringAlignment;
import com.example.picknwhip_be.domain.order.entity.enums.LetteringLineCount;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.List;

public class CustomReqDTO {

  public record CustomCreateDTO(
      @Schema(description = "가게 ID", example = "1") @NotNull Long shopId,
      @Schema(description = "케이크 사이즈 ID", example = "1") // 👈 기본값 1
          @NotNull
          Long shopCakeSizeId,
      @Schema(
              description = "픽업 날짜 (yyyy-MM-dd'T'HH:mm:ss)",
              example = "2026-01-30T16:36:31",
              type = "string")
          @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
          LocalDateTime pickupDatetime,
      String letteringText,
      LetteringLineCount letteringLineCount,
      LetteringAlignment letteringAlignment,
      String additionalRequest,
      String referenceImageUrl,
      String paymentMethod,
      @Schema(description = "선택한 옵션 ID 목록", example = "[1, 2]") List<Long> customOptionIds,
      List<Toppings> toppings) {

    public record Toppings(Long optionId, Double x, Double y) {}
  }
}

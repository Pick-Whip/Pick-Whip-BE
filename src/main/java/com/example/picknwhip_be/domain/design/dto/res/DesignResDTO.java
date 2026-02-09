package com.example.picknwhip_be.domain.design.dto.res;

import com.example.picknwhip_be.domain.custom.dto.res.CustomResDTO;
import com.example.picknwhip_be.domain.order.entity.enums.LetteringAlignment;
import com.example.picknwhip_be.domain.order.entity.enums.LetteringLineCount;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class DesignResDTO {

  @Builder
  @Getter
  @NoArgsConstructor
  @AllArgsConstructor
  public static class DesignPreviewDTO {
    private String cakeName;
    private int price;
    private List<String> keywords;
    private String imageUrl;
  }

  @Builder
  @Getter
  @NoArgsConstructor
  @AllArgsConstructor
  public static class GetDesignListDTO {
    private List<DesignPreviewDTO> designs;
  }

  @Builder
  @Getter
  @NoArgsConstructor
  @AllArgsConstructor
  public static class GetDesignDetailDTO {
    private String cakeName;
    private String cakeSize;
    private int price;
    private String imageUrl;
    private String allergyInfo;
    private String description;
    private String letteringText;
    private LetteringLineCount letteringLineCount;
    private LetteringAlignment letteringAlignment;
    private List<String> keywords;

    private List<CustomResDTO.Topping> toppings;
    private List<CustomResDTO.Option> options;
  }

  @Builder
  public record DesignNameDTO(
      @Schema(description = "디자인 ID", example = "1") Long designId,
      @Schema(description = "디자인 이름", example = "크리스마스 케이크") String designName) {}

  @Builder
  public record DesignListDTO(@Schema(description = "디자인 목록") List<DesignNameDTO> items) {}
}

package com.example.picknwhip_be.domain.design.dto.res;

import com.example.picknwhip_be.domain.custom.dto.res.CustomResDTO;
import com.example.picknwhip_be.domain.order.entity.enums.LetteringAlignment;
import com.example.picknwhip_be.domain.order.entity.enums.LetteringLineCount;
import com.example.picknwhip_be.domain.shop.entity.enums.OptionCategory;
import com.querydsl.core.annotations.QueryProjection;
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
    private Long designId;
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
    private String letteringColor;

    private List<AvailOptionDTO> availOptions;
    private List<String> keywords;

    private List<CustomResDTO.Topping> toppings;
    private List<CustomResDTO.Option> options;
  }

  @Getter
  @Builder
  @AllArgsConstructor
  public static class AvailOptionDTO {
    private OptionCategory category;
    private String name;
    private String colorCode;
    private int price;
  }

  @Builder
  public record DesignNameDTO(
      @Schema(description = "디자인 ID", example = "1") Long designId,
      @Schema(description = "디자인 이름", example = "크리스마스 케이크") String designName) {}

  @Builder
  public record DesignListDTO(@Schema(description = "디자인 목록") List<DesignNameDTO> items) {}

  @Getter
  @Builder
  @NoArgsConstructor
  @AllArgsConstructor
  public static class GalleryListDTO {
    private String currentRegion; // 현재 위치 (예: 서울시 마포구)
    private List<GalleryItemDTO> designs;
    private Integer totalPage;
    private Long totalElements;
    private Boolean isFirst;
    private Boolean isLast;
  }

  @Getter
  @NoArgsConstructor
  public static class GalleryItemDTO {
    private Long designId;
    private Long shopId;
    private String imageUrl;
    private String shopName;
    private String simpleAddress; // "서울 마포구" 까지만
    private Integer minPrice;
    private Double avgRating;
    private boolean isMyPick;

    @QueryProjection
    public GalleryItemDTO(
        Long designId,
        Long shopId,
        String imageUrl,
        String shopName,
        String fullAddress,
        Integer minPrice,
        Double avgRating,
        Long pickId) {
      this.designId = designId;
      this.shopId = shopId;
      this.imageUrl = imageUrl;
      this.shopName = shopName;
      this.simpleAddress = parseSimpleAddress(fullAddress); // 주소 가공 로직
      this.minPrice = minPrice;
      this.avgRating = avgRating != null ? avgRating : 0.0;
      this.isMyPick = (pickId != null); // pickId가 있으면 true
    }
      public static GalleryItemDTO withImageUrl(GalleryItemDTO src, String newImageUrl) {
          GalleryItemDTO dto = new GalleryItemDTO();
          dto.designId = src.designId;
          dto.shopId = src.shopId;
          dto.imageUrl = newImageUrl;
          dto.shopName = src.shopName;
          dto.simpleAddress = src.simpleAddress;
          dto.minPrice = src.minPrice;
          dto.avgRating = src.avgRating;
          dto.isMyPick = src.isMyPick;
          return dto;
      }

    // 주소 파싱 유틸리티 메서드
    private String parseSimpleAddress(String fullAddress) {
      if (fullAddress == null) return "";
      String[] parts = fullAddress.split(" ");
      if (parts.length >= 2) {
        return parts[0] + " " + parts[1];
      }
      return fullAddress;
    }
  }
}

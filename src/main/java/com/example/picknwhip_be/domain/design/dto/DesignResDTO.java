package com.example.picknwhip_be.domain.design.dto;

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
    private int price;
    private List<String> keywords;
    private String imageUrl;
    private String allergyInfo;
    private String description;
    private List<>

  }


}

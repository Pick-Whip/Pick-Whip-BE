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
  public static class GetDesignListDTO {
    private String cakeName;
    private Long price;
    private List<String> keywords;
    private String imageUrl;
  }
}

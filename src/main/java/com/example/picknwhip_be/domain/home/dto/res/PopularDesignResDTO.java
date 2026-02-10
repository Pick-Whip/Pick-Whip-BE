package com.example.picknwhip_be.domain.home.dto.res;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class PopularDesignResDTO {
  private Integer ranking;
  private Long designId;

  private String imageUrl;
  private String shopName;
  private String cakeName;

  private String designSpec;
  private String flavorSpec;
  private String letteringPhrase;
  private String letteringOption;

  @JsonProperty("isMyPick")
  private boolean isMyPick;
}

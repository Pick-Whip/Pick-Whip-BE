package com.example.picknwhip_be.domain.home.dto.res;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class PopularDesignResDTO {
  private Integer rank;
  private Long designId;

  private String imageUrl;
  private String shopName;
  private String cakeName;

  private String designSpec;
  private String flavorSpec;
  private String letteringPhrase;
  private String letteringOption;

  private boolean isMyPick;
}

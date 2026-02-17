package com.example.picknwhip_be.domain.favorite.dto.res;

import java.util.List;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class FavoriteShopDTO {
  private Long favoriteId; // 찜 ID
  private Long shopId; // 가게 ID
  private String shopName;
  private String shopImageUrl;
    private Boolean isMyPick;
    private List<String> keywords;
}

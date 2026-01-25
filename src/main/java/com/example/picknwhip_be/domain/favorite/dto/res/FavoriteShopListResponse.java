package com.example.picknwhip_be.domain.favorite.dto.res;

import java.util.List;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class FavoriteShopListResponse {
    private List<FavoriteShopDto> shopList;
    private Long nextCursor; // 다음 요청에 쓸 커서 (마지막 아이템의 ID)
    private boolean hasNext; // 다음 페이지 존재 여부
}
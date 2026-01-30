package com.example.picknwhip_be.domain.review.dto.res;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

public class ReviewResDTO {
  // 리뷰 작성 DTO
  @Builder
  public record WriteDTO(@Schema(description = "생성된 리뷰 ID", example = "1") Long reviewId) {}
}

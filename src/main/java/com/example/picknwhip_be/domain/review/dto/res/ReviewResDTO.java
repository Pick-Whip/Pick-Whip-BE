package com.example.picknwhip_be.domain.review.dto.res;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;
import java.util.List;
import lombok.Builder;

public class ReviewResDTO {
  // 리뷰 작성 DTO
  @Builder
  public record WriteDTO(@Schema(description = "생성된 리뷰 ID", example = "1") Long reviewId) {}

  // 리뷰 DTO 코어
  @Builder
  public record ReviewDTO(
      @Schema(description = "리뷰 ID", example = "1") Long reviewId,
      @Schema(description = "별점", example = "5") int rating,
      @Schema(description = "리뷰 내용", example = "너무 예쁘고 맛있어요!") String content,
      @Schema(description = "사장님 답글", example = "감사합니다.") String reply,
      @Schema(description = "리뷰 이미지 url 목록", example = "[\"https://....jpg\"]")
          List<String> imageUrls,
      @Schema(description = "작성일", example = "2026-01-01")
          @JsonFormat(pattern = "yyyy-MM-dd", timezone = "Asia/Seoul")
          LocalDateTime createdDate) {}

  // 작성한 리뷰
  @Builder
  public record MyReviewItemDTO(
      @Schema(description = "가게명", example = "달콤한 순간") String shopName,
      @Schema(description = "케이크 옵션", example = "기념일 케이크") String option,
      @Schema(description = "리뷰 내용") ReviewDTO review) {}

  // 작성한 리뷰 목록 조회 DTO
  @Builder
  public record MyReviewListDTO(
      @Schema(description = "작성한 리뷰 개수", example = "1") int count,
      @Schema(description = "평균 별점", example = "5.0") double rating,
      @Schema(description = "리뷰 목록") List<MyReviewItemDTO> items,
      @Schema(description = "다음 커서 값", example = "0") Long nextCursor,
      @Schema(description = "다음 데이터 존재", example = "true") boolean hasNext) {}
}

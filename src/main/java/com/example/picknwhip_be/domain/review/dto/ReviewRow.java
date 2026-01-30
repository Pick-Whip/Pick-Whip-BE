package com.example.picknwhip_be.domain.review.dto;

import com.querydsl.core.annotations.QueryProjection;
import java.time.LocalDateTime;

// 작성한 리뷰 목록 조회 응답을 위한 중간 조회 전용 DTO 모음
public class ReviewRow {
  public record MyReviewRow(
      Long reviewId,
      String shopName,
      String option,
      int rating,
      String content,
      LocalDateTime CreatedAt) {
    @QueryProjection
    public MyReviewRow {}
  }

  public record MyReviewImageRow(Long reviewId, String s3Key, int sortOrder) {
    @QueryProjection
    public MyReviewImageRow {}
  }

  public record MyReviewReplyRow(Long reviewId, String replyContent) {
    @QueryProjection
    public MyReviewReplyRow {}
  }

  public record MyReviewSummaryRow(long count, Double averageRating) {
    @QueryProjection
    public MyReviewSummaryRow {}
  }
}

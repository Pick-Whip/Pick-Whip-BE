package com.example.picknwhip_be.domain.review.dto;

import com.querydsl.core.annotations.QueryProjection;
import java.time.LocalDateTime;

// 중간 조회 전용 DTO 모음
public class ReviewRow {
  public record MyReviewRow(
      Long reviewId,
      String shopName,
      String option,
      int rating,
      String content,
      LocalDateTime createdAt) {
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

  public record ReviewDetailRow(
      Long reviewId,
      String nickname,
      String profileUrl,
      int rating,
      String content,
      LocalDateTime createdAt,
      String reply) {
    @QueryProjection
    public ReviewDetailRow {}
  }

  public record KeywordRow(Long reviewId, String code, String label) {
    @QueryProjection
    public KeywordRow {}
  }

  public record ShopReviewRow(
      Long reviewId,
      String nickname,
      String profileUrl,
      int rating,
      String option,
      String content,
      int likeCount,
      LocalDateTime createdDate) {
    @QueryProjection
    public ShopReviewRow {}
  }
}

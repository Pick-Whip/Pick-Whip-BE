package com.example.picknwhip_be.domain.review.service.query.cursor;

import com.example.picknwhip_be.domain.review.dto.ReviewRow;
import com.example.picknwhip_be.domain.review.enums.ReviewSort;
import java.time.LocalDateTime;

public record ReviewCursor(Long primaryValue, LocalDateTime createdAt, Long reviewId) {
  public static ReviewCursor forLatest(LocalDateTime createdAt, Long reviewId) {
    return new ReviewCursor(null, createdAt, reviewId);
  }

  public static ReviewCursor forPrimary(long primaryValue, long reviewId) {
    return new ReviewCursor(primaryValue, null, reviewId);
  }

  public static ReviewCursor fromRow(ReviewSort sort, ReviewRow.ShopReviewRow row) {
    return switch (sort) {
      case LATEST -> ReviewCursor.forLatest(row.createdDate(), row.reviewId());
      case HELPFUL -> ReviewCursor.forPrimary(row.likeCount(), row.reviewId());
      case RATING_HIGH, RATING_LOW -> ReviewCursor.forPrimary(row.rating(), row.reviewId());
    };
  }
}

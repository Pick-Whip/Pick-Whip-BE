package com.example.picknwhip_be.domain.review.repository;

import com.example.picknwhip_be.domain.review.dto.ReviewRow;
import java.util.List;

public interface ReviewRepositoryCustom {
  ReviewRow.MyReviewSummaryRow fetchMyReviewSummary(Long userId);

  List<ReviewRow.MyReviewRow> fetchMyReviews(Long userId, Long cursor, int limit);

  List<ReviewRow.MyReviewImageRow> fetchMyReviewImages(List<Long> reviewIds);

  List<ReviewRow.MyReviewReplyRow> fetchMyReviewReplies(List<Long> reviewIds);
}

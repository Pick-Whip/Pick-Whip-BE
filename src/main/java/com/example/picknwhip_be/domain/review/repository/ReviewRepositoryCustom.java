package com.example.picknwhip_be.domain.review.repository;

import com.example.picknwhip_be.domain.design.enums.Style;
import com.example.picknwhip_be.domain.review.dto.ReviewRow;
import com.example.picknwhip_be.domain.review.entity.Review;
import com.example.picknwhip_be.domain.review.enums.ReviewSort;
import com.example.picknwhip_be.domain.review.service.query.cursor.ReviewCursor;
import java.util.List;
import java.util.Set;

public interface ReviewRepositoryCustom {
  ReviewRow.ReviewSummaryRow fetchMyReviewSummary(Long userId);

  List<ReviewRow.MyReviewRow> fetchMyReviews(Long userId, Long cursor, int limit);

  List<ReviewRow.MyReviewImageRow> fetchMyReviewImages(List<Long> reviewIds);

  List<ReviewRow.MyReviewReplyRow> fetchMyReviewReplies(List<Long> reviewIds);

  ReviewRow.ReviewDetailRow fetchReviewDetail(Long reviewId);

  List<ReviewRow.KeywordRow> fetchReviewDetailKeywords(Long reviewId);

  List<ReviewRow.KeywordRow> fetchReviewKeywords(List<Long> reviewIds);

  List<Review> findBestHelpfulReviews(int limit);

  List<ReviewRow.ShopReviewRow> fetchShopReviewRows(
      Long shopId,
      ReviewSort sort,
      List<Long> designIds,
      List<Style> styles,
      ReviewCursor cursor,
      int limit);

  Set<Long> fetchLikedReviewIds(Long userId, List<Long> reviewIds);

  ReviewRow.ReviewSummaryRow fetchShopReviewSummary(Long shopId);

  List<ReviewRow.KeywordCategoryCountRow> fetchShopKeywordCategoryCounts(Long shopId);
}

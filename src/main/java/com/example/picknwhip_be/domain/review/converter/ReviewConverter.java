package com.example.picknwhip_be.domain.review.converter;

import com.example.picknwhip_be.domain.order.entity.Order;
import com.example.picknwhip_be.domain.review.dto.ReviewRow;
import com.example.picknwhip_be.domain.review.dto.req.ReviewReqDTO;
import com.example.picknwhip_be.domain.review.dto.res.ReviewResDTO;
import com.example.picknwhip_be.domain.review.entity.Review;
import com.example.picknwhip_be.domain.review.entity.mapping.ReviewLike;
import com.example.picknwhip_be.domain.review.enums.KeywordCategory;
import com.example.picknwhip_be.domain.user.entity.User;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class ReviewConverter {
  public static ReviewResDTO.WriteDTO toWriteDTO(Long reviewId) {
    return ReviewResDTO.WriteDTO.builder().reviewId(reviewId).build();
  }

  public static Review toReview(Order order, ReviewReqDTO.WriteDTO dto) {
    return Review.builder()
        .order(order)
        .user(order.getUser())
        .shop(order.getShop())
        .design(order.getDesignGallery())
        .rating(dto.rating())
        .content(dto.content())
        .agreement(dto.agreement())
        .build();
  }

  public static ReviewResDTO.MyReviewListDTO toMyReviewListDTO(
      long count,
      double avgRating,
      List<ReviewRow.MyReviewRow> pageRows,
      Map<Long, List<String>> imageUrlsByReviewId,
      Map<Long, String> replyByReviewId,
      Long nextCursor,
      boolean hasNext) {
    List<ReviewResDTO.MyReviewItemDTO> items = new ArrayList<>(pageRows.size());

    for (ReviewRow.MyReviewRow row : pageRows) {
      ReviewResDTO.ReviewDTO content =
          ReviewResDTO.ReviewDTO.builder()
              .reviewId(row.reviewId())
              .rating(row.rating())
              .content(row.content())
              .createdDate(row.createdAt())
              .reply(replyByReviewId.get(row.reviewId()))
              .imageUrls(imageUrlsByReviewId.getOrDefault(row.reviewId(), List.of()))
              .build();

      items.add(
          ReviewResDTO.MyReviewItemDTO.builder()
              .shopName(row.shopName())
              .option(row.option())
              .review(content)
              .build());
    }

    return ReviewResDTO.MyReviewListDTO.builder()
        .count(Math.toIntExact(count))
        .rating(avgRating)
        .items(items)
        .nextCursor(nextCursor)
        .hasNext(hasNext)
        .build();
  }

  public static ReviewLike toReviewLike(Review review, User user) {
    return ReviewLike.builder().review(review).user(user).build();
  }

  public static ReviewResDTO.ReviewLikeDTO toReviewLikeDTO(
      Long reviewId, boolean isLike, Long likeCount) {
    return ReviewResDTO.ReviewLikeDTO.builder()
        .reviewId(reviewId)
        .isLike(isLike)
        .likeCount(likeCount)
        .build();
  }

  public static ReviewResDTO.ReviewDetailDTO toReviewDetailDTO(
      Long reviewId,
      int rating,
      String content,
      String reply,
      List<String> imageUrls,
      LocalDateTime createdDate,
      String nickname,
      String profileUrl,
      List<ReviewResDTO.KeywordDTO> keywords) {
    return ReviewResDTO.ReviewDetailDTO.builder()
        .reviewId(reviewId)
        .nickname(nickname)
        .profileUrl(profileUrl)
        .rating(rating)
        .content(content)
        .imageUrls(imageUrls)
        .keywords(keywords)
        .createdDate(createdDate)
        .reply(reply)
        .build();
  }

  public static ReviewResDTO.ShopReviewListDTO toShopReviewListDTO(
      List<ReviewRow.ShopReviewRow> rows,
      Map<Long, List<String>> imageUrlsByReviewId,
      Map<Long, List<ReviewResDTO.KeywordDTO>> keywordsByReviewId,
      Set<Long> likedReviewIds,
      String nextCursor,
      boolean hasNext) {

    List<ReviewResDTO.ShopReviewItemDTO> items =
        rows.stream()
            .map(
                r ->
                    ReviewResDTO.ShopReviewItemDTO.builder()
                        .reviewId(r.reviewId())
                        .nickname(r.nickname())
                        .profileUrl(r.profileUrl())
                        .rating(r.rating())
                        .option(r.option())
                        .content(r.content())
                        .imageUrls(imageUrlsByReviewId.getOrDefault(r.reviewId(), List.of()))
                        .keywords(keywordsByReviewId.getOrDefault(r.reviewId(), List.of()))
                        .isLike(likedReviewIds.contains(r.reviewId()))
                        .likeCount((long) r.likeCount())
                        .createdDate(r.createdDate())
                        .build())
            .toList();

    return ReviewResDTO.ShopReviewListDTO.builder()
        .items(items)
        .nextCursor(nextCursor)
        .hasNext(hasNext)
        .build();
  }

  public static ReviewResDTO.ShopReviewSummaryDTO toShopReviewSummaryDTO(
      double rating, int count, List<ReviewRow.KeywordCategoryCountRow> categoryCounts) {
    Map<KeywordCategory, Long> countMap =
        categoryCounts.stream()
            .collect(
                Collectors.toMap(
                    ReviewRow.KeywordCategoryCountRow::category,
                    ReviewRow.KeywordCategoryCountRow::count));

    ReviewResDTO.KeywordRankingDTO keywordRankingDTO =
        ReviewResDTO.KeywordRankingDTO.builder()
            .DESIGN_SATISFACTION(
                countMap.getOrDefault(KeywordCategory.DESIGN_SATISFACTION, 0L).intValue())
            .SAME_AS_RESULT(countMap.getOrDefault(KeywordCategory.SAME_AS_RESULT, 0L).intValue())
            .TASTE(countMap.getOrDefault(KeywordCategory.TASTE, 0L).intValue())
            .COMMUNICATION(countMap.getOrDefault(KeywordCategory.COMMUNICATION, 0L).intValue())
            .PICKUP(countMap.getOrDefault(KeywordCategory.PICKUP, 0L).intValue())
            .build();

    return ReviewResDTO.ShopReviewSummaryDTO.builder()
        .rating(rating)
        .count(count)
        .keywordRanking(List.of(keywordRankingDTO))
        .build();
  }
}

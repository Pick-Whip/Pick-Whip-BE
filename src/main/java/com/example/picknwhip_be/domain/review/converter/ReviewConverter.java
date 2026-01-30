package com.example.picknwhip_be.domain.review.converter;

import com.example.picknwhip_be.domain.order.entity.Order;
import com.example.picknwhip_be.domain.review.dto.ReviewRow;
import com.example.picknwhip_be.domain.review.dto.req.ReviewReqDTO;
import com.example.picknwhip_be.domain.review.dto.res.ReviewResDTO;
import com.example.picknwhip_be.domain.review.entity.Review;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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
              .createdDate(row.CreatedAt())
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
}

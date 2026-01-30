package com.example.picknwhip_be.domain.review.converter;

import com.example.picknwhip_be.domain.order.entity.Order;
import com.example.picknwhip_be.domain.review.dto.req.ReviewReqDTO;
import com.example.picknwhip_be.domain.review.dto.res.ReviewResDTO;
import com.example.picknwhip_be.domain.review.entity.Review;

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
}

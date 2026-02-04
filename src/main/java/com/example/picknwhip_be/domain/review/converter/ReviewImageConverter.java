package com.example.picknwhip_be.domain.review.converter;

import com.example.picknwhip_be.domain.review.entity.Review;
import com.example.picknwhip_be.domain.review.entity.mapping.ReviewImage;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class ReviewImageConverter {
  public static List<ReviewImage> toReviewImages(Review review, List<String> imageKeys) {
    if (imageKeys == null || imageKeys.isEmpty()) {
      return List.of();
    }

    AtomicInteger sortOrder = new AtomicInteger(1);

    return imageKeys.stream()
        .map(
            key ->
                ReviewImage.builder()
                    .review(review)
                    .s3Key(key)
                    .sortOrder(sortOrder.getAndIncrement())
                    .build())
        .toList();
  }
}

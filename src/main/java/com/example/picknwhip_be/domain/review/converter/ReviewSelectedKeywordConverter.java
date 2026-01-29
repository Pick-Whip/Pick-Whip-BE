package com.example.picknwhip_be.domain.review.converter;

import com.example.picknwhip_be.domain.review.entity.Review;
import com.example.picknwhip_be.domain.review.entity.mapping.ReviewKeyword;
import com.example.picknwhip_be.domain.review.entity.mapping.ReviewSelectedKeyword;
import java.util.List;

public class ReviewSelectedKeywordConverter {
  public static List<ReviewSelectedKeyword> toSelectedKeywords(
      Review review, List<ReviewKeyword> keywords) {
    if (keywords == null || keywords.isEmpty()) {
      return List.of();
    }

    return keywords.stream()
        .map(keyword -> ReviewSelectedKeyword.builder().review(review).keyword(keyword).build())
        .toList();
  }
}

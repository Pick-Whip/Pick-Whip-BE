package com.example.picknwhip_be.domain.review.service.query;

import com.example.picknwhip_be.domain.review.dto.res.ReviewResDTO;

public interface ReviewQueryService {
  ReviewResDTO.MyReviewListDTO getMyReviewList(Long cursor, int size, Long userId);
    ReviewResDTO.BestReviewListDTO getBestCustomReviews();
}

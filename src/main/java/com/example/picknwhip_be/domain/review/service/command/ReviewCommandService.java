package com.example.picknwhip_be.domain.review.service.command;

import com.example.picknwhip_be.domain.review.dto.req.ReviewReqDTO;
import com.example.picknwhip_be.domain.review.dto.res.ReviewResDTO;

public interface ReviewCommandService {

  ReviewResDTO.WriteDTO createReview(Long orderId, ReviewReqDTO.WriteDTO dto, Long userId);

  Void deleteReview(Long reviewId, Long userId);

  ReviewResDTO.ReviewLikeDTO saveReviewLike(Long reviewId, Long userId);

  ReviewResDTO.ReviewLikeDTO deleteReviewLike(Long reviewId, Long userId);
}

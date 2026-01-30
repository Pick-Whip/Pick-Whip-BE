package com.example.picknwhip_be.domain.review.service.query;

import com.example.picknwhip_be.domain.review.dto.res.ReviewResDTO;

public interface ReviewQueryService {
  ReviewResDTO.MyReviewListDTO GetMyReviewList(Long cursor, int size, Long userId);
}

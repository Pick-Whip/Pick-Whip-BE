package com.example.picknwhip_be.domain.review.service.command;

import com.example.picknwhip_be.domain.review.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ReviewPurgeService {

  private final ReviewRepository reviewRepository;
  private final ReviewReplyRepository reviewReplyRepository;
  private final ReviewImageRepository reviewImageRepository;
  private final ReviewSelectedKeywordRepository reviewSelectedKeywordRepository;
  private final ReviewLikeRepository reviewLikeRepository;

  @Transactional
  public int hardDeleteReviewGraph(Long reviewId) {
    reviewSelectedKeywordRepository.deleteByReviewId(reviewId);
    reviewLikeRepository.deleteByReviewId(reviewId);
    reviewImageRepository.hardDeleteAllByReviewId(reviewId);
    reviewReplyRepository.hardDeleteAllByReviewId(reviewId);
    return reviewRepository.hardDeleteById(reviewId);
  }
}

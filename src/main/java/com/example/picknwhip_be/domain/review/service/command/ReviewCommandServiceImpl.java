package com.example.picknwhip_be.domain.review.service.command;

import com.example.picknwhip_be.domain.order.entity.Order;
import com.example.picknwhip_be.domain.order.repository.OrderRepository;
import com.example.picknwhip_be.domain.review.converter.ReviewConverter;
import com.example.picknwhip_be.domain.review.converter.ReviewImageConverter;
import com.example.picknwhip_be.domain.review.converter.ReviewSelectedKeywordConverter;
import com.example.picknwhip_be.domain.review.dto.req.ReviewReqDTO;
import com.example.picknwhip_be.domain.review.dto.res.ReviewResDTO;
import com.example.picknwhip_be.domain.review.entity.Review;
import com.example.picknwhip_be.domain.review.entity.mapping.ReviewImage;
import com.example.picknwhip_be.domain.review.entity.mapping.ReviewKeyword;
import com.example.picknwhip_be.domain.review.entity.mapping.ReviewSelectedKeyword;
import com.example.picknwhip_be.domain.review.exception.ReviewException;
import com.example.picknwhip_be.domain.review.exception.code.ReviewErrorCode;
import com.example.picknwhip_be.domain.review.repository.*;
import com.example.picknwhip_be.domain.review.validator.ReviewImageKeyValidator;
import java.time.Clock;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ReviewCommandServiceImpl implements ReviewCommandService {

  private final ReviewRepository reviewRepository;
  private final ReviewImageRepository reviewImageRepository;
  private final ReviewSelectedKeywordRepository reviewSelectedKeywordRepository;
  private final OrderRepository orderRepository;
  private final ReviewKeywordRepository reviewKeywordRepository;
  private final ReviewImageKeyValidator reviewImageKeyValidator;
  private final ReviewReplyRepository reviewReplyRepository;
  private final Clock clock;
  private final ReviewLikeRepository reviewLikeRepository;

  @Override
  @Transactional
  public ReviewResDTO.WriteDTO createReview(Long orderId, ReviewReqDTO.WriteDTO dto, Long userId) {
    Order order =
        orderRepository
            .findById(orderId)
            .orElseThrow(() -> new ReviewException(ReviewErrorCode.ORDER_NOT_FOUND));

    if (!order.getUser().getUserId().equals(userId)) {
      throw new ReviewException(ReviewErrorCode.REVIEW_WRITE_NOT_ALLOWED);
    }

    if (reviewRepository.existsByOrderIdAndDeletedAtIsNull(orderId)) {
      throw new ReviewException(ReviewErrorCode.REVIEW_ALREADY_EXISTS);
    }

    // 리뷰 저장
    Review review = ReviewConverter.toReview(order, dto);
    try {
      reviewRepository.save(review);
    } catch (DataIntegrityViolationException e) {
      throw new ReviewException(ReviewErrorCode.REVIEW_ALREADY_EXISTS);
    }

    // 리뷰 이미지 저장
    if (dto.imageKeys() != null && !dto.imageKeys().isEmpty()) {
      reviewImageKeyValidator.validateAll(dto.imageKeys());

      List<ReviewImage> images = ReviewImageConverter.toReviewImages(review, dto.imageKeys());
      reviewImageRepository.saveAll(images);
    }

    // 리뷰 키워드 저장
    Set<String> unique = new HashSet<>(dto.keywords());
    if (unique.size() != dto.keywords().size()) {
      throw new ReviewException(ReviewErrorCode.REVIEW_KEYWORD_DUPLICATED);
    }

    List<ReviewKeyword> keywords = reviewKeywordRepository.findAllByCodeIn(dto.keywords());

    if (keywords.size() != dto.keywords().size()) {
      throw new ReviewException(ReviewErrorCode.INVALID_REVIEW_KEYWORD_CODE);
    }

    List<ReviewSelectedKeyword> mappings =
        ReviewSelectedKeywordConverter.toSelectedKeywords(review, keywords);

    reviewSelectedKeywordRepository.saveAll(mappings);

    return ReviewConverter.toWriteDTO(review.getId());
  }

  @Override
  @Transactional
  public Void deleteReview(Long reviewId, Long userId) {
    Review review =
        reviewRepository
            .findById(reviewId)
            .orElseThrow(() -> new ReviewException(ReviewErrorCode.REVIEW_NOT_FOUND));

    if (!review.getUser().getUserId().equals(userId)) {
      throw new ReviewException(ReviewErrorCode.REVIEW_WRITE_NOT_ALLOWED);
    }

    if (review.isDeleted()) {
      return null;
    }

    LocalDateTime now = LocalDateTime.now(clock);

    review.softDelete(now);

    reviewReplyRepository.softDeleteAllByReviewId(reviewId, now);
    reviewImageRepository.softDeleteAllByReviewId(reviewId, now);

    reviewSelectedKeywordRepository.deleteByReviewId(reviewId);
    reviewLikeRepository.deleteByReviewId(reviewId);

    return null;
  }
}

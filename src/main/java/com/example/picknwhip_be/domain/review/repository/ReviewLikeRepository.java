package com.example.picknwhip_be.domain.review.repository;

import com.example.picknwhip_be.domain.review.entity.mapping.ReviewLike;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ReviewLikeRepository extends JpaRepository<ReviewLike, Long> {
  @Modifying(clearAutomatically = true, flushAutomatically = true)
  @Query("delete from ReviewLike rl where rl.review.id = :reviewId")
  void deleteByReviewId(@Param("reviewId") Long reviewId);

  boolean existsByReviewIdAndUserUserId(Long reviewId, Long userId);

  void deleteByReviewIdAndUserUserId(Long reviewId, Long userId);

  long countByReviewId(Long reviewId);
}

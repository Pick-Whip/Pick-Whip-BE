package com.example.picknwhip_be.domain.review.repository;

import com.example.picknwhip_be.domain.review.entity.mapping.ReviewReply;
import java.time.LocalDateTime;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ReviewReplyRepository extends JpaRepository<ReviewReply, Long> {
  @Modifying(clearAutomatically = true, flushAutomatically = true)
  @Query(
      """
      update ReviewReply rr
         set rr.deletedAt = :now
       where rr.review.id = :reviewId
         and rr.deletedAt is null
      """)
  void softDeleteAllByReviewId(@Param("reviewId") Long reviewId, @Param("now") LocalDateTime now);

  @Modifying
  @Query("delete from ReviewReply rr where rr.review.id = :reviewId")
  void hardDeleteAllByReviewId(@Param("reviewId") Long reviewId);
}

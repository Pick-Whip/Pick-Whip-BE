package com.example.picknwhip_be.domain.review.repository;

import com.example.picknwhip_be.domain.review.entity.mapping.ReviewImage;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ReviewImageRepository extends JpaRepository<ReviewImage, Long> {
  @Modifying(clearAutomatically = true, flushAutomatically = true)
  @Query(
      """
      update ReviewImage ri
         set ri.deletedAt = :now
       where ri.review.id = :reviewId
         and ri.deletedAt is null
      """)
  void softDeleteAllByReviewId(@Param("reviewId") Long reviewId, @Param("now") LocalDateTime now);

  @Query(
      value =
          """
                    select s3_key
                      from review_image
                     where review_id = :reviewId
                    """,
      nativeQuery = true)
  List<String> findS3KeysByReviewId(@Param("reviewId") Long reviewId);

  @Modifying
  @Query("delete from ReviewImage ri where ri.review.id = :reviewId")
  int hardDeleteAllByReviewId(@Param("reviewId") Long reviewId);
}

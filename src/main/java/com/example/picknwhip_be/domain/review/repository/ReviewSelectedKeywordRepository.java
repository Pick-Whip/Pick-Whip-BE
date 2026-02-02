package com.example.picknwhip_be.domain.review.repository;

import com.example.picknwhip_be.domain.review.entity.mapping.ReviewSelectedKeyword;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ReviewSelectedKeywordRepository
    extends JpaRepository<ReviewSelectedKeyword, Long> {
  @Modifying(clearAutomatically = true, flushAutomatically = true)
  @Query("delete from ReviewSelectedKeyword rsk where rsk.review.id = :reviewId")
  void deleteByReviewId(@Param("reviewId") Long reviewId);

  @Query("select rsk from ReviewSelectedKeyword rsk join fetch rsk.keyword where rsk.review.id in :reviewIds")
  List<ReviewSelectedKeyword> findAllByReviewIdIn(@Param("reviewIds") List<Long> reviewIds);
}

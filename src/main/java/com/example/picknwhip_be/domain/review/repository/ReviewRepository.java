package com.example.picknwhip_be.domain.review.repository;

import com.example.picknwhip_be.domain.review.entity.Review;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ReviewRepository extends JpaRepository<Review, Long> {
  boolean existsByOrderId(Long orderId);

  @Query(
      value =
          """
                    select id
                      from review
                     where deleted_at is not null
                       and deleted_at < :cutoff
                    order by id
                    """,
      nativeQuery = true)
  List<Long> findPurgeTargetIds(@Param("cutoff") LocalDateTime cutoff);

  @Modifying
  @Query(value = "delete from review where id = :reviewId", nativeQuery = true)
  int hardDeleteById(@Param("reviewId") Long reviewId);
}

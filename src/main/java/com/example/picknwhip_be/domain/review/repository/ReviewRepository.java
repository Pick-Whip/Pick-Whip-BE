package com.example.picknwhip_be.domain.review.repository;

import com.example.picknwhip_be.domain.review.entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReviewRepository extends JpaRepository<Review, Long> {
  boolean existsByOrderId(Long orderId);
}

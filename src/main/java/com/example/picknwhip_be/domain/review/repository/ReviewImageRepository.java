package com.example.picknwhip_be.domain.review.repository;

import com.example.picknwhip_be.domain.review.entity.mapping.ReviewImage;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReviewImageRepository extends JpaRepository<ReviewImage, Long> {}

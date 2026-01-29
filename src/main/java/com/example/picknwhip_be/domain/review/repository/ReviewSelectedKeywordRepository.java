package com.example.picknwhip_be.domain.review.repository;

import com.example.picknwhip_be.domain.review.entity.mapping.ReviewSelectedKeyword;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReviewSelectedKeywordRepository
    extends JpaRepository<ReviewSelectedKeyword, Long> {}

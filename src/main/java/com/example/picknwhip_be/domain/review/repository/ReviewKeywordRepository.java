package com.example.picknwhip_be.domain.review.repository;

import com.example.picknwhip_be.domain.review.entity.mapping.ReviewKeyword;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReviewKeywordRepository extends JpaRepository<ReviewKeyword, Long> {
  List<ReviewKeyword> findAllByCodeIn(List<String> codeList);
}

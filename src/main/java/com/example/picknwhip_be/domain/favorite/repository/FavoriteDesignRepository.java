package com.example.picknwhip_be.domain.favorite.repository;

import com.example.picknwhip_be.domain.design.entity.DesignGallery;
import com.example.picknwhip_be.domain.favorite.entity.FavoriteDesign;
import com.example.picknwhip_be.domain.review.service.user.entity.User;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface FavoriteDesignRepository extends JpaRepository<FavoriteDesign, Long> {

  @Query(
      "SELECT DISTINCT fd FROM FavoriteDesign fd JOIN FETCH fd.designGallery dg LEFT JOIN FETCH dg.keywords WHERE fd.user.userId = :userId")
  List<FavoriteDesign> findAllByUserId(@Param("userId") Long userId);

  boolean existsByUserAndDesignGallery(User user, DesignGallery designGallery);

  Optional<FavoriteDesign> findByUserAndDesignGallery(User user, DesignGallery designGallery);
}

package com.example.picknwhip_be.domain.favorite.repository;

import com.example.picknwhip_be.domain.design.entity.DesignGallery;
import com.example.picknwhip_be.domain.favorite.entity.FavoriteDesign;
import com.example.picknwhip_be.domain.user.entity.User;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface FavoriteDesignRepository extends JpaRepository<FavoriteDesign, Long> {

  @Query(
      "SELECT fd FROM FavoriteDesign fd JOIN FETCH fd.designGallery WHERE fd.user.userId = :userId")
  List<FavoriteDesign> findAllByUserId(@Param("userId") Long UserId);

  boolean existsByUserAndDesignGallery(User user, DesignGallery designGallery);

  Optional<FavoriteDesign> findByUserAndDesignGallery(User user, DesignGallery designGallery);
}

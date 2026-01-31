package com.example.picknwhip_be.domain.favorite.repository;

import com.example.picknwhip_be.domain.design.entity.DesignGallery;
import com.example.picknwhip_be.domain.favorite.entity.FavoriteDesign;
import com.example.picknwhip_be.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FavoriteDesignRepository extends JpaRepository<FavoriteDesign, Long> {

    boolean existsByUserAndDesignGallery(User user, DesignGallery designGallery);
}

package com.example.picknwhip_be.domain.favorite.repository;

import com.example.picknwhip_be.domain.favorite.entity.FavoriteShop;
import com.example.picknwhip_be.domain.shop.entity.Shop;
import com.example.picknwhip_be.domain.user.entity.User;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FavoriteShopRepository extends JpaRepository<FavoriteShop, Long> {

  // 특정 유저가 특정 가게를 찜했는지 확인
  boolean existsByUserAndShop(User user, Shop shop);

  // 찜 삭제를 위해 조회
  Optional<FavoriteShop> findByUserAndShop(User user, Shop shop);
}

package com.example.picknwhip_be.domain.favorite.repository;

import com.example.picknwhip_be.domain.favorite.entity.FavoriteShop;
import com.example.picknwhip_be.domain.shop.entity.Shop;
import com.example.picknwhip_be.domain.user.entity.User;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface FavoriteShopRepository extends JpaRepository<FavoriteShop, Long> {

  // 특정 유저가 특정 가게를 찜했는지 확인
  boolean existsByUserAndShop(User user, Shop shop);

  // 찜 삭제를 위해 조회
  Optional<FavoriteShop> findByUserAndShop(User user, Shop shop);

  /** 마이픽 목록 조회 (커서 페이징 + Fetch Join) :cursor가 null이면 가장 최신꺼부터 조회, 있으면 그 ID보다 작은 것 조회 */
  @Query(
          "SELECT fs.id FROM FavoriteShop fs "
                  + "WHERE fs.user.userId = :userId "
                  + "AND (:cursor IS NULL OR fs.id < :cursor) "
                  + "ORDER BY fs.id DESC")
  List<Long> findIdsByUserIdAndCursor(
          @Param("userId") Long userId, @Param("cursor") Long cursor, Pageable pageable);

    @Query(
            "SELECT DISTINCT fs FROM FavoriteShop fs "
                    + "JOIN FETCH fs.shop s "
                    + "LEFT JOIN FETCH s.keywordMappings km "
                    + "LEFT JOIN FETCH km.keyword k "
                    + "WHERE fs.id IN :ids "
                    + "ORDER BY fs.id DESC")
    List<FavoriteShop> findAllByIdsWithShopAndKeywords(@Param("ids") List<Long> ids);

    @Query("SELECT distinct f.shop.id FROM FavoriteShop f WHERE f.user.userId = :userId")
    Set<Long> findShopIdsByUserId(@Param("userId") Long userId);
}

package com.example.picknwhip_be.domain.home.repository;

import com.example.picknwhip_be.domain.home.dto.res.PopularCakeResDTO;
import com.example.picknwhip_be.domain.home.entity.PopularCakeRanking;
import java.util.List;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface PopularCakeRankingRepository extends JpaRepository<PopularCakeRanking, Integer> {

  @Cacheable(
      value = "popularCakes",
      key = "'top5'",
      unless = "#result == null || #result.isEmpty()")
  @Query(
      """
      SELECT new com.example.picknwhip_be.domain.home.dto.res.PopularCakeResDTO(
          r.ranking,
          d.id,
          d.designName,
          d.imageUrl,
          s.id,
          s.shopName,
          s.averageRating,
          s.minPrice,
          r.orderCount
      )
      FROM PopularCakeRanking r
      JOIN r.design d
      JOIN r.shop s
      ORDER BY r.ranking ASC
  """)
  List<PopularCakeResDTO> findTop5Rankings();
}

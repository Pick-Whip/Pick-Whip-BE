package com.example.picknwhip_be.domain.home.repository;

import com.example.picknwhip_be.domain.home.entity.PopularCakeRanking;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface PopularCakeRankingRepository extends JpaRepository<PopularCakeRanking, Integer> {

  @Query(
      "SELECT r FROM PopularCakeRanking r "
          + "JOIN FETCH r.design d "
          + "JOIN FETCH r.shop s "
          + "ORDER BY r.ranking ASC")
  List<PopularCakeRanking> findTop5WithDesignAndShop();
}

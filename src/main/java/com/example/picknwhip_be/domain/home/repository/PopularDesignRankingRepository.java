package com.example.picknwhip_be.domain.home.repository;

import com.example.picknwhip_be.domain.home.entity.PopularDesignRanking;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface PopularDesignRankingRepository extends JpaRepository<PopularDesignRanking, Long> {

  @Query(
      "SELECT DISTINCT r FROM PopularDesignRanking r "
          + "JOIN FETCH r.design d "
          + "JOIN FETCH d.shop s "
          + "JOIN FETCH d.shopCakeSize "
          + "LEFT JOIN FETCH d.options "
          + "ORDER BY r.ranking ASC")
  List<PopularDesignRanking> findTop4WithDetails();
}

package com.example.picknwhip_be.domain.shop.repository;

import com.example.picknwhip_be.domain.shop.entity.ShopEvent;
import java.time.LocalDate;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ShopEventRepository extends JpaRepository<ShopEvent, Long> {

  @Query(
      "SELECT e FROM ShopEvent e "
          + "WHERE e.shop.id = :shopId "
          + "AND e.isActive = true "
          + "AND :today BETWEEN e.startDate AND e.endDate "
          + "ORDER BY e.startDate DESC LIMIT 1")
  Optional<ShopEvent> findFirstActiveEvent(
      @Param("shopId") Long shopId, @Param("today") LocalDate today);
}

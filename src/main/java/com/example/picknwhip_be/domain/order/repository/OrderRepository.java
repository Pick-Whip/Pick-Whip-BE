package com.example.picknwhip_be.domain.order.repository;

import com.example.picknwhip_be.domain.order.entity.Order;
import java.time.LocalDate;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface OrderRepository extends JpaRepository<Order, Long> {
  // 특정 날짜, 특정 가게의 시간대별 예약 건수 조회
  @Query(
      "SELECT o.pickupDatetime, COUNT(o) FROM Order o "
          + "WHERE o.shop.id = :shopId "
          + "AND FUNCTION('DATE', o.pickupDatetime) = :date "
          + "AND o.status != 'IMPOSSIBLE' "
          + // 취소/거절된 주문은 제외
          "GROUP BY o.pickupDatetime")
  List<Object[]> countOrdersByShopAndDate(
      @Param("shopId") Long shopId, @Param("date") LocalDate date);
}

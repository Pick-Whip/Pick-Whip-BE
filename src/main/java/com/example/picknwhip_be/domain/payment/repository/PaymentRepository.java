package com.example.picknwhip_be.domain.payment.repository;

import com.example.picknwhip_be.domain.order.entity.enums.PaymentStatus;
import com.example.picknwhip_be.domain.payment.entity.Payment;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface PaymentRepository extends JpaRepository<Payment, Long> {

  interface PopularCakeAgg {
    Long getDesignId();

    Long getShopId();

    Long getOrderCount();
  }

  @Query(
      value =
          """
      SELECT
        o.design_id AS designId,
        o.shop_id   AS shopId,
        COUNT(*)    AS orderCount
      FROM payments p
      JOIN orders o ON p.order_id = o.id
      WHERE p.status = 'PAID'
        AND p.approved_at >= :startAt
        AND p.approved_at <  :endAt
        AND o.design_id IS NOT NULL
      GROUP BY o.design_id, o.shop_id
      ORDER BY orderCount DESC
      LIMIT 5
      """,
      nativeQuery = true)
  List<PopularCakeAgg> findPopularCakesTop5(
      @Param("startAt") LocalDateTime startAt, @Param("endAt") LocalDateTime endAt);

  // 인기 디자인
  interface PopularDesignAgg {
    Long getDesignId();

    Long getShopId();

    Long getOrderCount();
  }

  @Query(
      "SELECT o.designGallery.id as designId, "
          + "       o.shop.id as shopId, "
          + "       COUNT(o) as orderCount "
          + "FROM Order o "
          + "WHERE o.pickupDatetime BETWEEN :startDate AND :endDate "
          + "  AND o.paymentStatus = :paymentStatus "
          + "  AND o.designGallery IS NOT NULL "
          + "GROUP BY o.designGallery.id, o.shop.id "
          + "ORDER BY orderCount DESC")
  List<PopularDesignAgg> findTop4DesignByOrders(
      @Param("startDate") LocalDateTime startDate,
      @Param("endDate") LocalDateTime endDate,
      @Param("paymentStatus") PaymentStatus paymentStatus,
      Pageable pageable);
}

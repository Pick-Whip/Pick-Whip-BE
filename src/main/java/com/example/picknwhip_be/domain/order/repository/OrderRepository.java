package com.example.picknwhip_be.domain.order.repository;

import com.example.picknwhip_be.domain.order.entity.Order;
import com.example.picknwhip_be.domain.order.entity.enums.Status;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface OrderRepository extends JpaRepository<Order, Long>, OrderRepositoryCustom {
  @Query(
      "SELECT o.pickupDatetime, COUNT(o) FROM Order o "
          + "WHERE o.shop.id = :shopId "
          + "AND o.pickupDatetime BETWEEN :startOfDay AND :endOfDay "
          + "AND o.status NOT IN :excludedStatuses "
          + "GROUP BY o.pickupDatetime")
  List<Object[]> countOrdersByShopAndDateRange(
      @Param("shopId") Long shopId,
      @Param("startOfDay") LocalDateTime startOfDay,
      @Param("endOfDay") LocalDateTime endOfDay,
      @Param("excludedStatuses") List<Status> excludedStatuses);

  @Query(
      "SELECT COUNT(o) FROM Order o "
          + "WHERE o.shop.id = :shopId "
          + "AND o.pickupDatetime = :pickupDatetime "
          + "AND o.status NOT IN :excludedStatuses")
  long countByShopAndPickupTime(
      @Param("shopId") Long shopId,
      @Param("pickupDatetime") LocalDateTime pickupDatetime,
      @Param("excludedStatuses") List<Status> excludedStatuses);

  @Query(
      "SELECT o FROM Order o "
          + "JOIN FETCH o.shop "
          + "LEFT JOIN FETCH o.designGallery "
          + "LEFT JOIN FETCH o.orderItems "
          + "LEFT JOIN FETCH o.histories "
          + "WHERE o.id = :orderId")
  Optional<Order> findDetailById(@Param("orderId") Long orderId);
}

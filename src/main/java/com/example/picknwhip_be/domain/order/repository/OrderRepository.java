package com.example.picknwhip_be.domain.order.repository;

import com.example.picknwhip_be.domain.order.entity.Order;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import com.example.picknwhip_be.domain.order.entity.enums.Status;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface OrderRepository extends JpaRepository<Order, Long> {
    @Query(
            "SELECT o.pickupDatetime, COUNT(o) FROM Order o "
                    + "WHERE o.shop.id = :shopId "
                    + "AND o.pickupDatetime BETWEEN :startOfDay AND :endOfDay "
                    + "AND o.status != 'IMPOSSIBLE' "
                    + "GROUP BY o.pickupDatetime")
    List<Object[]> countOrdersByShopAndDateRange(
            @Param("shopId") Long shopId,
            @Param("startOfDay") LocalDateTime startOfDay,
            @Param("endOfDay") LocalDateTime endOfDay);

    @Query(
            "SELECT COUNT(o) FROM Order o "
                    + "WHERE o.shop.id = :shopId "
                    + "AND o.pickupDatetime = :pickupDatetime "
                    + "AND o.status != :impossibleStatus")
    long countByShopAndPickupTime(
            @Param("shopId") Long shopId,
            @Param("pickupDatetime") LocalDateTime pickupDatetime,
            @Param("impossibleStatus") Status impossibleStatus);
}

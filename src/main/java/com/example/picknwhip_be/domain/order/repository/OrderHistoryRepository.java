package com.example.picknwhip_be.domain.order.repository;

import com.example.picknwhip_be.domain.order.entity.OrderHistory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderHistoryRepository extends JpaRepository<OrderHistory, Long> {}

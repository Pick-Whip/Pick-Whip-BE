package com.example.picknwhip_be.domain.order.repository;

import com.example.picknwhip_be.domain.custom.entity.OrderDraftItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderDraftItemRepository extends JpaRepository<OrderDraftItem, Long> {}

package com.example.picknwhip_be.domain.order.repository;

import com.example.picknwhip_be.domain.order.entity.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface OrderRepositoryCustom {
    Page<Order> findOrdersByMemberAndType(Long userId, String type, Pageable pageable);
}
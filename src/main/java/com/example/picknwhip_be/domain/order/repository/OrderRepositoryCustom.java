package com.example.picknwhip_be.domain.order.repository;

import com.example.picknwhip_be.domain.order.dto.req.OrderCursorReqDTO;
import com.example.picknwhip_be.domain.order.entity.Order;
import java.util.List;

public interface OrderRepositoryCustom {
    List<Order> findAllByCursor(Long userId, OrderCursorReqDTO reqDTO);
}
package com.example.picknwhip_be.domain.order.service.query;

import com.example.picknwhip_be.domain.order.dto.res.OrderHistoryResDTO;
import com.example.picknwhip_be.domain.order.entity.Order;
import com.example.picknwhip_be.domain.order.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class OrderQueryServiceImpl implements OrderQueryService {

    private final OrderRepository orderRepository;

    @Override
    public Page<OrderHistoryResDTO> getOrderHistory(Long userId, String type, Pageable pageable) {
        Page<Order> orders = orderRepository.findOrdersByMemberAndType(userId, type, pageable);
        return orders.map(OrderHistoryResDTO::new);
    }
}
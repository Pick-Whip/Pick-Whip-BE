package com.example.picknwhip_be.domain.order.service.query;

import com.example.picknwhip_be.domain.order.dto.req.OrderCursorReqDTO;
import com.example.picknwhip_be.domain.order.dto.res.OrderHistoryResDTO;
import com.example.picknwhip_be.domain.order.entity.Order;
import com.example.picknwhip_be.domain.order.entity.enums.Status;
import com.example.picknwhip_be.domain.order.exception.OrderException;
import com.example.picknwhip_be.domain.order.exception.code.OrderErrorCode;
import com.example.picknwhip_be.domain.order.repository.OrderRepository;
import com.example.picknwhip_be.global.common.CursorResult;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class OrderQueryServiceImpl implements OrderQueryService {

    private final OrderRepository orderRepository;

    @Override
    public CursorResult<OrderHistoryResDTO> getOrderHistory(Long userId, OrderCursorReqDTO req) {
        validateOrderType(req.getType());

        List<Order> orders = orderRepository.findAllByCursor(userId, req);

        boolean hasNext = false;
        if (orders.size() > req.getLimit()) {
            hasNext = true;
            orders.remove(req.getLimit());
        }

        List<OrderHistoryResDTO> dtos = orders.stream()
                .map(OrderHistoryResDTO::new)
                .collect(Collectors.toList());

        CursorResult.Cursor nextCursor = null;
        if (!orders.isEmpty()) {
            Order lastOrder = orders.get(orders.size() - 1);
            int score = convertStatusToScore(lastOrder.getStatus());

            nextCursor = new CursorResult.Cursor(
                    score,
                    lastOrder.getPickupDatetime().toString(),
                    lastOrder.getId()
            );
        }

        return new CursorResult<>(dtos, hasNext, nextCursor);
    }

    private void validateOrderType(String type) {
        if (!"REQUEST".equalsIgnoreCase(type) && !"COMPLETE".equalsIgnoreCase(type)) {
            throw new OrderException(OrderErrorCode.INVALID_ORDER_HISTORY_TYPE);
        }
    }

    private int convertStatusToScore(Status status) {
        switch (status) {
            case CONFIRM_WAIT: return 10;
            case PROD_CONFIRM: return 20;
            case IMPOSSIBLE: return 30;
            case MAKING: return 40;
            case PICKUP_WAIT: return 50;
            case COMPLETED: return 60;
            default: return 99;
        }
    }
}
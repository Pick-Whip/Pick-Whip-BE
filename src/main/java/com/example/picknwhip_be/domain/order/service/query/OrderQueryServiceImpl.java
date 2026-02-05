package com.example.picknwhip_be.domain.order.service.query;

import com.example.picknwhip_be.domain.order.cursor.CursorResult;
import com.example.picknwhip_be.domain.order.dto.req.OrderCursorReqDTO;
import com.example.picknwhip_be.domain.order.dto.res.OrderHistoryResDTO;
import com.example.picknwhip_be.domain.order.entity.Order;
import com.example.picknwhip_be.domain.order.entity.enums.PaymentStatus;
import com.example.picknwhip_be.domain.order.entity.enums.Status;
import com.example.picknwhip_be.domain.order.exception.OrderException;
import com.example.picknwhip_be.domain.order.exception.code.OrderErrorCode;
import com.example.picknwhip_be.domain.order.repository.OrderRepository;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class OrderQueryServiceImpl implements OrderQueryService {

  private final OrderRepository orderRepository;
  private static final int MAX_LIMIT = 50;

  @Override
  public CursorResult<OrderHistoryResDTO> getOrderHistory(Long userId, OrderCursorReqDTO req) {
    validateRequest(req);
    List<Order> orders = orderRepository.findAllByCursor(userId, req);

    boolean hasNext = false;
    if (orders.size() > req.getLimit()) {
      hasNext = true;
      orders.remove(req.getLimit());
    }

    List<OrderHistoryResDTO> dtos =
        orders.stream().map(OrderHistoryResDTO::new).collect(Collectors.toList());

    CursorResult.Cursor nextCursor = null;
    if (!orders.isEmpty()) {
      Order lastOrder = orders.get(orders.size() - 1);
      int score = calculateStatusScore(lastOrder);

      nextCursor =
          new CursorResult.Cursor(
              score,
              lastOrder.getPickupDatetime().toString(), // ISO-8601 포맷
              lastOrder.getId());
    }

    return new CursorResult<>(dtos, hasNext, nextCursor);
  }

  private void validateRequest(OrderCursorReqDTO req) {
    if (!"REQUEST".equalsIgnoreCase(req.getType()) && !"COMPLETE".equalsIgnoreCase(req.getType())) {
      throw new OrderException(OrderErrorCode.INVALID_ORDER_HISTORY_TYPE);
    }
    if (req.getLimit() > MAX_LIMIT) {
      req.setLimit(MAX_LIMIT);
    }
    if (req.getLimit() < 1) {
      req.setLimit(10);
    }
    if (req.getLastOrderId() != null
        && (req.getLastStatusScore() == null || req.getLastPickupDatetime() == null)) {
      throw new OrderException(OrderErrorCode.INVALID_CURSOR_PARAMS);
    }
  }

  private int calculateStatusScore(Order order) {
    Status status = order.getStatus();
    PaymentStatus paymentStatus = order.getPaymentStatus();

    switch (status) {
      case CONFIRM_WAIT:
        return 10;
      case PROD_CONFIRM:
        if (paymentStatus == PaymentStatus.WAITING) {
          return 20;
        } else {
          return 30;
        }
      case IMPOSSIBLE:
        return 30;
      case MAKING:
        return 40;
      case PICKUP_WAIT:
        return 50;
      case COMPLETED:
        return 60;
      default:
        return 99;
    }
  }
}

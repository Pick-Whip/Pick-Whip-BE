package com.example.picknwhip_be.domain.order.service.command;

import com.example.picknwhip_be.domain.order.dto.req.OrderReqDTO;
import com.example.picknwhip_be.domain.order.dto.res.OrderResDTO;

public interface OrderCommandService {
  OrderResDTO.OrderCompleteDTO createOrder(Long userId, OrderReqDTO.CreateOrderDTO dto);

  void acceptOrder(Long userId, Long orderId);

  void rejectOrder(Long userId, Long orderId, String reason);

  void handlePaymentSuccess(Long orderId);

  void handlePaymentFailure(Long orderId);
}

package com.example.picknwhip_be.domain.order.service.command;

import com.example.picknwhip_be.domain.order.dto.req.OrderReqDTO;
import com.example.picknwhip_be.domain.order.dto.res.OrderResDTO;

public interface OrderCommandService {
  OrderResDTO.OrderCompleteDTO createOrder(Long userId, OrderReqDTO.CreateOrderDTO dto);
}

package com.example.picknwhip_be.domain.order.service.query;

import com.example.picknwhip_be.domain.order.cursor.CursorResult;
import com.example.picknwhip_be.domain.order.dto.req.OrderCursorReqDTO;
import com.example.picknwhip_be.domain.order.dto.res.OrderHistoryResDTO;

public interface OrderQueryService {
  CursorResult<OrderHistoryResDTO> getOrderHistory(Long userId, OrderCursorReqDTO req);
}

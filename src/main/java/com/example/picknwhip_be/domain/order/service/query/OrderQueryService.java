package com.example.picknwhip_be.domain.order.service.query;

import com.example.picknwhip_be.domain.order.dto.res.OrderHistoryResDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface OrderQueryService {
    Page<OrderHistoryResDTO> getOrderHistory(Long userId, String type, Pageable pageable);
}
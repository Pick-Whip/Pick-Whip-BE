package com.example.picknwhip_be.domain.order.dto.res;

import com.example.picknwhip_be.domain.order.entity.Order;
import java.time.LocalDateTime;
import lombok.*;

public class OrderResDTO {

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class OrderCompleteDTO {
        private Long orderId;
        private String orderCode;
        private String shopName;
        private LocalDateTime pickupDatetime;
        private int totalPrice;
    }

    public static OrderCompleteDTO from(Order order) {
        return OrderCompleteDTO.builder()
                .orderId(order.getId())
                .orderCode(order.getOrderCode())
                .shopName(order.getShop().getShopName())
                .pickupDatetime(order.getPickupDatetime())
                .totalPrice(order.getTotalPrice())
                .build();
    }
}
package com.example.picknwhip_be.domain.order.entity.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Status {
    CONFIRM_WAIT("주문서 확인 중"),
    PAYMENT_WAIT("결제 요청 중"),
    CANCELED_BY_SHOP("제작 불가"),
    PAYMENT_FAILED("제작 불가"),
    PROD_CONFIRM("제작 확정"),
    MAKING("제작 중"),
    PICKUP_WAIT("픽업 대기 중"),
    COMPLETED("픽업 완료");

    private final String description;
}
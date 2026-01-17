package com.example.picknwhip_be.domain.payment.entity.enums;

public enum PaymentStatus {
    READY,              // 결제 요청 중
    DONE,               // 승인 완료
    CANCELED,           // 전체 취소
    PARTIAL_CANCELED,   // 부분 취소
    ABORTED             // 결제 실패/중단
}
package com.example.picknwhip_be.domain.order.dto.res;

import com.example.picknwhip_be.domain.order.entity.Order;
import com.example.picknwhip_be.domain.order.entity.OrderItem;
import com.example.picknwhip_be.domain.order.entity.enums.PaymentStatus;
import com.example.picknwhip_be.domain.order.entity.enums.Status;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@NoArgsConstructor
public class OrderHistoryResDTO {

    @Schema(description = "주문 ID")
    private Long orderId;
    private String orderCode;
    private String shopName;
    private String cakeName;
    private String pickupDate;
    private String pickupTime;
    private int totalPrice;
    private String imageUrl;

    @Schema(description = "현재 진행 단계 (1~5)")
    private int currentStep;

    @Schema(description = "단계 상태 (DEFAULT: 정상 / ERROR: 에러상태)")
    private String stepStatus;

    private String topMessage;
    private String bottomMessage;
    private String rejectReason;

    private List<OrderItemResDTO> options;

    public OrderHistoryResDTO(Order order) {
        this.orderId = order.getId();
        this.orderCode = order.getOrderCode();
        this.shopName = order.getShop().getShopName();
        this.pickupDate = order.getPickupDatetime().format(DateTimeFormatter.ofPattern("yyyy.MM.dd"));
        this.pickupTime = order.getPickupDatetime().format(DateTimeFormatter.ofPattern("hh:mm a"));
        this.totalPrice = order.getTotalPrice();
        this.imageUrl = (order.getDesignGallery() != null) ? order.getDesignGallery().getImageUrl() : null;

        if (order.getOrderItems() != null && !order.getOrderItems().isEmpty()) {
            this.cakeName = order.getOrderItems().get(0).getOptionName();
            this.options = order.getOrderItems().stream()
                    .map(OrderItemResDTO::new)
                    .collect(Collectors.toList());
        } else {
            this.options = new ArrayList<>(); // null 방지
            this.cakeName = "옵션 정보 없음";
        }

        mapStatusToUi(order);
    }

    private void mapStatusToUi(Order order) {
        Status status = order.getStatus();
        PaymentStatus paymentStatus = order.getPaymentStatus();

        this.stepStatus = "DEFAULT";
        this.rejectReason = null;

        switch (status) {
            case CONFIRM_WAIT:
                this.currentStep = 1;
                this.topMessage = "주문서 확인 중";
                this.bottomMessage = "주문서 확인 중";
                break;

            case PROD_CONFIRM:
                if (paymentStatus == PaymentStatus.CANCELLED) {
                    this.currentStep = 3;
                    this.stepStatus = "ERROR";
                    this.topMessage = "제작 불가";
                    this.bottomMessage = "결제 미완료 상태입니다";
                } else {
                    this.currentStep = 2;
                    this.topMessage = "결제 요청 중";
                    this.bottomMessage = "결제 요청 중";
                }

            case IMPOSSIBLE:
                this.currentStep = 3;
                this.stepStatus = "ERROR";
                this.topMessage = "제작 불가";
                this.bottomMessage = "사장님 메세지를 확인해주세요";
                this.rejectReason = order.getRejectionReason();
                break;

            case MAKING:
                this.currentStep = 3;
                this.topMessage = "제작 중";
                this.bottomMessage = "제작 중";
                break;

            case PICKUP_WAIT:
                this.currentStep = 4;
                this.topMessage = "픽업 대기 중";
                this.bottomMessage = "픽업 대기 중";
                break;

            case COMPLETED:
                this.currentStep = 5;
                this.topMessage = "픽업 완료";
                this.bottomMessage = "픽업 완료";
                break;

            default:
                this.currentStep = 1;
                this.topMessage = "확인 중";
                this.bottomMessage = "확인 중";
        }
    }

    @Getter
    @NoArgsConstructor
    public static class OrderItemResDTO {
        private String category;
        private String name;
        private int price;

        public OrderItemResDTO(OrderItem item) {
            this.category = item.getOptionCategory().name();
            this.name = item.getOptionName();
            this.price = item.getUnitPrice();
        }
    }
}
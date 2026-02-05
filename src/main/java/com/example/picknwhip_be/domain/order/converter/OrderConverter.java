package com.example.picknwhip_be.domain.order.converter;

import com.example.picknwhip_be.domain.order.dto.res.OrderDetailResDTO;
import com.example.picknwhip_be.domain.order.entity.Order;
import com.example.picknwhip_be.domain.order.entity.OrderHistory;
import com.example.picknwhip_be.domain.order.entity.OrderItem;
import com.example.picknwhip_be.domain.order.entity.enums.Status;
import com.example.picknwhip_be.domain.shop.entity.enums.OptionCategory;
import org.springframework.stereotype.Component;

import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Component
public class OrderConverter {

    private static final DateTimeFormatter DATE_FMT = DateTimeFormatter.ofPattern("yyyy.MM.dd (E)", Locale.KOREAN);
    private static final DateTimeFormatter TIME_FMT = DateTimeFormatter.ofPattern("yyyy.MM.dd HH:mm");

    public OrderDetailResDTO toDetailDto(Order order) {
        return OrderDetailResDTO.builder()
                .statusInfo(buildStatusInfo(order))
                .shopInfo(buildShopInfo(order))
                .pickupInfo(buildPickupInfo(order))
                .productInfo(buildProductInfo(order))
                .orderCode(order.getOrderCode())
                .timeline(buildTimeline(order))
                .build();
    }

    private OrderDetailResDTO.StatusInfo buildStatusInfo(Order order) {
        Status status = order.getStatus();
        String topMsg = "";
        String bottomMsg = "";
        boolean isImpossible = (status == Status.IMPOSSIBLE);

        switch (status) {
            case CONFIRM_WAIT:
                topMsg = "주문서 확인 중"; bottomMsg = "주문서 확인 중"; break;
            case PROD_CONFIRM:
                topMsg = "결제 요청 중"; bottomMsg = "결제 요청 중"; break;
            case IMPOSSIBLE:
                topMsg = "제작 불가";
                bottomMsg = (order.getRejectionReason() != null) ? "사장님 메세지를 확인해주세요" : "결제 미완료 상태입니다";
                break;
            case MAKING:
                topMsg = "제작 중"; bottomMsg = "제작 중"; break;
            case PICKUP_WAIT:
                topMsg = "픽업 대기 중"; bottomMsg = "픽업 대기 중"; break;
            case COMPLETED:
                topMsg = "픽업 완료"; bottomMsg = "픽업 완료"; break;
            default:
                topMsg = "확인 중"; bottomMsg = "확인 중";
        }

        return OrderDetailResDTO.StatusInfo.builder()
                .status(status)
                .topMessage(topMsg)
                .bottomMessage(bottomMsg)
                .rejectReason(isImpossible ? order.getRejectionReason() : null)
                .isImpossible(isImpossible)
                .build();
    }

    private OrderDetailResDTO.ShopInfo buildShopInfo(Order order) {
        String productName = (order.getDesignGallery() != null)
                ? order.getDesignGallery().getDesignName()
                : "자유 디자인";

        return OrderDetailResDTO.ShopInfo.builder()
                .shopId(order.getShop().getId())
                .shopName(order.getShop().getShopName())
                .productName(productName)
                .build();
    }

    private OrderDetailResDTO.PickupInfo buildPickupInfo(Order order) {
        String startTime = order.getPickupDatetime().toLocalTime().toString();

        return OrderDetailResDTO.PickupInfo.builder()
                .pickupDate(order.getPickupDatetime().format(DATE_FMT))
                .pickupTime(startTime)
                .build();
    }

    private OrderDetailResDTO.ProductInfo buildProductInfo(Order order) {
        List<String> flavors = order.getOrderItems().stream()
                .filter(i -> i.getOptionCategory() == OptionCategory.SHEET || i.getOptionCategory() == OptionCategory.CREAM)
                .map(OrderItem::getOptionName)
                .collect(Collectors.toList());

        String deco = order.getOrderItems().stream()
                .filter(i -> i.getOptionCategory() == OptionCategory.TOPPING || i.getOptionCategory() == OptionCategory.ICING) // 카테고리 확인 필요
                .map(OrderItem::getOptionName)
                .collect(Collectors.joining(", "));

        String lettering = String.format("%s\n%s + %s",
                order.getLetteringText() != null ? order.getLetteringText() : "",
                order.getLetteringAlignment(),
                order.getLetteringLineCount());

        return OrderDetailResDTO.ProductInfo.builder()
                .imageUrl(order.getReferenceImageUrl())
                .designName(order.getDesignGallery() != null ? order.getDesignGallery().getDesignName() : "1호 원형") // 임시 로직
                .flavor(flavors.isEmpty() ? "-" : String.join(" + ", flavors))
                .lettering(lettering)
                .deco(deco.isEmpty() ? "-" : deco)
                .additionalRequest(order.getOrderAdditionalRequest())
                .build();
    }

    private List<OrderDetailResDTO.TimelineItem> buildTimeline(Order order) {
        if (order.getStatus() != Status.IMPOSSIBLE) {
            return null;
        }

        List<OrderHistory> histories = order.getHistories();
        List<OrderDetailResDTO.TimelineItem> timeline = new ArrayList<>();

        timeline.add(createTimelineItem("주문서 작성", histories, Status.CONFIRM_WAIT, false));
        timeline.add(createTimelineItem("사장님 확인", histories, Status.PROD_CONFIRM, false));
        timeline.add(createTimelineItem("제작 불가", histories, Status.IMPOSSIBLE, true));

        return timeline;
    }

    private OrderDetailResDTO.TimelineItem createTimelineItem(String name, List<OrderHistory> histories, Status targetStatus, boolean isReject) {
        Optional<OrderHistory> history = histories.stream()
                .filter(h -> h.getStatus() == targetStatus)
                .findFirst();

        return OrderDetailResDTO.TimelineItem.builder()
                .stepName(name)
                .time(history.map(h -> h.getCreatedAt().format(TIME_FMT)).orElse("-"))
                .isCompleted(history.isPresent())
                .isReject(isReject)
                .build();
    }
}
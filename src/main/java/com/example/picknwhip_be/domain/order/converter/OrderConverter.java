package com.example.picknwhip_be.domain.order.converter;

import com.example.picknwhip_be.domain.order.dto.res.OrderDetailResDTO;
import com.example.picknwhip_be.domain.order.entity.Order;
import com.example.picknwhip_be.domain.order.entity.OrderHistory;
import com.example.picknwhip_be.domain.order.entity.OrderItem;
import com.example.picknwhip_be.domain.order.entity.enums.LetteringAlignment;
import com.example.picknwhip_be.domain.order.entity.enums.LetteringLineCount;
import com.example.picknwhip_be.domain.order.entity.enums.Status;
import com.example.picknwhip_be.domain.shop.entity.enums.OptionCategory;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;
import org.springframework.stereotype.Component;

@Component
public class OrderConverter {

  private static final DateTimeFormatter DATE_FMT =
      DateTimeFormatter.ofPattern("yyyy.MM.dd (E)", Locale.KOREAN);
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
    String topMsg;
    String bottomMsg;
    boolean isImpossible = (status == Status.CANCELED_BY_SHOP || status == Status.PAYMENT_FAILED);
    String rejectReason = null;

    switch (status) {
      case CONFIRM_WAIT -> {
        topMsg = "주문서 확인 중";
        bottomMsg = "주문서 확인 중";
      }
      case PAYMENT_WAIT -> {
        topMsg = "결제 요청 중";
        bottomMsg = "결제 요청 중";
      }
      case CANCELED_BY_SHOP -> {
        topMsg = "제작 불가";
        bottomMsg = "사장님 메세지를 확인해주세요";
      }
      case PAYMENT_FAILED -> {
        topMsg = "제작 불가";
        bottomMsg = "결제 미완료 상태입니다";
      }
      case PROD_CONFIRM -> {
        topMsg = "제작 확정";
        bottomMsg = "제작 확정";
      }
      case MAKING -> {
        topMsg = "제작 중";
        bottomMsg = "제작 중";
      }
      case PICKUP_WAIT -> {
        topMsg = "픽업 대기 중";
        bottomMsg = "픽업 대기 중";
      }
      case COMPLETED -> {
        topMsg = "픽업 완료";
        bottomMsg = "픽업 완료";
      }
      default -> {
        topMsg = "확인 중";
        bottomMsg = "확인 중";
      }
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
    String productName =
        (order.getDesignGallery() != null) ? order.getDesignGallery().getDesignName() : "자유 디자인";

    return OrderDetailResDTO.ShopInfo.builder()
        .shopId(order.getShop().getId())
        .shopName(order.getShop().getShopName())
        .productName(productName)
        .build();
  }

  private OrderDetailResDTO.PickupInfo buildPickupInfo(Order order) {
    String startTime = order.getPickupDatetime().toLocalTime().toString();
    if (startTime.length() > 5) {
      startTime = startTime.substring(0, 5);
    }

    return OrderDetailResDTO.PickupInfo.builder()
        .pickupDate(order.getPickupDatetime().format(DATE_FMT))
        .pickupTime(startTime)
        .build();
  }

  private OrderDetailResDTO.ProductInfo buildProductInfo(Order order) {
    String flavors =
        order.getOrderItems().stream()
            .filter(
                i ->
                    i.getOptionCategory() == OptionCategory.SHEET
                        || i.getOptionCategory() == OptionCategory.CREAM)
            .map(OrderItem::getOptionName)
            .distinct()
            .collect(Collectors.joining(" + "));

    if (flavors.isEmpty()) flavors = "-";

    String deco =
        order.getOrderItems().stream()
            .filter(
                i ->
                    i.getOptionCategory() == OptionCategory.TOPPING
                        || i.getOptionCategory() == OptionCategory.ICING)
            .map(OrderItem::getOptionName)
            .distinct()
            .collect(Collectors.joining(", "));

    if (deco.isEmpty()) deco = "-";

    String letteringText = order.getLetteringText() != null ? order.getLetteringText() : "";
    String alignDesc = getAlignmentDescription(order.getLetteringAlignment());
    String lineDesc = getLineCountDescription(order.getLetteringLineCount());

    String letteringInfo = String.format("%s\n%s + %s", letteringText, alignDesc, lineDesc).trim();

    return OrderDetailResDTO.ProductInfo.builder()
        .imageUrl(order.getReferenceImageUrl())
        .designName(
            order.getDesignGallery() != null ? order.getDesignGallery().getDesignName() : "1호 원형")
        .flavor(flavors)
        .lettering(letteringInfo)
        .deco(deco)
        .additionalRequest(order.getOrderAdditionalRequest())
        .build();
  }

  private List<OrderDetailResDTO.TimelineItem> buildTimeline(Order order) {
    if (order.getStatus() != Status.CANCELED_BY_SHOP
        && order.getStatus() != Status.PAYMENT_FAILED) {
      return null;
    }

    Collection<OrderHistory> histories = order.getHistories();
    List<OrderDetailResDTO.TimelineItem> timeline = new ArrayList<>();
    timeline.add(createTimelineItemFromHistory("주문서 작성", histories, Status.CONFIRM_WAIT, false));
    Optional<OrderHistory> checkHistory =
        histories.stream().filter(h -> h.getStatus() == Status.PAYMENT_WAIT).findFirst();

    String checkTime;
    if (checkHistory.isPresent()) {
      checkTime = checkHistory.get().getCreatedAt().format(TIME_FMT);
    } else {
      checkTime = order.getUpdatedAt().format(TIME_FMT);
    }
    timeline.add(createItem("사장님 확인", checkTime, true, false));

    String errorTime = order.getUpdatedAt().format(TIME_FMT);
    timeline.add(createItem("제작 불가", errorTime, true, true));

    return timeline;
  }

  private OrderDetailResDTO.TimelineItem createItem(
      String stepName, String time, boolean isCompleted, boolean isReject) {
    return OrderDetailResDTO.TimelineItem.builder()
        .stepName(stepName)
        .time(time)
        .isCompleted(isCompleted)
        .isReject(isReject)
        .build();
  }

  private OrderDetailResDTO.TimelineItem createTimelineItemFromHistory(
      String name, Collection<OrderHistory> histories, Status targetStatus, boolean isReject) {

    Optional<OrderHistory> history =
        histories.stream().filter(h -> h.getStatus() == targetStatus).findFirst();

    String time = history.map(h -> h.getCreatedAt().format(TIME_FMT)).orElse("-");
    boolean isCompleted = history.isPresent();

    return createItem(name, time, isCompleted, isReject);
  }

  private String getAlignmentDescription(LetteringAlignment alignment) {
    if (alignment == null) return "";
    return switch (alignment) {
      case CENTER -> "가운데";
      case CURVE_UP -> "위로 둥글게";
      case CURVE_UP_DOWN -> "위아래 둥글게";
    };
  }

  private String getLineCountDescription(LetteringLineCount lineCount) {
    if (lineCount == null) return "";
    return switch (lineCount) {
      case ONE_LINE -> "1줄";
      case TWO_LINE -> "2줄";
      case THREE_LINE -> "3줄";
    };
  }
}

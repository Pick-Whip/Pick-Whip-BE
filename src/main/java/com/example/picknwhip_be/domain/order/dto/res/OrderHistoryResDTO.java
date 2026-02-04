package com.example.picknwhip_be.domain.order.dto.res;

import com.example.picknwhip_be.domain.order.entity.Order;
import com.example.picknwhip_be.domain.order.entity.OrderItem;
import com.example.picknwhip_be.domain.order.entity.enums.PaymentStatus;
import com.example.picknwhip_be.domain.order.entity.enums.Status;
import com.example.picknwhip_be.domain.shop.entity.enums.OptionCategory;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class OrderHistoryResDTO {

  @Schema(description = "주문 ID")
  private Long orderId;

  private String orderCode;
  private String shopName;
  private String pickupDate;
  private String pickupTime;
  private int totalPrice;
  private String imageUrl;

  private String designName;
  private String flavor;
  private String lettering;
  private String additionalRequest;

  private int currentStep;
  private String stepStatus;
  private String topMessage;
  private String bottomMessage;
  private String rejectReason;

  public OrderHistoryResDTO(Order order) {
    this.orderId = order.getId();
    this.orderCode = order.getOrderCode();
    this.shopName = order.getShop().getShopName();
    this.totalPrice = order.getTotalPrice();
    this.pickupDate = order.getPickupDatetime().format(DateTimeFormatter.ofPattern("yyyy.MM.dd"));
    this.pickupTime =
        order.getPickupDatetime().format(DateTimeFormatter.ofPattern("hh:mm a", Locale.ENGLISH));

    parseDetails(order);
    mapStatusToUi(order);
  }

  private void parseDetails(Order order) {
    this.designName =
        (order.getDesignGallery() != null) ? order.getDesignGallery().getDesignName() : "자유 디자인";
    this.imageUrl =
        (order.getDesignGallery() != null) ? order.getDesignGallery().getImageUrl() : null;
    this.lettering = order.getLetteringText();
    this.additionalRequest = order.getOrderAdditionalRequest();

    List<String> tasteList = new ArrayList<>();
    if (order.getOrderItems() != null) {
      for (OrderItem item : order.getOrderItems()) {
        if (item.getOptionCategory() == OptionCategory.SHEET
            || item.getOptionCategory() == OptionCategory.CREAM) {
          tasteList.add(item.getOptionName());
        }
      }
    }
    if (!tasteList.isEmpty()) {
      this.flavor = String.join(" + ", tasteList);
    } else {
      this.flavor = null;
    }
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
        if (paymentStatus == PaymentStatus.WAITING) {
          this.currentStep = 2;
          this.topMessage = "결제 요청 중";
          this.bottomMessage = "결제 요청 중";
        } else {
          this.currentStep = 3;
          this.stepStatus = "ERROR";
          this.topMessage = "제작 불가";
          this.bottomMessage = "결제 미완료 상태입니다";
          this.rejectReason = null;
        }
        break;

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
}

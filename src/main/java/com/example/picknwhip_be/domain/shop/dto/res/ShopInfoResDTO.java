package com.example.picknwhip_be.domain.shop.dto.res;

import com.fasterxml.jackson.annotation.JsonInclude;
import java.util.List;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ShopInfoResDTO {

  private List<PriceGuideDTO> priceGuides;

  private List<SizeGuideDTO> sizeGuides;

  private PickupInfoDTO pickupInfo;

  private PaymentInfoDTO paymentInfo;

  private List<String> precautionNotices;

  private EventDTO ongoingEvent;

  @Getter
  @Builder
  public static class PriceGuideDTO {
    private String sizeName;
    private String priceText;
  }

  @Getter
  @Builder
  public static class SizeGuideDTO {
    private String sizeName;
    private String diameter;
  }

  @Getter
  @Builder
  public static class PickupInfoDTO {
    private String operationHours;
    private String pickupNotice;
    private String sameDayOrder;
    private String parkingInfo;
  }

  @Getter
  @Builder
  public static class PaymentInfoDTO {
    private List<String> paymentMethods;
    private String paymentNotice;
    private String prepaymentInfo;
  }

  @Getter
  @Builder
  public static class EventDTO {
    private String title;
    private String content;
    private String period;
  }
}

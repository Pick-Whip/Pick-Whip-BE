package com.example.picknwhip_be.domain.order.dto.res;

import com.example.picknwhip_be.domain.order.entity.enums.Status;
import java.util.List;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class OrderDetailResDTO {
  private StatusInfo statusInfo;

  private ShopInfo shopInfo;

  private PickupInfo pickupInfo;

  private ProductInfo productInfo;

  private String orderCode;

  private List<TimelineItem> timeline;

  @Getter
  @Builder
  public static class StatusInfo {
    private Status status;
    private String topMessage;
    private String bottomMessage;
    private String rejectReason;
    private boolean isImpossible;
  }

  @Getter
  @Builder
  public static class ShopInfo {
    private Long shopId;
    private String shopName;
    private String productName;
  }

  @Getter
  @Builder
  public static class PickupInfo {
    private String pickupDate;
    private String pickupTime;
  }

  @Getter
  @Builder
  public static class ProductInfo {
    private String imageUrl;
    private String designName;
    private String flavor;
    private String lettering;
    private String deco;
    private String additionalRequest;
  }

  @Getter
  @Builder
  public static class TimelineItem {
    private String stepName;
    private String time;
    private boolean isCompleted;
    private boolean isReject;
  }
}

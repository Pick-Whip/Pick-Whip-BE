package com.example.picknwhip_be.domain.shop.converter;

import com.example.picknwhip_be.domain.payment.entity.enums.PaymentMethod;
import com.example.picknwhip_be.domain.shop.constant.ShopInfoConstants;
import com.example.picknwhip_be.domain.shop.dto.res.ShopInfoResDTO;
import com.example.picknwhip_be.domain.shop.entity.*;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;
import org.springframework.stereotype.Component;

@Component
public class ShopInfoConverter {

  private static final DateTimeFormatter DATE_FMT = DateTimeFormatter.ofPattern("yyyy.MM.dd");
  private static final DateTimeFormatter TIME_FMT = DateTimeFormatter.ofPattern("HH:mm");

  public ShopInfoResDTO toInfoResDTO(
      Shop shop,
      List<ShopCakeSize> sizes,
      List<ShopBusinessHour> hours,
      boolean hasBankAccount,
      ShopEvent activeEvent) {
    return ShopInfoResDTO.builder()
        .priceGuides(toPriceGuides(sizes))
        .sizeGuides(toSizeGuides(sizes))
        .pickupInfo(toPickupInfo(shop, hours))
        .paymentInfo(toPaymentInfo(shop, hasBankAccount))
        .precautionNotices(parsePrecaution(shop.getPrecautionNotice()))
        .ongoingEvent(toEventDTO(activeEvent))
        .build();
  }

  private List<ShopInfoResDTO.PriceGuideDTO> toPriceGuides(List<ShopCakeSize> sizes) {
    if (sizes.isEmpty()) return new ArrayList<>();

    List<ShopCakeSize> sortedSizes =
        sizes.stream().sorted(Comparator.comparingInt(ShopCakeSize::getPrice)).toList();

    int basePrice = sortedSizes.get(0).getPrice(); // 기준가

    return sortedSizes.stream()
        .map(
            size -> {
              String priceText;
              if (size.getPrice() == basePrice) {
                priceText = "디자인 기준가 적용";
              } else {
                int diff = size.getPrice() - basePrice;
                priceText = String.format("+%,d원", diff);
              }
              return ShopInfoResDTO.PriceGuideDTO.builder()
                  .sizeName(size.getSizeName())
                  .priceText(priceText)
                  .build();
            })
        .collect(Collectors.toList());
  }

  private List<ShopInfoResDTO.SizeGuideDTO> toSizeGuides(List<ShopCakeSize> sizes) {
    return sizes.stream()
        .sorted(Comparator.comparingInt(ShopCakeSize::getPrice))
        .map(
            size ->
                ShopInfoResDTO.SizeGuideDTO.builder()
                    .sizeName(size.getSizeName())
                    .diameter(size.getDiameter())
                    .build())
        .toList();
  }

  private ShopInfoResDTO.PickupInfoDTO toPickupInfo(Shop shop, List<ShopBusinessHour> hours) {
    return ShopInfoResDTO.PickupInfoDTO.builder()
        .operationHours(formatOperationHours(hours))
        .pickupNotice(shop.getPickupTimeGuide())
        .sameDayOrder(shop.getDayOrderGuide())
        .parkingInfo(shop.getParkingGuide())
        .build();
  }

  private String formatOperationHours(List<ShopBusinessHour> hours) {
    if (hours == null || hours.isEmpty()) return ShopInfoConstants.NO_OPERATION_HOURS;

    Map<String, List<Integer>> timeGroup = new LinkedHashMap<>();

    hours.stream()
        .filter(h -> h.getDayOfWeek() != null)
        .sorted(Comparator.comparingInt(ShopBusinessHour::getDayOfWeek))
        .forEach(
            h -> {
              if (h.isClosed()) return;

              if (h.getOpenTime() == null || h.getCloseTime() == null) return;

              String timeStr =
                  String.format(
                      "%s - %s",
                      h.getOpenTime().format(TIME_FMT), h.getCloseTime().format(TIME_FMT));

              timeGroup.computeIfAbsent(timeStr, k -> new ArrayList<>()).add(h.getDayOfWeek());
            });

    if (timeGroup.isEmpty()) return ShopInfoConstants.CLOSED_DAY;

    List<String> resultLines = new ArrayList<>();

    for (Map.Entry<String, List<Integer>> entry : timeGroup.entrySet()) {
      String timeRange = entry.getKey();
      List<Integer> days = entry.getValue();
      String dayStr = formatDays(days);

      resultLines.add(dayStr + " " + timeRange);
    }

    return String.join("\n", resultLines);
  }

  private String formatDays(List<Integer> days) {
    if (days.size() == 7) return "매일";

    boolean isConsecutive = true;
    for (int i = 0; i < days.size() - 1; i++) {
      if (days.get(i) + 1 != days.get(i + 1)) {
        isConsecutive = false;
        break;
      }
    }

    if (isConsecutive && days.size() >= 3) {
      return getDayName(days.get(0)) + "~" + getDayName(days.get(days.size() - 1));
    } else {
      return days.stream().map(this::getDayName).collect(Collectors.joining(","));
    }
  }

  private String getDayName(int dayOfWeek) {
    return switch (dayOfWeek) {
      case 1 -> "월";
      case 2 -> "화";
      case 3 -> "수";
      case 4 -> "목";
      case 5 -> "금";
      case 6 -> "토";
      case 7 -> "일";
      default -> "";
    };
  }

  private ShopInfoResDTO.PaymentInfoDTO toPaymentInfo(Shop shop, boolean hasBankAccount) {
    List<String> methods = new ArrayList<>();
    methods.add(PaymentMethod.CARD.name());
    if (hasBankAccount) {
      methods.add(PaymentMethod.TRANSFER.name());
    }

    String prepaymentStr = null;
    if (shop.getPrepayment() != null && shop.getPrepayment() > 0) {
      prepaymentStr = String.format("%,d원", shop.getPrepayment());
    }

    return ShopInfoResDTO.PaymentInfoDTO.builder()
        .paymentMethods(methods)
        .paymentNotice(shop.getPaymentNotice())
        .prepaymentInfo(prepaymentStr)
        .build();
  }

  private List<String> parsePrecaution(String notice) {
    if (notice == null || notice.isBlank()) return new ArrayList<>();
    return Arrays.stream(notice.split("\n")).map(String::trim).filter(s -> !s.isEmpty()).toList();
  }

  private ShopInfoResDTO.EventDTO toEventDTO(ShopEvent event) {
    if (event == null) return null;
    return ShopInfoResDTO.EventDTO.builder()
        .title(event.getTitle())
        .content(event.getContent())
        .period(event.getStartDate().format(DATE_FMT) + " - " + event.getEndDate().format(DATE_FMT))
        .build();
  }
}

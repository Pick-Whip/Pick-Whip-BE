package com.example.picknwhip_be.domain.shop.converter;

import com.example.picknwhip_be.domain.shop.dto.res.ShopInfoResDTO;
import com.example.picknwhip_be.domain.shop.entity.*;
import org.springframework.stereotype.Component;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class ShopInfoConverter {

    private static final DateTimeFormatter DATE_FMT = DateTimeFormatter.ofPattern("yyyy.MM.dd");
    private static final DateTimeFormatter TIME_FMT = DateTimeFormatter.ofPattern("HH:mm");

    public ShopInfoResDTO toInfoResDTO(
            Shop shop,
            List<ShopCakeSize> sizes,
            List<ShopBusinessHour> hours,
            boolean hasBankAccount,
            ShopEvent activeEvent
    ) {
        return ShopInfoResDTO.builder()
                .priceGuides(toPriceGuides(sizes))
                .priceNote("디자인 및 토핑에 따라 가격이 변동될 수 있습니다.")
                .sizeGuides(toSizeGuides(sizes))
                .pickupInfo(toPickupInfo(shop, hours))
                .paymentInfo(toPaymentInfo(shop, hasBankAccount))
                .precautionNotices(parsePrecaution(shop.getPrecautionNotice()))
                .ongoingEvent(toEventDTO(activeEvent))
                .build();
    }

    private List<ShopInfoResDTO.PriceGuideDTO> toPriceGuides(List<ShopCakeSize> sizes) {
        if (sizes.isEmpty()) return new ArrayList<>();

        List<ShopCakeSize> sortedSizes = sizes.stream()
                .sorted(Comparator.comparingInt(ShopCakeSize::getPrice))
                .toList();

        int basePrice = sortedSizes.get(0).getPrice(); // 기준가

        return sortedSizes.stream().map(size -> {
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
        }).collect(Collectors.toList());
    }

    private List<ShopInfoResDTO.SizeGuideDTO> toSizeGuides(List<ShopCakeSize> sizes) {
        return sizes.stream()
                .sorted(Comparator.comparingInt(ShopCakeSize::getPrice))
                .map(size -> ShopInfoResDTO.SizeGuideDTO.builder()
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
        if (hours == null || hours.isEmpty()) return "운영 시간 정보 없음";
        ShopBusinessHour hour = hours.get(0);
        return String.format("매일 %s - %s",
                hour.getOpenTime().format(TIME_FMT),
                hour.getCloseTime().format(TIME_FMT));
    }

    private ShopInfoResDTO.PaymentInfoDTO toPaymentInfo(Shop shop, boolean hasBankAccount) {
        List<String> methods = new ArrayList<>();
        methods.add("CARD"); // 기본 지원
        if (hasBankAccount) {
            methods.add("TRANSFER");
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
        return Arrays.stream(notice.split("\n"))
                .map(String::trim)
                .filter(s -> !s.isEmpty())
                .toList();
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
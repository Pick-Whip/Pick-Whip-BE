package com.example.picknwhip_be.domain.custom.service.command;

import com.example.picknwhip_be.domain.custom.dto.req.CustomReqDTO;
import com.example.picknwhip_be.domain.custom.dto.res.CustomResDTO;
import com.example.picknwhip_be.domain.custom.entity.OrderDraft;
import com.example.picknwhip_be.domain.custom.entity.OrderDraftItem;
import com.example.picknwhip_be.domain.custom.exception.CustomException;
import com.example.picknwhip_be.domain.custom.exception.code.CustomErrorCode;
import com.example.picknwhip_be.domain.custom.repository.OrderDraftRepository;
import com.example.picknwhip_be.domain.design.entity.DesignGallery;
import com.example.picknwhip_be.domain.design.exception.DesignException;
import com.example.picknwhip_be.domain.design.exception.code.DesignErrorCode;
import com.example.picknwhip_be.domain.design.repository.DesignGalleryRepository;
import com.example.picknwhip_be.domain.order.entity.enums.Status;
import com.example.picknwhip_be.domain.order.exception.OrderException;
import com.example.picknwhip_be.domain.order.exception.code.OrderErrorCode;
import com.example.picknwhip_be.domain.order.repository.OrderDraftItemRepository;
import com.example.picknwhip_be.domain.order.repository.OrderRepository;
import com.example.picknwhip_be.domain.shop.entity.CustomOption;
import com.example.picknwhip_be.domain.shop.entity.Shop;
import com.example.picknwhip_be.domain.shop.entity.ShopBusinessHour;
import com.example.picknwhip_be.domain.shop.entity.ShopCakeSize;
import com.example.picknwhip_be.domain.shop.entity.enums.ScheduleType;
import com.example.picknwhip_be.domain.shop.exception.ShopException;
import com.example.picknwhip_be.domain.shop.exception.code.ShopErrorCode;
import com.example.picknwhip_be.domain.shop.repository.CustomOptionRepository;
import com.example.picknwhip_be.domain.shop.repository.ShopBusinessHourRepository;
import com.example.picknwhip_be.domain.shop.repository.ShopCakeSizeRepository;
import com.example.picknwhip_be.domain.shop.repository.ShopRepository;
import com.example.picknwhip_be.domain.user.entity.User;
import com.example.picknwhip_be.domain.user.exception.UserException;
import com.example.picknwhip_be.domain.user.exception.code.UserErrorCode;
import com.example.picknwhip_be.domain.user.repository.UserRepository;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class CustomCommandServiceImpl implements CustomCommandService {

  private final OrderDraftRepository orderDraftRepository;
  private final OrderDraftItemRepository orderDraftItemRepository;
  private final ShopRepository shopRepository;
  private final ShopCakeSizeRepository shopCakeSizeRepository;
  private final CustomOptionRepository customOptionRepository;
  private final UserRepository userRepository;
  private final ShopBusinessHourRepository shopBusinessHourRepository;
  private final OrderRepository orderRepository;
  private final DesignGalleryRepository designGalleryRepository;

  @Override
  public CustomResDTO.CustomCreateDTO saveCustom(Long userId, CustomReqDTO.CustomCreateDTO dto) {

    User user =
        userRepository
            .findById(userId)
            .orElseThrow(() -> new UserException(UserErrorCode.USER_NOT_FOUND));

    Shop shop =
        shopRepository
            .findById(dto.shopId())
            .orElseThrow(() -> new ShopException(ShopErrorCode.SHOP_NOT_FOUND));

    ShopCakeSize size =
        shopCakeSizeRepository
            .findById(dto.shopCakeSizeId())
            .orElseThrow(() -> new ShopException(ShopErrorCode.CAKE_SIZE_NOT_FOUND));

    DesignGallery designGallery = null;
    if (dto.designId() != null) {
      designGallery =
          designGalleryRepository
              .findById(dto.designId())
              .orElseThrow(() -> new DesignException(DesignErrorCode.DESIGN_NOT_FOUND));
    }

    if (size.getShop() == null || !size.getShop().getId().equals(shop.getId())) {
      throw new ShopException(ShopErrorCode.CAKE_SIZE_NOT_FOUND);
    }
    validatePickupTime(shop, dto.pickupDatetime());

    OrderDraft draft =
        OrderDraft.builder()
            .user(user)
            .shop(shop)
            .shopCakeSize(size)
            .pickupDatetime(dto.pickupDatetime())
            .designGallery(designGallery)
            .letteringText(dto.letteringText())
            .letteringLineCount(dto.letteringLineCount())
            .letteringAlignment(dto.letteringAlignment())
            .additionalRequest(dto.additionalRequest())
            .referenceImageUrl(dto.referenceImageUrl())
            .pickupDatetime(dto.pickupDatetime())
            .build();

    OrderDraft savedDraft = orderDraftRepository.save(draft);

    List<OrderDraftItem> items = new ArrayList<>();

    if (dto.customOptionIds() != null) {
      for (Long optionId : dto.customOptionIds()) {
        CustomOption option =
            customOptionRepository
                .findById(optionId)
                .orElseThrow(() -> new CustomException(CustomErrorCode.OPTION_NOT_FOUND));

        items.add(createDraftItem(savedDraft, option, null, null));
      }
    }

    if (dto.toppings() != null) {
      for (CustomReqDTO.CustomCreateDTO.Topping toppingReq : dto.toppings()) {
        CustomOption option =
            customOptionRepository
                .findById(toppingReq.optionId())
                .orElseThrow(() -> new CustomException(CustomErrorCode.OPTION_NOT_FOUND));

        items.add(createDraftItem(savedDraft, option, toppingReq.x(), toppingReq.y()));
      }
    }

    orderDraftItemRepository.saveAll(items);

    return new CustomResDTO.CustomCreateDTO(savedDraft.getId(), savedDraft.getCreatedAt());
  }

  private void validatePickupTime(Shop shop, LocalDateTime pickupDatetime) {
    if (pickupDatetime.isBefore(LocalDateTime.now())) {
      throw new OrderException(OrderErrorCode.INVALID_PICKUP_TIME);
    }

    LocalDate date = pickupDatetime.toLocalDate();
    LocalTime time = pickupDatetime.toLocalTime();

    ShopBusinessHour hour =
        shopBusinessHourRepository
            .findByShopIdAndDateAndScheduleType(shop.getId(), date, ScheduleType.DATE)
            .orElseGet(
                () ->
                    shopBusinessHourRepository
                        .findByShopIdAndDayOfWeekAndScheduleType(
                            shop.getId(), date.getDayOfWeek().getValue(), ScheduleType.WEEKLY)
                        .orElse(null));

    if (hour == null || hour.isClosed()) {
      throw new OrderException(OrderErrorCode.SHOP_CLOSED_DAY);
    }

    if (time.isBefore(hour.getOpenTime()) || !time.isBefore(hour.getCloseTime())) {
      throw new OrderException(OrderErrorCode.SHOP_CLOSED_TIME);
    }

    List<Status> excludedStatuses = Arrays.asList(Status.CANCELED_BY_SHOP, Status.PAYMENT_FAILED);

    long currentOrders =
        orderRepository.countByShopAndPickupTime(shop.getId(), pickupDatetime, excludedStatuses);

    int maxCapacity = shop.getMaxOrdersPerSlot() > 0 ? shop.getMaxOrdersPerSlot() : 2;

    if (currentOrders >= maxCapacity) {
      throw new OrderException(OrderErrorCode.SLOT_ALREADY_FULL);
    }
  }

  private OrderDraftItem createDraftItem(
      OrderDraft draft, CustomOption option, Double x, Double y) {
    return OrderDraftItem.builder()
        .orderDraft(draft)
        .customOption(option)
        .positionX(x)
        .positionY(y)
        .build();
  }
}

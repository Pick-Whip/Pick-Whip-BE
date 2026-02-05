package com.example.picknwhip_be.domain.order.service.command;

import com.example.picknwhip_be.domain.custom.entity.OrderDraft;
import com.example.picknwhip_be.domain.custom.entity.OrderDraftItem;
import com.example.picknwhip_be.domain.custom.repository.OrderDraftRepository;
import com.example.picknwhip_be.domain.order.dto.req.OrderReqDTO;
import com.example.picknwhip_be.domain.order.dto.res.OrderResDTO;
import com.example.picknwhip_be.domain.order.entity.DailyShopOrderCounter;
import com.example.picknwhip_be.domain.order.entity.Order;
import com.example.picknwhip_be.domain.order.entity.OrderItem;
import com.example.picknwhip_be.domain.order.entity.enums.PaymentStatus;
import com.example.picknwhip_be.domain.order.entity.enums.Status;
import com.example.picknwhip_be.domain.order.exception.OrderException;
import com.example.picknwhip_be.domain.order.exception.code.OrderErrorCode;
import com.example.picknwhip_be.domain.order.repository.DailyShopOrderCounterRepository;
import com.example.picknwhip_be.domain.order.repository.OrderItemRepository;
import com.example.picknwhip_be.domain.order.repository.OrderRepository;
import com.example.picknwhip_be.domain.shop.validator.PickupTimeValidator;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class OrderCommandServiceImpl implements OrderCommandService {

  private final OrderRepository orderRepository;
  private final OrderItemRepository orderItemRepository;
  private final OrderDraftRepository orderDraftRepository;
  private final DailyShopOrderCounterRepository counterRepository;
  private final PickupTimeValidator pickupTimeValidator;

  @Override
  public OrderResDTO.OrderCompleteDTO createOrder(Long userId, OrderReqDTO.CreateOrderDTO dto) {
    OrderDraft draft =
        orderDraftRepository
            .findById(dto.getDraftId())
            .orElseThrow(() -> new OrderException(OrderErrorCode.DRAFT_NOT_FOUND));

    if (!draft.getUser().getUserId().equals(userId)) {
      throw new OrderException(OrderErrorCode.FORBIDDEN_ACCESS);
    }
    pickupTimeValidator.validate(draft.getShop(), draft.getPickupDatetime());
    String orderCode =
        generateOrderCode(draft.getShop().getId(), draft.getPickupDatetime().toLocalDate());

    Order newOrder =
        Order.builder()
            .user(draft.getUser())
            .shop(draft.getShop())
            .shopCakeSize(draft.getShopCakeSize())
            .designGallery(draft.getDesignGallery())
            .status(Status.CONFIRM_WAIT)
            .pickupDatetime(draft.getPickupDatetime())
            .letteringText(draft.getLetteringText())
            .letteringLineCount(draft.getLetteringLineCount())
            .letteringAlignment(draft.getLetteringAlignment())
            .additionalRequest(draft.getAdditionalRequest())
            .orderAdditionalRequest(dto.getAdditionalRequest())
            .referenceImageUrl(draft.getReferenceImageUrl())
            .paymentMethod("TBD")
            .paymentStatus(PaymentStatus.WAITING)
            .totalPrice(calculateTotalPrice(draft))
            .orderCode(orderCode)
            .customerName(dto.getCustomerName())
            .customerPhone(dto.getCustomerPhone())
            .build();

    Order savedOrder = orderRepository.save(newOrder);

    List<OrderItem> orderItems = new ArrayList<>();
    for (OrderDraftItem draftItem : draft.getItems()) {
      orderItems.add(
          OrderItem.builder()
              .order(savedOrder)
              .customOption(draftItem.getCustomOption())
              .optionCategory(draftItem.getCustomOption().getCategory())
              .optionName(draftItem.getCustomOption().getOptionName())
              .unitPrice(draftItem.getCustomOption().getAdditionalPrice())
              .colorRgbCode(draftItem.getCustomOption().getColorRgbCode())
              .positionX(draftItem.getPositionX())
              .positionY(draftItem.getPositionY())
              .build());
    }
    orderItemRepository.saveAll(orderItems);
    orderDraftRepository.delete(draft);

    return OrderResDTO.from(savedOrder);
  }

  /** 주문 코드 생성 로직 (YYMMDD_XXX) */
  private String generateOrderCode(Long shopId, LocalDate date) {
    int maxRetries = 3;
    int retryCount = 0;

    while (retryCount < maxRetries) {
      try {
        DailyShopOrderCounter counter =
            counterRepository
                .findByShopIdAndDateWithLock(shopId, date)
                .orElseGet(
                    () ->
                        DailyShopOrderCounter.builder().shopId(shopId).date(date).count(0).build());

        counter.increaseCount();
        counterRepository.save(counter);

        String dateStr = date.format(DateTimeFormatter.ofPattern("yyMMdd"));
        return String.format("%s_%03d", dateStr, counter.getCount());

      } catch (DataIntegrityViolationException e) {
        retryCount++;
        if (retryCount >= maxRetries) {
          throw new OrderException(OrderErrorCode.ORDER_CODE_GENERATION_FAILED);
        }
      }
    }
    throw new OrderException(OrderErrorCode.ORDER_CODE_GENERATION_FAILED);
  }

  private int calculateTotalPrice(OrderDraft draft) {
    int basePrice = draft.getShopCakeSize().getPrice() + draft.getDesignGallery().getBasePrice();
    int optionPrice =
        draft.getItems().stream()
            .mapToInt(item -> item.getCustomOption().getAdditionalPrice())
            .sum();
    return basePrice + optionPrice;
  }
}

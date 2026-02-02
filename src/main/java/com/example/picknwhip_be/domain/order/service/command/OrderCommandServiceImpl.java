package com.example.picknwhip_be.domain.order.service.command;

import com.example.picknwhip_be.domain.custom.entity.OrderDraft;
import com.example.picknwhip_be.domain.custom.entity.OrderDraftItem;
import com.example.picknwhip_be.domain.custom.exception.CustomException;
import com.example.picknwhip_be.domain.custom.exception.code.CustomErrorCode;
import com.example.picknwhip_be.domain.custom.repository.OrderDraftRepository;
import com.example.picknwhip_be.domain.order.dto.req.OrderReqDTO;
import com.example.picknwhip_be.domain.order.dto.res.OrderResDTO;
import com.example.picknwhip_be.domain.order.entity.DailyShopOrderCounter;
import com.example.picknwhip_be.domain.order.entity.Order;
import com.example.picknwhip_be.domain.order.entity.OrderItem;
import com.example.picknwhip_be.domain.order.entity.enums.PaymentStatus;
import com.example.picknwhip_be.domain.order.entity.enums.Status;
import com.example.picknwhip_be.domain.order.repository.DailyShopOrderCounterRepository;
import com.example.picknwhip_be.domain.order.repository.OrderRepository;
import com.example.picknwhip_be.domain.order.repository.OrderItemRepository;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
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

    @Override
    public OrderResDTO.OrderCompleteDTO createOrder(Long userId, OrderReqDTO.CreateOrderDTO dto) {
        OrderDraft draft = orderDraftRepository.findById(dto.getDraftId())
                .orElseThrow(() -> new CustomException(CustomErrorCode.DRAFT_NOT_FOUND));

        if (!draft.getUser().getUserId().equals(userId)) {
            throw new CustomException(CustomErrorCode.FORBIDDEN);
        }

        String orderCode = generateOrderCode(draft.getShop().getId(), LocalDate.now());

        int estimatedPrice = draft.getShopCakeSize().getPrice();
        for(OrderDraftItem item : draft.getItems()) {
            estimatedPrice += item.getCustomOption().getAdditionalPrice();
        }

        Order order = Order.builder()
                .user(draft.getUser())
                .customerName(dto.getCustomerName())
                .customerPhone(dto.getCustomerPhone())
                .additionalRequest(dto.getAdditionalRequest())
                .shop(draft.getShop())
                .shopCakeSize(draft.getShopCakeSize())
                .designGallery(draft.getDesignGallery())
                .status(Status.CONFIRM_WAIT)
                .pickupDatetime(draft.getPickupDatetime())
                .letteringText(draft.getLetteringText())
                .letteringLineCount(draft.getLetteringLineCount())
                .letteringAlignment(draft.getLetteringAlignment())
                .referenceImageUrl(draft.getReferenceImageUrl())
                .paymentStatus(PaymentStatus.WAITING)
                .totalPrice(estimatedPrice)
                .orderCode(orderCode)
                .build();

        Order savedOrder = orderRepository.save(order);
        List<OrderItem> orderItems = new ArrayList<>();
        for (OrderDraftItem draftItem : draft.getItems()) {
            orderItems.add(OrderItem.builder()
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

    private String generateOrderCode(Long shopId, LocalDate date) {
        DailyShopOrderCounter counter = counterRepository
                .findByShopIdAndDateWithLock(shopId, date)
                .orElseGet(() -> DailyShopOrderCounter.builder()
                        .shopId(shopId)
                        .date(date)
                        .count(0)
                        .build());

        counter.increaseCount();
        counterRepository.save(counter);

        String dateStr = date.format(DateTimeFormatter.ofPattern("yyMMdd"));
        return String.format("%s_%03d", dateStr, counter.getCount());
    }
}
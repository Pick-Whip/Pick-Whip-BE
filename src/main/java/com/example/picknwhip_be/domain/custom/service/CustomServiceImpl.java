package com.example.picknwhip_be.domain.custom.service;

import com.example.picknwhip_be.domain.custom.dto.CustomReqDTO;
import com.example.picknwhip_be.domain.custom.dto.CustomResDTO;
import com.example.picknwhip_be.domain.custom.entity.OrderDraft;
import com.example.picknwhip_be.domain.custom.entity.OrderDraftItem;
import com.example.picknwhip_be.domain.custom.exception.CustomException;
import com.example.picknwhip_be.domain.custom.exception.code.CustomOptionErrorCode;
import com.example.picknwhip_be.domain.order.repository.OrderDraftItemRepository;
import com.example.picknwhip_be.domain.order.repository.OrderDraftRepository;
import com.example.picknwhip_be.domain.shop.entity.CustomOption;
import com.example.picknwhip_be.domain.shop.entity.Shop;
import com.example.picknwhip_be.domain.shop.entity.ShopCakeSize;
import com.example.picknwhip_be.domain.shop.exception.ShopException;
import com.example.picknwhip_be.domain.shop.exception.code.ShopErrorCode;
import com.example.picknwhip_be.domain.shop.repository.CustomOptionRepository;
import com.example.picknwhip_be.domain.shop.repository.ShopCakeSizeRepository;
import com.example.picknwhip_be.domain.shop.repository.ShopRepository;
import com.example.picknwhip_be.domain.user.entity.User;
import com.example.picknwhip_be.domain.user.repository.UserRepository;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class CustomServiceImpl implements CustomService {

  private final OrderDraftRepository orderDraftRepository;
  private final OrderDraftItemRepository orderDraftItemRepository;
  private final ShopRepository shopRepository;
  private final ShopCakeSizeRepository shopCakeSizeRepository;
  private final CustomOptionRepository customOptionRepository;
  private final UserRepository userRepository;

  @Override
  public CustomResDTO.CustomCreateDTO saveCustom(CustomReqDTO.CustomCreateDTO dto) {
    User user =
        userRepository.findById(1L).orElseThrow(() -> new RuntimeException("테스트용 1번 유저가 DB에 없음"));

    Shop shop =
        shopRepository
            .findById(dto.shopId())
            .orElseThrow(() -> new ShopException(ShopErrorCode.SHOP_NOT_FOUND));

    ShopCakeSize size =
        shopCakeSizeRepository
            .findById(dto.shopCakeSizeId())
            .orElseThrow(() -> new ShopException(ShopErrorCode.CAKE_SIZE_NOT_FOUND));

    OrderDraft draft =
        OrderDraft.builder()
            .user(user)
            .shop(shop)
            .shopCakeSize(size)
            .pickupDatetime(dto.pickupDatetime())
            .letteringText(dto.letteringText())
            .letteringLineCount(dto.letteringLineCount())
            .letteringAlignment(dto.letteringAlignment())
            .additionalRequest(dto.additionalRequest())
            .referenceImageUrl(dto.referenceImageUrl())
            .build();

    OrderDraft savedDraft = orderDraftRepository.save(draft);

    List<OrderDraftItem> items = new ArrayList<>();

    if (dto.customOptionIds() != null) {
      for (Long optionId : dto.customOptionIds()) {
        CustomOption option =
            customOptionRepository
                .findById(optionId)
                .orElseThrow(() -> new CustomException(CustomOptionErrorCode.OPTION_NOT_FOUND));

        items.add(createDraftItem(savedDraft, option, null, null));
      }
    }

    if (dto.toppings() != null) {
      for (CustomReqDTO.CustomCreateDTO.ToppingRequest toppingReq : dto.toppings()) {
        CustomOption option =
            customOptionRepository
                .findById(toppingReq.optionId())
                .orElseThrow(() -> new CustomException(CustomOptionErrorCode.OPTION_NOT_FOUND));

        items.add(createDraftItem(savedDraft, option, toppingReq.x(), toppingReq.y()));
      }
    }

    orderDraftItemRepository.saveAll(items);

    return new CustomResDTO.CustomCreateDTO(savedDraft.getId(), savedDraft.getCreatedAt());
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

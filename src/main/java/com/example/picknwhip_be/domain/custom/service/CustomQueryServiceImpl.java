package com.example.picknwhip_be.domain.custom.service;

import com.example.picknwhip_be.domain.custom.converter.CustomConverter;
import com.example.picknwhip_be.domain.custom.dto.CustomResDTO;
import com.example.picknwhip_be.domain.custom.entity.OrderDraft;
import com.example.picknwhip_be.domain.custom.exception.CustomException;
import com.example.picknwhip_be.domain.custom.exception.code.CustomErrorCode;
import com.example.picknwhip_be.domain.custom.repository.OrderDraftRepository;
import com.example.picknwhip_be.domain.shop.entity.CustomOption;
import com.example.picknwhip_be.domain.shop.entity.ShopCakeSize;
import com.example.picknwhip_be.domain.shop.entity.enums.OptionCategory;
import com.example.picknwhip_be.domain.shop.exception.ShopException;
import com.example.picknwhip_be.domain.shop.exception.code.ShopErrorCode;
import com.example.picknwhip_be.domain.shop.repository.CustomOptionRepository;
import com.example.picknwhip_be.domain.shop.repository.ShopCakeSizeRepository;
import com.example.picknwhip_be.domain.user.entity.User;
import com.example.picknwhip_be.domain.user.exception.UserException;
import com.example.picknwhip_be.domain.user.exception.code.UserErrorCode;
import com.example.picknwhip_be.domain.user.repository.UserRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class CustomQueryServiceImpl implements CustomQueryService {

  private final OrderDraftRepository orderDraftRepository;
  private final UserRepository userRepository;
  private final CustomOptionRepository customOptionRepository;
  private final ShopCakeSizeRepository shopCakeSizeRepository;

  @Override
  @Transactional(readOnly = true)
  public List<CustomResDTO.GetDraftListDTO> findDraftList(Long userId) {

    User user =
        userRepository
            .findById(userId)
            .orElseThrow(() -> new UserException(UserErrorCode.USER_NOT_FOUND));

    List<OrderDraft> orderDrafts = orderDraftRepository.findAllByUserOrderByIdDesc(user);

    return orderDrafts.stream()
        .map(
            draft -> {
              String sheetName =
                  draft.getItems().stream()
                      .filter(item -> item.getCustomOption().getCategory() == OptionCategory.SHEET)
                      .map(item -> item.getCustomOption().getOptionName())
                      .findFirst()
                      .orElse(null);

              Long progress = draft.calculateProgress();
              String status = draft.calculateStatus();

              return CustomResDTO.GetDraftListDTO.builder()
                  .draftId(draft.getId())
                  .shopCakeSize(draft.getShopCakeSize().getSizeName())
                  .pickupDatetime(draft.getPickupDatetime())
                  .progressPercentage(progress)
                  .presentStatus(status)
                  .updateAt(draft.getUpdatedAt())
                  .sheetName(sheetName)
                  .build();
            })
        .toList();
  }

  @Override
  @Transactional(readOnly = true)
  public CustomResDTO.GetDraftDetailDTO findDraftDetail(Long draftId) {

    OrderDraft orderDraft =
        orderDraftRepository
            .findById(draftId)
            .orElseThrow(() -> new CustomException(CustomErrorCode.DRAFT_NOT_FOUND));

    return CustomConverter.toDraftDetailDTO(orderDraft);
  }

  @Override
  @Transactional
  public CustomResDTO.DeleteDraftDTO deleteDraft(Long draftId, Long userId) {

    OrderDraft orderDraft =
        orderDraftRepository
            .findById(draftId)
            .orElseThrow(() -> new CustomException(CustomErrorCode.DRAFT_NOT_FOUND));
    if (!orderDraft.getUser().getUserId().equals(userId)) {
      throw new CustomException(CustomErrorCode.FORBIDDEN);
    }

    orderDraftRepository.delete(orderDraft);

    return CustomResDTO.DeleteDraftDTO.builder().draftId(orderDraft.getId()).build();
  }

  @Override
  @Transactional
  public CustomResDTO.GetDesignOptionDTO findDesignOption(Long shopId) {
    List<ShopCakeSize> shopCakeSizes = shopCakeSizeRepository.findByShopId(shopId);
    if (shopCakeSizes.isEmpty()) {
      throw new ShopException(ShopErrorCode.CAKE_SIZE_INFO_NOT_FOUND);
    }

    List<CustomOption> customOptions = customOptionRepository.findByShopId(shopId);
    if (customOptions.isEmpty()) {
      throw new ShopException(ShopErrorCode.CUSTOMOPTION_INFO_NOT_FOUND);
    }

    return CustomConverter.toDesignOptionDTO(shopId, shopCakeSizes, customOptions);
  }
}

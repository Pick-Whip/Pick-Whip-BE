package com.example.picknwhip_be.domain.custom.service;

import com.example.picknwhip_be.domain.custom.dto.CustomResDTO;
import com.example.picknwhip_be.domain.custom.entity.OrderDraft;
import com.example.picknwhip_be.domain.order.repository.OrderDraftRepository;
import com.example.picknwhip_be.domain.user.entity.User;
import com.example.picknwhip_be.domain.user.repository.UserRepository;
import com.example.picknwhip_be.global.apiPayload.code.GeneralErrorCode;
import com.example.picknwhip_be.global.apiPayload.exception.GeneralException;
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

  @Override
  @Transactional(readOnly = true)
  public List<CustomResDTO.GetDraftListDTO> findDraftList() {

    User user =
        userRepository
            .findById(1L)
            .orElseThrow(() -> new GeneralException(GeneralErrorCode.USER_NOT_FOUND));

    List<OrderDraft> orderDrafts = orderDraftRepository.findAllByUserOrderByIdDesc(user);

    return orderDrafts.stream()
        .map(
            draft -> {
              List<Long> optionIds =
                  draft.getItems().stream().map(item -> item.getCustomOption().getId()).toList();

              return CustomResDTO.GetDraftListDTO.builder()
                  .draftId(draft.getId())
                  .shopCakeSize(draft.getShopCakeSize().getSizeName())
                  .pickupDatetime(draft.getPickupDatetime())
                  .progressPercentage(0L) // 수정해야됌
                  .presentStatus("WRITING") // 수정해야됌
                  .updateAt(draft.getUpdatedAt())
                  .customOptionIds(optionIds)
                  .build();
            })
        .toList();
  }
}

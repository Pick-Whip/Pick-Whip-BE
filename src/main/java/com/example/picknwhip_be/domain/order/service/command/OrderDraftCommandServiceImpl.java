package com.example.picknwhip_be.domain.order.service.command;

import com.example.picknwhip_be.domain.custom.entity.OrderDraft;
import com.example.picknwhip_be.domain.custom.exception.CustomException;
import com.example.picknwhip_be.domain.custom.exception.code.CustomErrorCode;
import com.example.picknwhip_be.domain.custom.repository.OrderDraftRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Transactional
public class OrderDraftCommandServiceImpl implements OrderDraftCommandService {

    private final OrderDraftRepository orderDraftRepository;

    @Override
    public void updatePickupTime(Long userId, Long draftId, LocalDateTime newPickupTime) {
        OrderDraft draft = orderDraftRepository.findById(draftId)
                .orElseThrow(() -> new CustomException(CustomErrorCode.DRAFT_NOT_FOUND));

        if (!draft.getUser().getUserId().equals(userId)) {
            throw new CustomException(CustomErrorCode.FORBIDDEN);
        }
        draft.updatePickupDatetime(newPickupTime);
    }
}


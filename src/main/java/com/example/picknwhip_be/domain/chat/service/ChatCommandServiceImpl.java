package com.example.picknwhip_be.domain.chat.service;

import com.example.picknwhip_be.domain.chat.converter.ChatConverter;
import com.example.picknwhip_be.domain.chat.dto.req.ChatRequestDTO;
import com.example.picknwhip_be.domain.chat.dto.res.ChatResponseDTO;
import com.example.picknwhip_be.domain.chat.entity.ChatMessage;
import com.example.picknwhip_be.domain.chat.entity.ChatRoom;
import com.example.picknwhip_be.domain.chat.repository.ChatMessageRepository;
import com.example.picknwhip_be.domain.chat.repository.ChatRoomRepository;
import com.example.picknwhip_be.domain.shop.entity.Shop;
import com.example.picknwhip_be.domain.shop.repository.ShopRepository;
import com.example.picknwhip_be.domain.user.entity.User;
import com.example.picknwhip_be.domain.user.repository.UserRepository;
import com.example.picknwhip_be.global.apiPayload.code.GeneralErrorCode;
import com.example.picknwhip_be.global.apiPayload.exception.GeneralException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class ChatCommandServiceImpl implements ChatCommandService {
  private final ChatRoomRepository chatRoomRepository;
  private final ChatMessageRepository chatMessageRepository;
  private final UserRepository userRepository;
  private final ShopRepository shopRepository;

  @Override
  public ChatResponseDTO.RoomInfo saveOrCreateRoom(ChatRequestDTO.CreateRoom dto, Long customerId) {
    User customer =
        userRepository
            .findById(customerId)
            .orElseThrow(() -> new GeneralException(GeneralErrorCode.USER_NOT_FOUND));
    Shop shop =
        shopRepository
            .findById(dto.getShopId())
            .orElseThrow(() -> new GeneralException(GeneralErrorCode.NOT_FOUND));

    ChatRoom room =
        chatRoomRepository
            .findByCustomerAndShop(customer, shop)
            .orElseGet(
                () ->
                    chatRoomRepository.save(
                        ChatRoom.builder().customer(customer).shop(shop).build()));

    return ChatConverter.toRoomInfo(room);
  }

  @Override
  public ChatMessage saveMessage(Long roomId, ChatRequestDTO.SendMessageDTO dto, Long senderId) {
    ChatRoom room =
        chatRoomRepository
            .findById(roomId)
            .orElseThrow(() -> new GeneralException(GeneralErrorCode.CHAT_ROOM_NOT_FOUND));

    // 권한 체크
    if (!room.getCustomer().getUserId().equals(senderId)
        && !room.getShop().getOwner().getUserId().equals(senderId)) {
      throw new GeneralException(GeneralErrorCode.CHAT_NOT_PARTICIPANT);
    }

    User sender =
        userRepository
            .findById(senderId)
            .orElseThrow(() -> new GeneralException(GeneralErrorCode.USER_NOT_FOUND));
    ChatMessage message = ChatConverter.toChatMessage(room, sender, dto);

    ChatMessage saved = chatMessageRepository.save(message);
    room.updateLastMessage(saved.getChatId());
    return saved;
  }

  @Override
  public void updateMarkAsRead(Long roomId, Long userId) {
    chatMessageRepository.markAsReadByRoomId(roomId, userId);
  }
}

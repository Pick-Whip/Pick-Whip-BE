package com.example.picknwhip_be.domain.chat.service.command;

import com.example.picknwhip_be.domain.S3.dto.res.S3ResDTO;
import com.example.picknwhip_be.domain.S3.service.S3Service;
import com.example.picknwhip_be.domain.chat.converter.ChatConverter;
import com.example.picknwhip_be.domain.chat.dto.req.ChatReqDTO;
import com.example.picknwhip_be.domain.chat.dto.res.ChatResDTO;
import com.example.picknwhip_be.domain.chat.entity.ChatMessage;
import com.example.picknwhip_be.domain.chat.entity.ChatRoom;
import com.example.picknwhip_be.domain.chat.exception.ChatException;
import com.example.picknwhip_be.domain.chat.exception.code.ChatErrorCode;
import com.example.picknwhip_be.domain.chat.repository.ChatMessageRepository;
import com.example.picknwhip_be.domain.chat.repository.ChatRoomRepository;
import com.example.picknwhip_be.domain.order.entity.Order;
import com.example.picknwhip_be.domain.order.exception.OrderException;
import com.example.picknwhip_be.domain.order.exception.code.OrderErrorCode;
import com.example.picknwhip_be.domain.order.repository.OrderRepository;
import com.example.picknwhip_be.domain.shop.entity.Shop;
import com.example.picknwhip_be.domain.shop.repository.ShopRepository;
import com.example.picknwhip_be.domain.user.entity.User;
import com.example.picknwhip_be.domain.user.exception.UserException;
import com.example.picknwhip_be.domain.user.exception.code.UserErrorCode;
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
public class ChatCommandServiceImpl implements ChatCommandService {
  private final ChatRoomRepository chatRoomRepository;
  private final ChatMessageRepository chatMessageRepository;
  private final UserRepository userRepository;
  private final ShopRepository shopRepository;
  private final S3Service s3Service;
  private final OrderRepository orderRepository;

  @Override
  public ChatResDTO.RoomInfo saveOrCreateRoom(ChatReqDTO.CreateRoom dto, Long customerId) {
    User customer =
        userRepository
            .findById(customerId)
            .orElseThrow(() -> new UserException(UserErrorCode.USER_NOT_FOUND));
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

    // 요청에 orderId가 포함되어 있다면 해당 주문 정보 조회
    Order order = null;
    if (dto.getOrderId() != null) {
      order =
          orderRepository
              .findById(dto.getOrderId())
              .orElseThrow(() -> new OrderException(OrderErrorCode.ORDER_NOT_FOUND));

      if (!order.getUser().getUserId().equals(customerId)) {
        throw new OrderException(OrderErrorCode.FORBIDDEN_ACCESS);
      }

      if (!order.getShop().getId().equals(shop.getId())) {
        throw new OrderException(OrderErrorCode.INVALID_ORDER_CONTEXT);
      }
    }

    return ChatConverter.toRoomInfo(room, order);
  }

  @Override
  public ChatMessage saveMessage(Long roomId, ChatReqDTO.SendMessageDTO dto, Long senderId) {
    ChatRoom room =
        chatRoomRepository
            .findById(roomId)
            .orElseThrow(() -> new ChatException(ChatErrorCode.CHAT_ROOM_NOT_FOUND));

    // 권한 체크
    if (!room.getCustomer().getUserId().equals(senderId)
        && !room.getShop().getOwner().getUserId().equals(senderId)) {
      throw new ChatException(ChatErrorCode.CHAT_NOT_PARTICIPANT);
    }

    User sender =
        userRepository
            .findById(senderId)
            .orElseThrow(() -> new UserException(UserErrorCode.USER_NOT_FOUND));
    ChatMessage message = ChatConverter.toChatMessage(room, sender, dto);

    ChatMessage saved = chatMessageRepository.save(message);
    room.updateLastMessage(saved.getChatId());
    return saved;
  }

  @Override
  public void updateMarkAsRead(Long roomId, Long userId) {
    chatMessageRepository.markAsReadByRoomId(roomId, userId);
  }

  @Override
  public List<S3ResDTO.PresignResponseDTO> getChatImageUploadUrls(
      Long roomId, Long userId, List<String> fileNames) {
    ChatRoom room =
        chatRoomRepository
            .findById(roomId)
            .orElseThrow(() -> new ChatException(ChatErrorCode.CHAT_ROOM_NOT_FOUND));

    if (!room.getCustomer().getUserId().equals(userId)
        && !room.getShop().getOwner().getUserId().equals(userId)) {
      throw new ChatException(ChatErrorCode.CHAT_NOT_PARTICIPANT);
    }

    return s3Service.createChatUploadUrls(roomId, fileNames);
  }
}

package com.example.picknwhip_be.domain.chat.converter;

import com.example.picknwhip_be.domain.chat.dto.req.ChatReqDTO;
import com.example.picknwhip_be.domain.chat.dto.res.ChatResDTO;
import com.example.picknwhip_be.domain.chat.entity.ChatMessage;
import com.example.picknwhip_be.domain.chat.entity.ChatRoom;
import com.example.picknwhip_be.domain.order.entity.Order;
import com.example.picknwhip_be.domain.user.entity.User;
import java.util.List;

public class ChatConverter {
  public static ChatResDTO.RoomInfo toRoomInfo(ChatRoom room, Order order) {
    return ChatResDTO.RoomInfo.builder()
        .roomId(room.getChatRoomId())
        .shopName(room.getShop().getShopName())
        .orderSummary(order != null ? toOrderSummary(order) : null)
        .build();
  }

  public static ChatResDTO.OrderSummary toOrderSummary(Order order) {
    if (order == null) return null;
    return ChatResDTO.OrderSummary.builder()
        .orderId(order.getId())
        .orderCode(order.getOrderCode())
        .cakeSize(order.getShopCakeSize().getSizeName())
        .pickupDatetime(order.getPickupDatetime().toString())
        .totalPrice(order.getTotalPrice())
        .status(order.getStatus().name())
        .build();
  }

  public static ChatResDTO.MessageInfo toMessageInfo(ChatMessage message) {
    return ChatResDTO.MessageInfo.builder()
        .messageId(message.getChatId())
        .senderId(message.getSender().getUserId())
        .content(message.getMessageText())
        .imageUrl(message.getMessageImageUrl())
        .type(message.getMessageType())
        .isRead(message.isRead())
        .createdAt(message.getCreatedAt())
        .build();
  }

  public static ChatMessage toChatMessage(
      ChatRoom room, User sender, ChatReqDTO.SendMessageDTO dto) {
    return ChatMessage.builder()
        .chatRoom(room)
        .sender(sender)
        .messageType(dto.getType())
        .messageText(dto.getContent())
        .messageImageUrl(dto.getImageUrl())
        .build();
  }

  public static ChatResDTO.ChatRoomSummaryDTO toChatRoomSummaryDTO(
      ChatRoom room, ChatMessage lastMessage, Long unreadCount) {

    return ChatResDTO.ChatRoomSummaryDTO.builder()
        .roomId(room.getChatRoomId())
        .shopName(room.getShop().getShopName())
        .shopImageUrl(room.getShop().getShopImageUrl())
        .lastMessage(lastMessage != null ? lastMessage.getMessageText() : "메시지가 없습니다.")
        .lastMessageTime(lastMessage != null ? lastMessage.getCreatedAt() : room.getCreatedAt())
        .unreadCount(unreadCount)
        .build();
  }

  public static ChatResDTO.ChatRoomListDTO toChatRoomListDTO(
      List<ChatResDTO.ChatRoomSummaryDTO> summaryList, Long nextCursor, boolean hasNext) {
    return ChatResDTO.ChatRoomListDTO.builder()
        .chatRooms(summaryList)
        .nextCursor(nextCursor)
        .hasNext(hasNext)
        .build();
  }

  public static ChatResDTO.UnreadCountUpdateDTO toUnreadCountUpdateDTO(
      Long roomId, Long unreadCount) {
    return ChatResDTO.UnreadCountUpdateDTO.builder()
        .roomId(roomId)
        .unreadCount(unreadCount)
        .build();
  }
}

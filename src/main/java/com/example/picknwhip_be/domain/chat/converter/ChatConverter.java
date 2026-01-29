package com.example.picknwhip_be.domain.chat.converter;

import com.example.picknwhip_be.domain.chat.dto.req.ChatRequestDTO;
import com.example.picknwhip_be.domain.chat.dto.res.ChatResponseDTO;
import com.example.picknwhip_be.domain.chat.entity.ChatMessage;
import com.example.picknwhip_be.domain.chat.entity.ChatRoom;
import com.example.picknwhip_be.domain.user.entity.User;
import java.util.List;

public class ChatConverter {
  public static ChatResponseDTO.RoomInfo toRoomInfo(ChatRoom room) {
    return ChatResponseDTO.RoomInfo.builder()
        .roomId(room.getChatRoomId())
        .shopName(room.getShop().getShopName())
        .build();
  }

  public static ChatResponseDTO.MessageInfo toMessageInfo(ChatMessage message) {
    return ChatResponseDTO.MessageInfo.builder()
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
      ChatRoom room, User sender, ChatRequestDTO.SendMessageDTO dto) {
    return ChatMessage.builder()
        .chatRoom(room)
        .sender(sender)
        .messageType(dto.getType())
        .messageText(dto.getContent())
        .messageImageUrl(dto.getImageUrl())
        .build();
  }

  public static ChatResponseDTO.ChatRoomSummaryDTO toChatRoomSummaryDTO(
      ChatRoom room, ChatMessage lastMessage, Long unreadCount) {

    return ChatResponseDTO.ChatRoomSummaryDTO.builder()
        .roomId(room.getChatRoomId())
        .shopName(room.getShop().getShopName())
        .shopImageUrl(room.getShop().getShopImageUrl())
        .lastMessage(lastMessage != null ? lastMessage.getMessageText() : "메시지가 없습니다.")
        .lastMessageTime(lastMessage != null ? lastMessage.getCreatedAt() : room.getCreatedAt())
        .unreadCount(unreadCount)
        .build();
  }

  public static ChatResponseDTO.ChatRoomListDTO toChatRoomListDTO(
      List<ChatResponseDTO.ChatRoomSummaryDTO> summaryList, Long nextCursor, boolean hasNext) {
    return ChatResponseDTO.ChatRoomListDTO.builder()
        .chatRooms(summaryList)
        .nextCursor(nextCursor)
        .hasNext(hasNext)
        .build();
  }

  public static ChatResponseDTO.UnreadCountUpdateDTO toUnreadCountUpdateDTO(
      Long roomId, Long unreadCount) {
    return ChatResponseDTO.UnreadCountUpdateDTO.builder()
        .roomId(roomId)
        .unreadCount(unreadCount)
        .build();
  }
}

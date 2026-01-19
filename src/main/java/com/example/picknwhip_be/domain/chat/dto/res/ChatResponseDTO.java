package com.example.picknwhip_be.domain.chat.dto.res;

import com.example.picknwhip_be.domain.chat.entity.MessageType;
import java.time.LocalDateTime;
import java.util.List;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class ChatResponseDTO {
  @Builder
  @Getter
  @NoArgsConstructor
  @AllArgsConstructor
  public static class RoomInfo {
    private Long roomId;
    private String shopName;
  }

  @Builder
  @Getter
  @NoArgsConstructor
  @AllArgsConstructor
  public static class MessageInfo {
    private Long messageId;
    private Long senderId;
    private String content;
    private String imageUrl;
    private MessageType type;
    private boolean isRead;
    private LocalDateTime createdAt;
  }

  @Builder
  @Getter
  @NoArgsConstructor
  @AllArgsConstructor
  public static class ChatRoomSummaryDTO {
    private Long roomId;
    private String shopName;
    private String shopImageUrl;
    private String lastMessage;
    private LocalDateTime lastMessageTime;
    private Long unreadCount;
  }

  @Builder
  @Getter
  @NoArgsConstructor
  @AllArgsConstructor
  public static class ChatRoomListDTO {
    private List<ChatRoomSummaryDTO> chatRooms;
  }

  @Builder
  @Getter
  @NoArgsConstructor
  @AllArgsConstructor
  public static class UnreadCountUpdateDTO {
    private Long roomId;
    private Long unreadCount;
  }

  @Builder
  @Getter
  @NoArgsConstructor
  @AllArgsConstructor
  public static class MessageListDTO {
    private List<MessageInfo> messageList;
    private Long nextCursor;
    private boolean hasNext;
  }
}

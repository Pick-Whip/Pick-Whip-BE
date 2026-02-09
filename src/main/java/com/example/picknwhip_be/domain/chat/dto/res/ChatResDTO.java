package com.example.picknwhip_be.domain.chat.dto.res;

import com.example.picknwhip_be.domain.chat.entity.MessageType;
import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalDateTime;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class ChatResDTO {
  @Builder
  @Getter
  @NoArgsConstructor
  @AllArgsConstructor
  public static class RoomInfo {
    private Long roomId;
    private String shopName;
    private OrderSummary orderSummary;
  }

  @Builder
  @Getter
  @NoArgsConstructor
  @AllArgsConstructor
  public static class OrderSummary {
    private Long orderId;
    private String orderCode;
    private String cakeSize;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "Asia/Seoul")
    private LocalDateTime pickupDatetime;

    private int totalPrice;
    private String status;
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
    private Long nextCursor;
    private boolean hasNext;
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

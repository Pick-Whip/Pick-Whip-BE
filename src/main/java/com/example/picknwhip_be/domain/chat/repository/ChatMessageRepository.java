package com.example.picknwhip_be.domain.chat.repository;

import com.example.picknwhip_be.domain.chat.entity.ChatMessage;
import com.example.picknwhip_be.domain.chat.entity.ChatRoom;
import com.example.picknwhip_be.domain.review.service.user.entity.User;
import java.util.List;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ChatMessageRepository extends JpaRepository<ChatMessage, Long> {
  List<ChatMessage> findByChatRoomOrderByCreatedAtAsc(ChatRoom chatRoom);

  // 채팅방 ID 목록으로 각 방의 마지막 메시지들을 한꺼번에 조회
  @Query(
      "SELECT m FROM ChatMessage m WHERE m.chatId IN "
          + "(SELECT MAX(m2.chatId) FROM ChatMessage m2 WHERE m2.chatRoom.chatRoomId IN :roomIds GROUP BY m2.chatRoom.chatRoomId)")
  List<ChatMessage> findLastMessagesByRoomIds(@Param("roomIds") List<Long> roomIds);

  // 채팅방 ID 목록으로 각 방의 안 읽은 메시지 개수를 그룹화하여 조회
  @Query(
      "SELECT m.chatRoom.chatRoomId, COUNT(m) FROM ChatMessage m "
          + "WHERE m.chatRoom.chatRoomId IN :roomIds AND m.isRead = false AND m.sender != :user "
          + "GROUP BY m.chatRoom.chatRoomId")
  List<Object[]> countUnreadMessagesByRoomIds(
      @Param("roomIds") List<Long> roomIds, @Param("user") User user);

  // 커서 페이징: 특정 ID(cursor)보다 작은 메시지들을 최신순으로 조회
  @Query(
      "SELECT m FROM ChatMessage m "
          + "JOIN FETCH m.sender "
          + "WHERE m.chatRoom.chatRoomId = :roomId "
          + "AND (:cursor IS NULL OR m.chatId < :cursor) "
          + // 커서가 null이면 가장 최신부터
          "ORDER BY m.chatId DESC")
  List<ChatMessage> findMessagesWithCursor(
      @Param("roomId") Long roomId, @Param("cursor") Long cursor, Pageable pageable);

  Long countByChatRoom_ChatRoomIdAndIsReadFalseAndSender_UserIdNot(Long chatRoomId, Long userId);

  @Modifying
  @Query(
      "UPDATE ChatMessage m SET m.isRead = true WHERE m.chatRoom.chatRoomId = :roomId AND m.sender.userId != :userId")
  void markAsReadByRoomId(@Param("roomId") Long roomId, @Param("userId") Long userId);
}

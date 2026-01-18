package com.example.picknwhip_be.domain.chat.repository;

import com.example.picknwhip_be.domain.chat.entity.ChatMessage;
import com.example.picknwhip_be.domain.chat.entity.ChatRoom;
import com.example.picknwhip_be.domain.user.entity.User;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ChatMessageRepository extends JpaRepository<ChatMessage, Long> {
  List<ChatMessage> findByChatRoomOrderByCreatedAtAsc(ChatRoom chatRoom);

  // 마지막 메시지 1건 조회
  Optional<ChatMessage> findTopByChatRoomOrderByCreatedAtDesc(ChatRoom chatRoom);

  // 읽지 않은 메시지 개수 조회
  Long countByChatRoomAndIsReadFalseAndSenderNot(ChatRoom chatRoom, User sender);

  @Modifying
  @Query(
      "UPDATE ChatMessage m SET m.isRead = true WHERE m.chatRoom.chatRoomId = :roomId AND m.sender.userId != :userId")
  void markAsReadByRoomId(@Param("roomId") Long roomId, @Param("userId") Long userId);
}

package com.example.picknwhip_be.domain.chat.service;

import com.example.picknwhip_be.domain.chat.converter.ChatConverter;
import com.example.picknwhip_be.domain.chat.dto.res.ChatResponseDTO;
import com.example.picknwhip_be.domain.chat.entity.ChatMessage;
import com.example.picknwhip_be.domain.chat.entity.ChatRoom;
import com.example.picknwhip_be.domain.chat.exception.ChatException;
import com.example.picknwhip_be.domain.chat.exception.code.ChatErrorCode;
import com.example.picknwhip_be.domain.chat.repository.ChatMessageRepository;
import com.example.picknwhip_be.domain.chat.repository.ChatRoomRepository;
import com.example.picknwhip_be.domain.user.entity.User;
import com.example.picknwhip_be.domain.user.exception.UserException;
import com.example.picknwhip_be.domain.user.exception.code.UserErrorCode;
import com.example.picknwhip_be.domain.user.repository.UserRepository;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ChatQueryServiceImpl implements ChatQueryService {

  private final UserRepository userRepository;
  private final ChatRoomRepository chatRoomRepository;
  private final ChatMessageRepository chatMessageRepository;

  @Override
  public ChatResponseDTO.MessageListDTO getMessages(Long roomId, Long cursor, Integer size) {
    // 요청한 size보다 1개 더 조회하여 다음 페이지 여부 확인
    PageRequest pageRequest = PageRequest.of(0, size + 1);
    List<ChatMessage> messages =
        chatMessageRepository.findMessagesWithCursor(roomId, cursor, pageRequest);

    boolean hasNext = messages.size() > size;
    if (hasNext) {
      messages = messages.subList(0, size); // 실제 필요한 개수만큼 자름
    }

    List<ChatResponseDTO.MessageInfo> messageInfos =
        messages.stream().map(ChatConverter::toMessageInfo).toList();

    // 다음 커서는 현재 리스트의 가장 마지막 메시지 ID
    Long nextCursor = messages.isEmpty() ? null : messages.get(messages.size() - 1).getChatId();

    return ChatResponseDTO.MessageListDTO.builder()
        .messageList(messageInfos)
        .nextCursor(nextCursor)
        .hasNext(hasNext)
        .build();
  }

  @Override
  public ChatResponseDTO.ChatRoomListDTO getChatRoomList(
      Long userId, String keyword, Long cursor, Integer size) {
    User user =
        userRepository
            .findById(userId)
            .orElseThrow(() -> new UserException(UserErrorCode.USER_NOT_FOUND));

    // 요청한 size보다 1개 더 조회하여 다음 페이지 여부 확인
    PageRequest pageRequest = PageRequest.of(0, size + 1);
    List<ChatRoom> rooms =
        chatRoomRepository.findChatRoomsWithCursor(user, keyword, cursor, pageRequest);

    boolean hasNext = rooms.size() > size;
    if (hasNext) {
      rooms = rooms.subList(0, size);
    }

    if (rooms.isEmpty()) {
      return ChatConverter.toChatRoomListDTO(Collections.emptyList(), null, false);
    }

    List<Long> roomIds = rooms.stream().map(ChatRoom::getChatRoomId).toList();

    // 마지막 메시지 및 안 읽은 개수 배치 조회 (기존 로직 활용)
    Map<Long, ChatMessage> lastMessageMap =
        chatMessageRepository.findLastMessagesByRoomIds(roomIds).stream()
            .collect(Collectors.toMap(m -> m.getChatRoom().getChatRoomId(), m -> m));

    Map<Long, Long> unreadCountMap =
        chatMessageRepository.countUnreadMessagesByRoomIds(roomIds, user).stream()
            .collect(Collectors.toMap(obj -> (Long) obj[0], obj -> (Long) obj[1]));

    List<ChatResponseDTO.ChatRoomSummaryDTO> summaryList =
        rooms.stream()
            .map(
                room -> {
                  ChatMessage lastMessage = lastMessageMap.get(room.getChatRoomId());
                  Long unreadCount = unreadCountMap.getOrDefault(room.getChatRoomId(), 0L);
                  return ChatConverter.toChatRoomSummaryDTO(room, lastMessage, unreadCount);
                })
            .toList();

    // 다음 커서는 리스트의 마지막 방의 lastMessageId를 사용
    Long nextCursor = rooms.isEmpty() ? null : rooms.get(rooms.size() - 1).getLastMessageId();

    return ChatConverter.toChatRoomListDTO(summaryList, nextCursor, hasNext);
  }

  @Override
  public Long getUnreadCount(Long roomId, Long userId) {
    // 채팅방 존재 여부 체크
    if (!chatRoomRepository.existsById(roomId)) {
      throw new ChatException(ChatErrorCode.CHAT_ROOM_NOT_FOUND);
    }

    // User ID로 카운트 쿼리 실행
    return chatMessageRepository.countByChatRoom_ChatRoomIdAndIsReadFalseAndSender_UserIdNot(
        roomId, userId);
  }
}

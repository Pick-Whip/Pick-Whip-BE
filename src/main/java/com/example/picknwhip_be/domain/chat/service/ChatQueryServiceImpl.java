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
import java.util.Comparator;
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
  public ChatResponseDTO.ChatRoomListDTO getChatRoomList(Long userId) {
    User user =
        userRepository
            .findById(userId)
            .orElseThrow(() -> new UserException(UserErrorCode.USER_NOT_FOUND));

    // 유저가 참여 중인 채팅방 목록 조회
    List<ChatRoom> rooms = chatRoomRepository.findAllByCustomerOrShopOwner(user);
    if (rooms.isEmpty()) {
      return ChatConverter.toChatRoomListDTO(Collections.emptyList());
    }

    // 채팅방 ID 리스트 추출
    List<Long> roomIds = rooms.stream().map(ChatRoom::getChatRoomId).toList();

    // 마지막 메시지들을 한꺼번에 조회하여 Map<RoomID, ChatMessage>으로 변환
    Map<Long, ChatMessage> lastMessageMap =
        chatMessageRepository.findLastMessagesByRoomIds(roomIds).stream()
            .collect(Collectors.toMap(m -> m.getChatRoom().getChatRoomId(), m -> m));

    // 안 읽은 개수들을 한꺼번에 조회하여 Map<RoomID, Long>으로 변환
    Map<Long, Long> unreadCountMap =
        chatMessageRepository.countUnreadMessagesByRoomIds(roomIds, user).stream()
            .collect(Collectors.toMap(obj -> (Long) obj[0], obj -> (Long) obj[1]));

    // 메모리에서 데이터 조합하여 DTO 생성
    List<ChatResponseDTO.ChatRoomSummaryDTO> summaryList =
        rooms.stream()
            .map(
                room -> {
                  ChatMessage lastMessage = lastMessageMap.get(room.getChatRoomId());
                  Long unreadCount = unreadCountMap.getOrDefault(room.getChatRoomId(), 0L);
                  return ChatConverter.toChatRoomSummaryDTO(room, lastMessage, unreadCount);
                })
            .sorted(
                Comparator.comparing(ChatResponseDTO.ChatRoomSummaryDTO::getLastMessageTime)
                    .reversed())
            .toList();

    return ChatConverter.toChatRoomListDTO(summaryList);
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

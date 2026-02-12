-- ======================================================
-- Chat Domain Seed Data (Repeatable)
-- Profile: local
-- Goal   : user_id=1 기준으로 Chat API 테스트 가능
-- ======================================================

/* ------------------------------------------------------
   Chat Room & Message 데이터

   시나리오:
   - user_id=1 (초코케이크)이 여러 가게와 채팅
   - 채팅방 1: user 1 <-> shop 1 (마포 스윗 케이크, owner: user 2)
   - 채팅방 2: user 1 <-> shop 2 (강남 스윗 케이크, owner: user 3)
   - 채팅방 3: user 4 <-> shop 1 (다른 고객의 채팅방)
   ------------------------------------------------------ */

-- 기존 데이터 삭제 (Idempotent를 위해)
DELETE FROM chat_messages WHERE room_id IN (
    SELECT chat_room_id FROM chat_rooms WHERE customer_id IN (1, 4)
);
DELETE FROM chat_rooms WHERE customer_id IN (1, 4);

/* ------------------------------------------------------
   1) 채팅방 생성
   ------------------------------------------------------ */
INSERT INTO chat_rooms (
    chat_room_id, customer_id, shop_id, last_message_id, created_at, updated_at
) VALUES
      -- 채팅방 1: user 1 <-> shop 1 (가장 최근 메시지 있음)
      (1, 1, 1, NULL, DATE_SUB(NOW(6), INTERVAL 3 DAY), NOW(6)),

      -- 채팅방 2: user 1 <-> shop 2 (조금 오래된 채팅방)
      (2, 1, 2, NULL, DATE_SUB(NOW(6), INTERVAL 7 DAY), DATE_SUB(NOW(6), INTERVAL 2 DAY)),

      -- 채팅방 3: user 4 <-> shop 1 (다른 고객의 채팅방 - 목록 테스트용)
      (3, 4, 1, NULL, DATE_SUB(NOW(6), INTERVAL 1 DAY), DATE_SUB(NOW(6), INTERVAL 1 HOUR))
    AS new
ON DUPLICATE KEY UPDATE
                     updated_at = new.updated_at;


/* ------------------------------------------------------
   2) 채팅 메시지 생성

   채팅방 1 (user 1 <-> shop 1):
   - 3일 전: 초기 문의 시작
   - 2일 전: 케이크 디자인 관련 대화
   - 1일 전: 가격 및 픽업 시간 문의
   - 오늘: 최종 확인 메시지 (읽지 않은 메시지 포함)
   ------------------------------------------------------ */

-- 채팅방 1의 메시지들
INSERT INTO chat_messages (
    chat_id, room_id, sender_id, message_type, message_text, message_image_url,
    is_read, created_at, updated_at
) VALUES
      -- 3일 전: 초기 문의
      (1, 1, 1, 'TEXT', '안녕하세요! 케이크 주문 문의드립니다.', NULL,
       true, DATE_SUB(NOW(6), INTERVAL 3 DAY), DATE_SUB(NOW(6), INTERVAL 3 DAY)),

      (2, 1, 2, 'TEXT', '안녕하세요! 마포 스윗 케이크입니다. 어떤 케이크를 찾으시나요?', NULL,
       true, DATE_SUB(NOW(6), INTERVAL 3 DAY), DATE_SUB(NOW(6), INTERVAL 3 DAY)),

      (3, 1, 1, 'TEXT', '생일 케이크를 주문하고 싶은데, 초코 케이크 가능한가요?', NULL,
       true, DATE_SUB(NOW(6), INTERVAL 3 DAY), DATE_SUB(NOW(6), INTERVAL 3 DAY)),

      -- 2일 전: 디자인 논의
      (4, 1, 2, 'TEXT', '네, 가능합니다! 사이즈는 어떤 걸로 원하시나요?', NULL,
       true, DATE_SUB(NOW(6), INTERVAL 2 DAY), DATE_SUB(NOW(6), INTERVAL 2 DAY)),

      (5, 1, 1, 'TEXT', '1호 사이즈로 부탁드립니다. 이런 디자인 가능할까요?', NULL,
       true, DATE_SUB(NOW(6), INTERVAL 2 DAY), DATE_SUB(NOW(6), INTERVAL 2 DAY)),

      (6, 1, 1, 'IMAGE', '디자인 참고 이미지입니다.', 'https://picknwhip-dev.s3.ap-northeast-2.amazonaws.com/chat/sample-design-1.jpg',
       true, DATE_SUB(NOW(6), INTERVAL 2 DAY), DATE_SUB(NOW(6), INTERVAL 2 DAY)),

      (7, 1, 2, 'TEXT', '좋은 디자인이네요! 비슷하게 제작 가능합니다.', NULL,
       true, DATE_SUB(NOW(6), INTERVAL 2 DAY), DATE_SUB(NOW(6), INTERVAL 2 DAY)),

      -- 1일 전: 가격 및 픽업 관련
      (8, 1, 1, 'TEXT', '가격은 얼마정도 될까요?', NULL,
       true, DATE_SUB(NOW(6), INTERVAL 1 DAY), DATE_SUB(NOW(6), INTERVAL 1 DAY)),

      (9, 1, 2, 'TEXT', '1호 사이즈 초코 케이크는 3만원입니다. 레터링 추가시 5천원 추가됩니다.', NULL,
       true, DATE_SUB(NOW(6), INTERVAL 1 DAY), DATE_SUB(NOW(6), INTERVAL 1 DAY)),

      (10, 1, 1, 'TEXT', '알겠습니다! 픽업은 언제 가능할까요?', NULL,
       true, DATE_SUB(NOW(6), INTERVAL 1 DAY), DATE_SUB(NOW(6), INTERVAL 1 DAY)),

      (11, 1, 2, 'TEXT', '주문 후 최소 3일 소요됩니다. 오늘 주문하시면 3일 후부터 픽업 가능해요.', NULL,
       true, DATE_SUB(NOW(6), INTERVAL 1 DAY), DATE_SUB(NOW(6), INTERVAL 1 DAY)),

      -- 오늘: 최종 확인 (일부 안읽은 메시지)
      (12, 1, 1, 'TEXT', '네, 그럼 정식으로 주문하겠습니다!', NULL,
       true, DATE_SUB(NOW(6), INTERVAL 3 HOUR), DATE_SUB(NOW(6), INTERVAL 3 HOUR)),

      (13, 1, 2, 'TEXT', '감사합니다! 주문 페이지에서 정식 주문 부탁드립니다.', NULL,
       false, DATE_SUB(NOW(6), INTERVAL 2 HOUR), DATE_SUB(NOW(6), INTERVAL 2 HOUR)),  -- 안읽음

      (14, 1, 2, 'TEXT', '추가로 궁금하신 점 있으시면 언제든 연락주세요!', NULL,
       false, DATE_SUB(NOW(6), INTERVAL 2 HOUR), DATE_SUB(NOW(6), INTERVAL 2 HOUR))   -- 안읽음
    AS new
ON DUPLICATE KEY UPDATE
                     message_text = new.message_text,
                     is_read = new.is_read;


-- 채팅방 2의 메시지들 (user 1 <-> shop 2)
INSERT INTO chat_messages (
    chat_id, room_id, sender_id, message_type, message_text, message_image_url,
    is_read, created_at, updated_at
) VALUES
      -- 7일 전: 초기 문의
      (15, 2, 1, 'TEXT', '안녕하세요, 케이크 주문 가능한가요?', NULL,
       true, DATE_SUB(NOW(6), INTERVAL 7 DAY), DATE_SUB(NOW(6), INTERVAL 7 DAY)),

      (16, 2, 3, 'TEXT', '안녕하세요! 강남 스윗 케이크입니다. 어떤 케이크 찾으시나요?', NULL,
       true, DATE_SUB(NOW(6), INTERVAL 7 DAY), DATE_SUB(NOW(6), INTERVAL 7 DAY)),

      (17, 2, 1, 'TEXT', '딸기 생크림 케이크 주문하고 싶어요.', NULL,
       true, DATE_SUB(NOW(6), INTERVAL 7 DAY), DATE_SUB(NOW(6), INTERVAL 7 DAY)),

      -- 6일 전
      (18, 2, 3, 'TEXT', '딸기 생크림 케이크 인기 많습니다! 1호 사이즈 45,000원입니다.', NULL,
       true, DATE_SUB(NOW(6), INTERVAL 6 DAY), DATE_SUB(NOW(6), INTERVAL 6 DAY)),

      (19, 2, 1, 'TEXT', '조금 비싸네요. 좀 더 저렴한 옵션은 없나요?', NULL,
       true, DATE_SUB(NOW(6), INTERVAL 6 DAY), DATE_SUB(NOW(6), INTERVAL 6 DAY)),

      -- 2일 전: 마지막 대화
      (20, 2, 3, 'TEXT', '도시락 사이즈는 25,000원입니다. 2-3인분 정도 되세요.', NULL,
       true, DATE_SUB(NOW(6), INTERVAL 2 DAY), DATE_SUB(NOW(6), INTERVAL 2 DAY)),

      (21, 2, 1, 'TEXT', '음.. 고민해볼게요. 감사합니다!', NULL,
       false, DATE_SUB(NOW(6), INTERVAL 2 DAY), DATE_SUB(NOW(6), INTERVAL 2 DAY))  -- 사장님 안읽음
    AS new
ON DUPLICATE KEY UPDATE
                     message_text = new.message_text,
                     is_read = new.is_read;


-- 채팅방 3의 메시지들 (user 4 <-> shop 1) - 다른 고객
INSERT INTO chat_messages (
    chat_id, room_id, sender_id, message_type, message_text, message_image_url,
    is_read, created_at, updated_at
) VALUES
      -- 1일 전
      (22, 3, 4, 'TEXT', '레터링 케이크 주문 가능한가요?', NULL,
       true, DATE_SUB(NOW(6), INTERVAL 1 DAY), DATE_SUB(NOW(6), INTERVAL 1 DAY)),

      (23, 3, 2, 'TEXT', '네, 가능합니다! 어떤 문구 원하시나요?', NULL,
       true, DATE_SUB(NOW(6), INTERVAL 1 DAY), DATE_SUB(NOW(6), INTERVAL 1 DAY)),

      -- 1시간 전
      (24, 3, 4, 'TEXT', '생일 축하해 라고 써주세요!', NULL,
       true, DATE_SUB(NOW(6), INTERVAL 1 HOUR), DATE_SUB(NOW(6), INTERVAL 1 HOUR)),

      (25, 3, 2, 'TEXT', '알겠습니다! 정식 주문 부탁드립니다.', NULL,
       false, DATE_SUB(NOW(6), INTERVAL 1 HOUR), DATE_SUB(NOW(6), INTERVAL 1 HOUR))
    AS new
ON DUPLICATE KEY UPDATE
                     message_text = new.message_text,
                     is_read = new.is_read;


/* ------------------------------------------------------
   3) 채팅방의 last_message_id 업데이트
   각 채팅방의 마지막 메시지 ID를 설정
   ------------------------------------------------------ */
UPDATE chat_rooms SET last_message_id = 14, updated_at = NOW(6) WHERE chat_room_id = 1;  -- 채팅방 1의 마지막 메시지
UPDATE chat_rooms SET last_message_id = 21, updated_at = NOW(6) WHERE chat_room_id = 2;  -- 채팅방 2의 마지막 메시지
UPDATE chat_rooms SET last_message_id = 25, updated_at = NOW(6) WHERE chat_room_id = 3;  -- 채팅방 3의 마지막 메시지


/* ------------------------------------------------------
   테스트 시나리오 정리:

-- 채팅방 생성/조회 API (POST /api/chats)
      - user 1이 shop 1, 2와 채팅방 생성/조회 가능
      - 이미 존재하는 채팅방 조회 가능

-- 채팅방 목록 조회 API (GET /api/chats)
      - user 1: 채팅방 1, 2 조회됨 (총 2개)
      - user 2 (shop 1 사장): 채팅방 1, 3 조회됨 (총 2개)
      - user 3 (shop 2 사장): 채팅방 2 조회됨 (총 1개)
      - keyword 검색: "마포" -> 채팅방 1, "강남" -> 채팅방 2

-- 메시지 내역 조회 API (GET /api/chats/{roomId}/messages)
      - 채팅방 1: 총 14개 메시지 (cursor 기반 페이징 테스트 가능)
      - 채팅방 2: 총 7개 메시지
      - 채팅방 3: 총 4개 메시지
      - 상대방 메시지 읽음 처리 테스트 가능

-- 메시지 전송 (WebSocket - MessageMapping)
      - user 1 -> shop 1, 2에 메시지 전송 가능
      - 실시간 읽지않은 메시지 카운트 업데이트

-- 이미지 업로드 URL 발급 API (POST /api/chats/{roomId}/images)
      - 채팅방 참여자만 Presigned URL 발급 가능
      - 권한 체크 테스트 가능

-- 통계 데이터:
      - user 1의 안읽은 메시지: 2개 (채팅방 1에서 메시지 13, 14)
      - user 2의 안읽은 메시지: 1개 (채팅방 2에서 메시지 21) + 1개 (채팅방 3에서 메시지 25)
      - user 3의 안읽은 메시지: 0개
      - user 4의 안읽은 메시지: 0개
   ------------------------------------------------------ */
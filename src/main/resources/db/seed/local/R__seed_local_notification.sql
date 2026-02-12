-- ======================================================
-- Notification Domain Seed Data (Repeatable)
-- Profile: local
-- Goal   : user_id=1 기준으로 Notification API 테스트 가능
-- ======================================================

/* ------------------------------------------------------
   Notification 데이터

   시나리오:
   - user_id=1 (초코케이크)의 알림 데이터
   - ORDER / REVIEW / ETC 타입 골고루 분포
   - 모든 NotificationKind 포함
   - 읽은 알림(is_read=true) / 안 읽은 알림(is_read=false) 혼합
   ------------------------------------------------------ */

-- 기존 데이터 삭제 (Idempotent를 위해)
DELETE FROM notification WHERE user_id IN (1, 4);

-- user_id=1 알림 데이터 (다양한 kind, type, 읽음/안읽음 혼합)
INSERT INTO notification (
    id, created_at, updated_at,
    user_id, type, kind, target_id, title, content, is_read
) VALUES
    -- ORDER 타입 알림들
    (1, DATE_SUB(NOW(6), INTERVAL 7 DAY), DATE_SUB(NOW(6), INTERVAL 7 DAY),
     1, 'ORDER', 'ORDER_SHEET_CHECKING', 1,
     '주문서가 전달되었습니다', '마포 스윗 케이크에서 주문서를 확인 후 알려드릴게요!', true),

    (2, DATE_SUB(NOW(6), INTERVAL 6 DAY), DATE_SUB(NOW(6), INTERVAL 6 DAY),
     1, 'ORDER', 'ORDER_PAYMENT_REQUESTED', 1,
     '결제가 완료되면 주문이 확정됩니다', '마포 스윗 케이크에서 주문을 확정했어요.\n결제를 진행해주세요.', true),

    (3, DATE_SUB(NOW(6), INTERVAL 5 DAY), DATE_SUB(NOW(6), INTERVAL 5 DAY),
     1, 'ORDER', 'ORDER_MAKING', 1,
     '사장님이 결제를 확인했어요', '마포 스윗 케이크에서 케이크 제작을 시작했어요. 조금만 기다려주시면 정성껏 준비해드릴게요!', true),

    (4, DATE_SUB(NOW(6), INTERVAL 4 DAY), DATE_SUB(NOW(6), INTERVAL 4 DAY),
     1, 'ORDER', 'ORDER_PICKUP_READY', 1,
     '케이크 픽업이 준비되었습니다', '마포 스윗 케이크에서 케이크가 준비되었어요. 픽업 시간을 확인해주세요.', true),

    (5, DATE_SUB(NOW(6), INTERVAL 3 DAY), DATE_SUB(NOW(6), INTERVAL 3 DAY),
     1, 'ORDER', 'ORDER_REJECTED', 2,
     '주문 제작이 어려워요', '강남 스윗 케이크에서 주문서를 확인했습니다. 사장님과 채팅으로 상담해보세요.', true),

    (6, DATE_SUB(NOW(6), INTERVAL 2 DAY), DATE_SUB(NOW(6), INTERVAL 2 DAY),
     1, 'ORDER', 'ORDER_REJECTED_PAYMENT_FAILED', NULL,
     '결제가 정상적으로 처리되지 않았어요', '결제가 완료되지 않았거나, 결제 과정에 오류가 있을 수 있으니 다시 결제를 진행해주세요.', false),

    (7, DATE_SUB(NOW(6), INTERVAL 1 DAY), DATE_SUB(NOW(6), INTERVAL 1 DAY),
     1, 'ORDER', 'ORDER_SHEET_CHECKING', 3,
     '주문서가 전달되었습니다', '강남 스윗 케이크에서 주문서를 확인 후 알려드릴게요!', false),

    (8, DATE_SUB(NOW(6), INTERVAL 12 HOUR), DATE_SUB(NOW(6), INTERVAL 12 HOUR),
     1, 'ORDER', 'ORDER_PAYMENT_REQUESTED', 3,
     '결제가 완료되면 주문이 확정됩니다', '강남 스윗 케이크에서 주문을 확정했어요.\n결제를 진행해주세요.', false),

    -- REVIEW 타입 알림들
    (9, DATE_SUB(NOW(6), INTERVAL 4 DAY), DATE_SUB(NOW(6), INTERVAL 4 DAY),
     1, 'REVIEW', 'REVIEW_WRITE_REQUESTED', 1,
     '리뷰를 작성해주세요', '케이크는 어떠셨나요? 소중한 후기를 남겨주세요!', true),

    (10, DATE_SUB(NOW(6), INTERVAL 3 DAY), DATE_SUB(NOW(6), INTERVAL 3 DAY),
     1, 'REVIEW', 'REVIEW_REPLY_CREATED', 1,
     '사장님이 답변을 남겼어요', '작성하신 리뷰에 마포 스윗 케이크 사장님이 답변을 남겼습니다.', true),

    (11, DATE_SUB(NOW(6), INTERVAL 8 HOUR), DATE_SUB(NOW(6), INTERVAL 8 HOUR),
     1, 'REVIEW', 'REVIEW_WRITE_REQUESTED', 4,
     '리뷰를 작성해주세요', '케이크는 어떠셨나요? 소중한 후기를 남겨주세요!', false),

    (12, DATE_SUB(NOW(6), INTERVAL 2 HOUR), DATE_SUB(NOW(6), INTERVAL 2 HOUR),
     1, 'REVIEW', 'REVIEW_REPLY_CREATED', 2,
     '사장님이 답변을 남겼어요', '작성하신 리뷰에 강남 스윗 케이크 사장님이 답변을 남겼습니다.', false),

    -- ETC 타입 알림들
    (13, DATE_SUB(NOW(6), INTERVAL 6 DAY), DATE_SUB(NOW(6), INTERVAL 6 DAY),
     1, 'ETC', 'REPORT_RECEIVED', NULL,
     '신고가 접수되었습니다', '3-5 영업일 내에 검토하여 답변 드리겠습니다.', true),

    (14, DATE_SUB(NOW(6), INTERVAL 5 DAY), DATE_SUB(NOW(6), INTERVAL 5 DAY),
     1, 'ETC', 'REPORT_REPLIED', NULL,
     '접수하신 신고에 대해 안내드립니다', '신고하신 내용에 대한 답변 드립니다.', true),

    (15, DATE_SUB(NOW(6), INTERVAL 1 DAY), DATE_SUB(NOW(6), INTERVAL 1 DAY),
     1, 'ETC', 'EVENT', NULL,
     '크리스마스 특별 할인', '12월 한정! 크리스마스 케이크 10% 할인 이벤트가 진행중이에요.', false),

    (16, DATE_SUB(NOW(6), INTERVAL 30 MINUTE), DATE_SUB(NOW(6), INTERVAL 30 MINUTE),
     1, 'ETC', 'REPORT_RECEIVED', NULL,
     '신고가 접수되었습니다', '3-5 영업일 내에 검토하여 답변 드리겠습니다.', false)

ON DUPLICATE KEY UPDATE
    user_id    = VALUES(user_id),
    type       = VALUES(type),
    kind       = VALUES(kind),
    target_id  = VALUES(target_id),
    title      = VALUES(title),
    content    = VALUES(content),
    is_read    = VALUES(is_read),
    updated_at = VALUES(updated_at);

-- user_id=4 알림 데이터 (다른 유저의 알림도 존재하는 상황 테스트)
INSERT INTO notification (
    id, created_at, updated_at,
    user_id, type, kind, target_id, title, content, is_read
) VALUES
    (17, DATE_SUB(NOW(6), INTERVAL 2 DAY), DATE_SUB(NOW(6), INTERVAL 2 DAY),
     4, 'ORDER', 'ORDER_SHEET_CHECKING', 5,
     '주문서가 전달되었습니다', '마포 스윗 케이크에서 주문서를 확인 후 알려드릴게요!', false),

    (18, DATE_SUB(NOW(6), INTERVAL 1 DAY), DATE_SUB(NOW(6), INTERVAL 1 DAY),
     4, 'REVIEW', 'REVIEW_WRITE_REQUESTED', 5,
     '리뷰를 작성해주세요', '케이크는 어떠셨나요? 소중한 후기를 남겨주세요!', false),

    (19, DATE_SUB(NOW(6), INTERVAL 6 HOUR), DATE_SUB(NOW(6), INTERVAL 6 HOUR),
     4, 'ETC', 'EVENT', NULL,
     '크리스마스 특별 할인', '12월 한정! 크리스마스 케이크 10% 할인 이벤트가 진행중이에요.', true)

ON DUPLICATE KEY UPDATE
    user_id    = VALUES(user_id),
    type       = VALUES(type),
    kind       = VALUES(kind),
    target_id  = VALUES(target_id),
    title      = VALUES(title),
    content    = VALUES(content),
    is_read    = VALUES(is_read),
    updated_at = VALUES(updated_at);

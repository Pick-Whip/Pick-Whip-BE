-- ======================================================
-- Best Custom Reviews Seed Data (Repeatable) - FIXED v3
-- Goal   : Test 'Best Custom Reviews' API
-- Context: '이대로 주문하기(Order as is)' 주문 건들에 대한 리뷰 데이터
--          사용자는 디자인/맛 등을 변경하지 않고 레터링 등만 변경함.
--          따라서 각 주문의 옵션은 해당 디자인의 원본 레시피를 따라가야 함.
-- ======================================================

-- =====================================================
-- 1. 커스텀 옵션 데이터 추가 (아이싱/토핑 종류별 ID 분리)
--    디자인별로 지정된 아이싱/토핑의 컬러를 정확히 표현하기 위해 옵션 정의 추가
-- =====================================================
INSERT INTO custom_options (id, shop_id, category, option_name, additional_price, color_rgb_code)
VALUES
    (11, 1, 'ICING',   '초코 아이싱',   2000, '#5D4037'),
    (12, 1, 'ICING',   '화이트 아이싱',  0,    '#FFFFFF'),
    (13, 1, 'ICING',   '연노랑 아이싱',  2000, '#FFFFE0'),
    (14, 1, 'TOPPING', '진주 스프링클',  1000, NULL),
    (15, 1, 'TOPPING', '하트 스프링클',  1000, NULL)
    AS new
ON DUPLICATE KEY UPDATE
                     option_name = new.option_name,
                     color_rgb_code = new.color_rgb_code;


-- =====================================================
-- 2. 주문 데이터 (5개 - shop 1 기반, order id 37~41)
-- =====================================================
INSERT INTO orders (
    id, user_id, shop_id, shop_cake_size_id, design_id,
    status, pickup_datetime,
    lettering_text, lettering_line_count, lettering_alignment,
    additional_request, order_additional_request,
    reference_image_url, payment_method, payment_status,
    total_price, deposit_amount,
    created_at, updated_at,
    order_code, customer_name, customer_phone
) VALUES
      -- 주문 37: 로맨틱 플라워 (Design 5) - "핑크 장미 많이"는 디자인 디테일 요청
      (37, 7, 1, 2, 5, 'COMPLETED', '2026-01-20 14:00:00',
       '사랑해요 ♥', 'ONE_LINE', 'CENTER',
       '핑크 장미 많이 올려주세요', '포장 예쁘게 부탁드려요',
       'https://example.com/cake_best1.jpg',
       'CARD', 'PAID', 58000, 20000,
       '2026-01-10 10:00:00', '2026-01-10 10:00:00',
       '260110_201', '초코러버', '010-7777-7777'),

      -- 주문 38: 초코 드림 (Design 6) - "초코 진하게"는 맛/데코 관련 요청
      (38, 8, 1, 2, 6, 'COMPLETED', '2026-01-21 15:00:00',
       'Happy Birthday', 'ONE_LINE', 'CENTER',
       '초코 진하게 해주세요', NULL,
       'https://example.com/cake_best2.jpg',
       'CARD', 'PAID', 48500, 20000,
       '2026-01-11 11:00:00', '2026-01-11 11:00:00',
       '260111_201', '딸기공주', '010-8888-8888'),

      -- 주문 39: 아이돌 포토 하트 (Design 3) - "포토카드" 등 데코 관련 요청
      (39, 4, 1, 2, 3, 'COMPLETED', '2026-01-22 16:00:00',
       '축하합니다', 'ONE_LINE', 'CENTER',
       '포토카드 2장 넣어주세요', '케이크 칼 같이 주세요',
       'https://example.com/cake_best3.jpg',
       'CARD', 'PAID', 59500, 20000,
       '2026-01-12 12:00:00', '2026-01-12 12:00:00',
       '260112_201', '케이크마니아', '010-4444-4444'),

      -- 주문 40: 심플 플라워 (Design 1)
      (40, 5, 1, 2, 1, 'COMPLETED', '2026-01-23 17:00:00',
       'Congratulations', 'ONE_LINE', 'CENTER',
       '딸기 많이 올려주세요', NULL,
       'https://example.com/cake_best4.jpg',
       'CARD', 'PAID', 38000, 20000,
       '2026-01-13 13:00:00', '2026-01-13 13:00:00',
       '260113_201', '달달구리', '010-5555-5555'),

      -- 주문 41: 미니멀 레터링 (Design 2) - "레터링 굵게"는 전형적인 이대로 주문하기의 변경 요청
      (41, 6, 1, 2, 2, 'COMPLETED', '2026-01-24 18:00:00',
       '감사합니다', 'ONE_LINE', 'CENTER',
       '레터링 굵게 해주세요', '픽업 30분 전에 연락주세요',
       'https://example.com/cake_best5.jpg',
       'CARD', 'PAID', 36000, 20000,
       '2026-01-14 14:00:00', '2026-01-14 14:00:00',
       '260114_201', '생크림왕', '010-6666-6666')
    AS new
ON DUPLICATE KEY UPDATE
                     status         = new.status,
                     total_price    = new.total_price,
                     deposit_amount = new.deposit_amount;


-- =====================================================
-- 3. 주문 아이템 (커스텀 옵션 매핑) - [핵심 수정]
--    각 디자인(주문)에 고정된 아이싱/토핑 옵션을 정확한 custom_option_id로 매핑
-- =====================================================
INSERT INTO order_items (
    id, order_id, custom_option_id, option_category, option_name, unit_price, color_rgb_code, position_x, position_y
) VALUES
      -- 주문 37 (로맨틱 플라워): 1호 + 원형(9) + 바닐라(1) + 생크림(3) + 딸기(5) + 금박(7) + 핑크 아이싱(8)
      (37000, 37, 9, 'SHAPE',   '원형',         0,    NULL,       NULL, NULL),
      (37001, 37, 1, 'SHEET',   '바닐라 시트',  0,    '#F5F5DC',  NULL, NULL),
      (37002, 37, 3, 'CREAM',   '생크림',       0,    '#FFFFFF',  NULL, NULL),
      (37003, 37, 5, 'TOPPING', '딸기',         3000, NULL,       NULL, NULL),
      (37004, 37, 7, 'TOPPING', '금박',         8000, NULL,       NULL, NULL),
      (37005, 37, 8, 'ICING',   '핑크 아이싱',  2000, '#FFB6C1',  NULL, NULL), -- ID 8 (핑크)

      -- 주문 38 (초코 드림): 1호 + 원형(9) + 초코(2) + 초코(4) + 마카롱(6) + 초코 아이싱(11)
      (38000, 38, 9, 'SHAPE',   '원형',         0,    NULL,       NULL, NULL),
      (38001, 38, 2, 'SHEET',   '초코 시트',    1000, '#8B4513',  NULL, NULL),
      (38002, 38, 4, 'CREAM',   '초코 크림',    1500, '#8B4513',  NULL, NULL),
      (38003, 38, 6, 'TOPPING', '마카롱',       5000, NULL,       NULL, NULL),
      (38004, 38, 11,'ICING',   '초코 아이싱',  2000, '#5D4037',  NULL, NULL), -- [수정] ID 11 (초코)

      -- 주문 39 (아이돌 포토 하트): 1호 + 하트(10) + 바닐라(1) + 생크림(3) + 딸기(5) + 화이트 아이싱(12)
      (39000, 39, 10,'SHAPE',   '하트',         2000, NULL,       NULL, NULL),
      (39001, 39, 1, 'SHEET',   '바닐라 시트',  0,    '#F5F5DC',  NULL, NULL),
      (39002, 39, 3, 'CREAM',   '생크림',       0,    '#FFFFFF',  NULL, NULL),
      (39003, 39, 5, 'TOPPING', '딸기',         3000, NULL,       NULL, NULL),
      (39004, 39, 12,'ICING',   '화이트 아이싱', 0,    '#FFFFFF',  NULL, NULL), -- [수정] ID 12 (화이트)

      -- 주문 40 (심플 플라워): 1호 + 원형(9) + 바닐라(1) + 생크림(3) + 진주 스프링클(14) + 연노랑 아이싱(13)
      (40000, 40, 9, 'SHAPE',   '원형',         0,    NULL,       NULL, NULL),
      (40001, 40, 1, 'SHEET',   '바닐라 시트',  0,    '#F5F5DC',  NULL, NULL),
      (40002, 40, 3, 'CREAM',   '생크림',       0,    '#FFFFFF',  NULL, NULL),
      (40003, 40, 14,'TOPPING', '진주 스프링클', 1000, NULL,       NULL, NULL), -- [수정] ID 14 (진주)
      (40004, 40, 13,'ICING',   '연노랑 아이싱', 2000, '#FFFFE0',  NULL, NULL), -- [수정] ID 13 (연노랑)

      -- 주문 41 (미니멀 레터링): 1호 + 원형(9) + 초코(2) + 생크림(3) + 하트 스프링클(15) + 화이트 아이싱(12)
      (41000, 41, 9, 'SHAPE',   '원형',         0,    NULL,       NULL, NULL),
      (41001, 41, 2, 'SHEET',   '초코 시트',    1000, '#8B4513',  NULL, NULL),
      (41002, 41, 3, 'CREAM',   '생크림',       0,    '#FFFFFF',  NULL, NULL),
      (41003, 41, 15,'TOPPING', '하트 스프링클', 1000, NULL,       NULL, NULL), -- [수정] ID 15 (하트)
      (41004, 41, 12,'ICING',   '화이트 아이싱', 0,    '#FFFFFF',  NULL, NULL)  -- [수정] ID 12 (화이트)
    AS new
ON DUPLICATE KEY UPDATE
                     custom_option_id = new.custom_option_id,
                     unit_price = new.unit_price,
                     color_rgb_code = new.color_rgb_code;


-- =====================================================
-- 4. 리뷰 데이터 (review id 8~12)
--    agreement=true, design_id 있음 -> best custom reviews API 대상
-- =====================================================
INSERT INTO review (
    id, order_id, user_id, shop_id, design_id,
    rating, content, agreement,
    created_at, updated_at, deleted_at
) VALUES
      -- 리뷰 8: 로맨틱 플라워 (좋아요 15개 예정 - BEST 1위)
      (8, 37, 7, 1, 5, 5,
       '정말 너무 예뻐요! 핑크 장미 장식이 생각보다 훨씬 화려하고 금박도 고급스러워요. 맛도 달지 않고 부드러워서 최고였습니다. 생일 케이크로 강추합니다!',
       true, '2026-01-21 20:00:00', '2026-01-21 20:00:00', NULL),

      -- 리뷰 9: 초코 드림 (좋아요 12개 예정 - BEST 2위)
      (9, 38, 8, 1, 6, 5,
       '초코 마니아라면 꼭 드셔보세요! 초코 시트와 초코 크림의 조화가 환상적이에요. 너무 달지 않고 진한 초콜릿 맛이 일품입니다. 마카롱도 맛있어요!',
       true, '2026-01-22 19:00:00', '2026-01-22 19:00:00', NULL),

      -- 리뷰 10: 아이돌 포토 하트 (좋아요 10개 예정 - BEST 3위)
      (10, 39, 4, 1, 3, 4,
       '딸기와 블루베리가 정말 신선했어요! 상큼한 맛이 좋았고 비주얼도 화려해서 사진 찍기 좋았습니다. 포토카드도 예쁘게 넣어주셔서 감사해요.',
       true, '2026-01-23 18:00:00', '2026-01-23 18:00:00', NULL),

      -- 리뷰 11: 심플 플라워 (좋아요 8개 예정 - BEST 4위)
      (11, 40, 5, 1, 1, 5,
       '결혼 기념일 케이크로 주문했는데 정말 우아하고 고급스러웠어요. 딸기도 싱싱하고 플라워 장식이 너무 예뻐서 감동받았습니다!',
       true, '2026-01-24 17:00:00', '2026-01-24 17:00:00', NULL),

      -- 리뷰 12: 미니멀 레터링 (좋아요 6개 예정 - BEST 5위)
      (12, 41, 6, 1, 2, 4,
       '레터링이 너무 깔끔하고 예뻐요! 졸업 선물로 주문했는데 받는 사람이 너무 좋아했습니다. 다음에도 또 주문할게요.',
       true, '2026-01-25 16:00:00', '2026-01-25 16:00:00', NULL)
    AS new
ON DUPLICATE KEY UPDATE
                     rating   = new.rating,
                     content  = new.content;


-- =====================================================
-- 5. 리뷰 선택 키워드
-- =====================================================
INSERT INTO review_selected_keyword (id, review_id, keyword_id)
VALUES
    -- 리뷰 8 키워드
    (8001, 8, 2),
    (8002, 8, 10),
    (8003, 8, 15),
    -- 리뷰 9 키워드
    (9001, 9, 3),
    (9002, 9, 10),
    (9003, 9, 8),
    -- 리뷰 10 키워드
    (10001, 10, 2),
    (10002, 10, 11),
    -- 리뷰 11 키워드
    (11001, 11, 2),
    (11002, 11, 10),
    (11003, 11, 20),
    -- 리뷰 12 키워드
    (12001, 12, 3),
    (12002, 12, 10)
    AS new
ON DUPLICATE KEY UPDATE keyword_id = new.keyword_id;


-- =====================================================
-- 6. 리뷰 좋아요 (user 9~23 기반)
-- =====================================================
-- 리뷰 8에 15개 좋아요 (BEST 1위)
INSERT INTO review_like (review_id, user_id, created_at, updated_at)
SELECT 8, user_id, NOW(6), NOW(6)
FROM users
WHERE user_id BETWEEN 9 AND 23
    ON DUPLICATE KEY UPDATE updated_at = NOW(6);

-- 리뷰 9에 12개 좋아요 (BEST 2위)
INSERT INTO review_like (review_id, user_id, created_at, updated_at)
SELECT 9, user_id, NOW(6), NOW(6)
FROM users
WHERE user_id BETWEEN 9 AND 20
    ON DUPLICATE KEY UPDATE updated_at = NOW(6);

-- 리뷰 10에 10개 좋아요 (BEST 3위)
INSERT INTO review_like (review_id, user_id, created_at, updated_at)
SELECT 10, user_id, NOW(6), NOW(6)
FROM users
WHERE user_id BETWEEN 9 AND 18
    ON DUPLICATE KEY UPDATE updated_at = NOW(6);

-- 리뷰 11에 8개 좋아요 (BEST 4위)
INSERT INTO review_like (review_id, user_id, created_at, updated_at)
SELECT 11, user_id, NOW(6), NOW(6)
FROM users
WHERE user_id BETWEEN 9 AND 16
    ON DUPLICATE KEY UPDATE updated_at = NOW(6);

-- 리뷰 12에 6개 좋아요 (BEST 5위)
INSERT INTO review_like (review_id, user_id, created_at, updated_at)
SELECT 12, user_id, NOW(6), NOW(6)
FROM users
WHERE user_id BETWEEN 9 AND 14
    ON DUPLICATE KEY UPDATE updated_at = NOW(6);
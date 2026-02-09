-- ======================================================
-- Best Custom Reviews Seed Data (Repeatable)
-- Goal   : Test 'Best Custom Reviews' API
-- Pattern: Self-Contained & Idempotent
-- ======================================================

-- =====================================================
-- 0. 사전 준비: Shop Owner & Shop 생성 (ID: 1)
-- =====================================================
-- Shop Owner 유저 생성
INSERT INTO users (
    user_id,
    created_at,
    updated_at,
    email,
    kakao_id,
    name,
    nickname,
    phone,
    status
) VALUES (
             1,
             NOW(6),
             NOW(6),
             'local.owner@picknwhip.com',
             1000000001,
             '사장님',
             '사장님',
             '010-0000-0000',
             'ACTIVE'
         ) AS new
ON DUPLICATE KEY UPDATE
                     updated_at = new.updated_at,
                     status = new.status;

-- Shop 생성 (ID: 1)
INSERT INTO shops (
    shop_id,
    average_rating,
    chat_nickname,
    chat_profile_image_url,
    created_at,
    day_order_guide,
    description,
    location,
    min_price,
    parking_guide,
    payment_notice,
    phone,
    pickup_time_guide,
    precaution_notice,
    prepayment,
    registration_image_url,
    shop_image_url,
    shop_name,
    status,
    verification_status,
    owner_id,
    address
) VALUES (
             1,
             NULL,
             '테스트 케이크샵',
             NULL,
             NOW(6),
             NULL,
             '로컬 개발용 테스트 케이크샵입니다.',
             ST_GeomFromText('POINT(37.5665 126.9780)', 4326),
             NULL,
             NULL,
             NULL,
             '02-1234-5678',
             NULL,
             NULL,
             NULL,
             NULL,
             NULL,
             '테스트 케이크샵',
             'ACTIVE',
             'VERIFIED',
             1,
             '서울특별시 중구'
         ) AS new
ON DUPLICATE KEY UPDATE
                     phone = new.phone,
                     shop_name = new.shop_name,
                     status = new.status,
                     verification_status = new.verification_status;

-- Shop Cake Size 생성 (ID: 1)
INSERT INTO shop_cake_sizes (
    size_id,
    shop_id,
    size_name,
    diameter,
    price
) VALUES (
             1,
             1,
             '1호',
             '15cm',
             30000
         ) AS new
ON DUPLICATE KEY UPDATE
                     price = new.price;


-- =====================================================
-- 1. 리뷰 작성자 유저 생성 (5명)
-- =====================================================
INSERT INTO users (
    user_id,
    created_at,
    updated_at,
    email,
    kakao_id,
    name,
    nickname,
    phone,
    profile_image_url,
    status
) VALUES
      (201, NOW(6), NOW(6), 'reviewer1@test.com', 2000000201, '리뷰어1', '초코러버', '010-2001-0001', NULL, 'ACTIVE'),
      (202, NOW(6), NOW(6), 'reviewer2@test.com', 2000000202, '리뷰어2', '딸기공주', '010-2002-0002', NULL, 'ACTIVE'),
      (203, NOW(6), NOW(6), 'reviewer3@test.com', 2000000203, '리뷰어3', '케이크마니아', '010-2003-0003', NULL, 'ACTIVE'),
      (204, NOW(6), NOW(6), 'reviewer4@test.com', 2000000204, '리뷰어4', '달달구리', '010-2004-0004', NULL, 'ACTIVE'),
      (205, NOW(6), NOW(6), 'reviewer5@test.com', 2000000205, '리뷰어5', '생크림왕', '010-2005-0005', NULL, 'ACTIVE')
    AS new
ON DUPLICATE KEY UPDATE
                     email = new.email,
                     nickname = new.nickname,
                     status = new.status;


-- =====================================================
-- 2. 커스텀 옵션 데이터 (기존 Shop ID: 1 사용)
-- =====================================================
INSERT INTO custom_options (
    id, shop_id, category, option_name, additional_price, color_rgb_code
) VALUES
      -- SHEET (시트)
      (101, 1, 'SHEET', '초코 시트', 2000, '#8B4513'),
      (102, 1, 'SHEET', '딸기 시트', 2500, '#FFB6C1'),
      (103, 1, 'SHEET', '녹차 시트', 3000, '#90EE90'),

      -- CREAM (크림)
      (104, 1, 'CREAM', '생크림', 0, '#FFFFFF'),
      (105, 1, 'CREAM', '초코 크림', 1500, '#8B4513'),
      (106, 1, 'CREAM', '치즈 크림', 2000, '#FFF8DC'),

      -- TOPPING (토핑/데코)
      (107, 1, 'TOPPING', '딸기', 3000, NULL),
      (108, 1, 'TOPPING', '블루베리', 3500, NULL),
      (109, 1, 'TOPPING', '마카롱', 5000, NULL),
      (110, 1, 'TOPPING', '금박', 8000, NULL),

      -- ICING (아이싱)
      (111, 1, 'ICING', '핑크 아이싱', 2000, '#FFB6C1'),
      (112, 1, 'ICING', '블루 아이싱', 2000, '#87CEEB')
    AS new
ON DUPLICATE KEY UPDATE
                     additional_price = new.additional_price,
                     color_rgb_code = new.color_rgb_code;


-- =====================================================
-- 3. 디자인 갤러리 추가 (다양한 디자인)
-- =====================================================
INSERT INTO design_gallery (
    id, shop_id, shop_cake_size_id, design_name, base_price, description
) VALUES
      (101, 1, 1, '로맨틱 플라워', 45000, '핑크 장미와 금박 장식'),
      (102, 1, 1, '초코 드림', 42000, '진한 초코 시트와 초코 크림'),
      (103, 1, 1, '베리 파라다이스', 48000, '딸기와 블루베리 가득'),
      (104, 1, 1, '엘레강스 화이트', 50000, '화이트 크림과 마카롱'),
      (105, 1, 1, '그린티 블리스', 43000, '녹차 시트와 치즈 크림')
    AS new
ON DUPLICATE KEY UPDATE
                     design_name = new.design_name,
                     base_price = new.base_price;


-- =====================================================
-- 4. 주문 데이터 (5개 - 각 리뷰어당 1개씩)
-- =====================================================
INSERT INTO orders (
    id,
    user_id,
    shop_id,
    shop_cake_size_id,
    design_id,
    status,
    pickup_datetime,
    lettering_text,
    lettering_line_count,
    lettering_alignment,
    additional_request,
    order_additional_request,
    reference_image_url,
    payment_method,
    payment_status,
    total_price,
    deposit_amount,
    created_at,
    updated_at,
    order_code,
    customer_name,
    customer_phone
) VALUES
      -- 주문 1: 로맨틱 플라워 (가장 많은 좋아요 예정)
      (201, 201, 1, 1, 101, 'COMPLETED', '2026-01-20 14:00:00',
       '사랑해요 ♥', 'ONE_LINE', 'CENTER',
       '핑크 장미 많이 올려주세요', '포장 예쁘게 부탁드려요',
       'https://example.com/cake1.jpg',
       'CARD', 'PAID', 58000, 0,
       '2026-01-10 10:00:00', '2026-01-10 10:00:00',
       '260110_101', '초코러버', '010-2001-0001'),

      -- 주문 2: 초코 드림 (두번째로 많은 좋아요)
      (202, 202, 1, 1, 102, 'COMPLETED', '2026-01-21 15:00:00',
       'Happy Birthday', 'ONE_LINE', 'CENTER',
       '초코 진하게 해주세요', NULL,
       'https://example.com/cake2.jpg',
       'CARD', 'PAID', 48500, 0,
       '2026-01-11 11:00:00', '2026-01-11 11:00:00',
       '260111_101', '딸기공주', '010-2002-0002'),

      -- 주문 3: 베리 파라다이스
      (203, 203, 1, 1, 103, 'COMPLETED', '2026-01-22 16:00:00',
       '축하합니다', 'ONE_LINE', 'CENTER',
       '베리류 신선한 것으로', '케이크 칼 같이 주세요',
       'https://example.com/cake3.jpg',
       'CARD', 'PAID', 59500, 0,
       '2026-01-12 12:00:00', '2026-01-12 12:00:00',
       '260112_101', '케이크마니아', '010-2003-0003'),

      -- 주문 4: 엘레강스 화이트
      (204, 204, 1, 1, 104, 'COMPLETED', '2026-01-23 17:00:00',
       'Congratulations', 'ONE_LINE', 'CENTER',
       '마카롱 색상 파스텔톤으로', NULL,
       'https://example.com/cake4.jpg',
       'CARD', 'PAID', 63000, 0,
       '2026-01-13 13:00:00', '2026-01-13 13:00:00',
       '260113_101', '달달구리', '010-2004-0004'),

      -- 주문 5: 그린티 블리스
      (205, 205, 1, 1, 105, 'COMPLETED', '2026-01-24 18:00:00',
       '감사합니다', 'ONE_LINE', 'CENTER',
       '녹차 향 진하게', '픽업 30분 전에 연락주세요',
       'https://example.com/cake5.jpg',
       'CARD', 'PAID', 48000, 0,
       '2026-01-14 14:00:00', '2026-01-14 14:00:00',
       '260114_101', '생크림왕', '010-2005-0005')
    AS new
ON DUPLICATE KEY UPDATE
                     status = new.status,
                     total_price = new.total_price;


-- =====================================================
-- 5. 주문 아이템 (커스텀 옵션 매핑)
-- =====================================================
-- 주문 201: 로맨틱 플라워 (딸기 시트 + 생크림 + 딸기 토핑 + 금박 + 핑크 아이싱)
INSERT INTO order_items (
    id, order_id, custom_option_id, option_category, option_name, unit_price, color_rgb_code, position_x, position_y
) VALUES
      (2001, 201, 102, 'SHEET', '딸기 시트', 2500, '#FFB6C1', NULL, NULL),
      (2002, 201, 104, 'CREAM', '생크림', 0, '#FFFFFF', NULL, NULL),
      (2003, 201, 107, 'TOPPING', '딸기', 3000, NULL, NULL, NULL),
      (2004, 201, 110, 'TOPPING', '금박', 8000, NULL, NULL, NULL),
      (2005, 201, 111, 'ICING', '핑크 아이싱', 2000, '#FFB6C1', NULL, NULL)
    AS new
ON DUPLICATE KEY UPDATE unit_price = new.unit_price;

-- 주문 202: 초코 드림 (초코 시트 + 초코 크림 + 마카롱)
INSERT INTO order_items (
    id, order_id, custom_option_id, option_category, option_name, unit_price, color_rgb_code, position_x, position_y
) VALUES
      (2006, 202, 101, 'SHEET', '초코 시트', 2000, '#8B4513', NULL, NULL),
      (2007, 202, 105, 'CREAM', '초코 크림', 1500, '#8B4513', NULL, NULL),
      (2008, 202, 109, 'TOPPING', '마카롱', 5000, NULL, NULL, NULL)
    AS new
ON DUPLICATE KEY UPDATE unit_price = new.unit_price;

-- 주문 203: 베리 파라다이스 (딸기 시트 + 생크림 + 딸기 + 블루베리)
INSERT INTO order_items (
    id, order_id, custom_option_id, option_category, option_name, unit_price, color_rgb_code, position_x, position_y
) VALUES
      (2009, 203, 102, 'SHEET', '딸기 시트', 2500, '#FFB6C1', NULL, NULL),
      (2010, 203, 104, 'CREAM', '생크림', 0, '#FFFFFF', NULL, NULL),
      (2011, 203, 107, 'TOPPING', '딸기', 3000, NULL, NULL, NULL),
      (2012, 203, 108, 'TOPPING', '블루베리', 3500, NULL, NULL, NULL),
      (2013, 203, 111, 'ICING', '핑크 아이싱', 2000, '#FFB6C1', NULL, NULL)
    AS new
ON DUPLICATE KEY UPDATE unit_price = new.unit_price;

-- 주문 204: 엘레강스 화이트 (초코 시트 + 생크림 + 마카롱 + 금박)
INSERT INTO order_items (
    id, order_id, custom_option_id, option_category, option_name, unit_price, color_rgb_code, position_x, position_y
) VALUES
      (2014, 204, 101, 'SHEET', '초코 시트', 2000, '#8B4513', NULL, NULL),
      (2015, 204, 104, 'CREAM', '생크림', 0, '#FFFFFF', NULL, NULL),
      (2016, 204, 109, 'TOPPING', '마카롱', 5000, NULL, NULL, NULL),
      (2017, 204, 110, 'TOPPING', '금박', 8000, NULL, NULL, NULL)
    AS new
ON DUPLICATE KEY UPDATE unit_price = new.unit_price;

-- 주문 205: 그린티 블리스 (녹차 시트 + 치즈 크림 + 블루베리)
INSERT INTO order_items (
    id, order_id, custom_option_id, option_category, option_name, unit_price, color_rgb_code, position_x, position_y
) VALUES
      (2018, 205, 103, 'SHEET', '녹차 시트', 3000, '#90EE90', NULL, NULL),
      (2019, 205, 106, 'CREAM', '치즈 크림', 2000, '#FFF8DC', NULL, NULL),
      (2020, 205, 108, 'TOPPING', '블루베리', 3500, NULL, NULL, NULL)
    AS new
ON DUPLICATE KEY UPDATE unit_price = new.unit_price;


-- =====================================================
-- 6. 리뷰 키워드 마스터 데이터
-- =====================================================
INSERT INTO review_keyword (
    id, category, code, label
) VALUES
      (1, 'DESIGN_SATISFACTION', 'DESIGN_BEAUTIFUL', '디자인 예뻐요'),
      (2, 'DESIGN_SATISFACTION', 'DESIGN_UNIQUE', '디자인 독특해요'),
      (3, 'SAME_AS_RESULT', 'SAME_AS_ORDER', '주문과 동일해요'),
      (4, 'TASTE', 'TASTE_DELICIOUS', '맛있어요'),
      (5, 'TASTE', 'TASTE_FRESH', '신선해요'),
      (6, 'COMMUNICATION', 'KIND_RESPONSE', '친절해요'),
      (7, 'PICKUP', 'PICKUP_EASY', '픽업 편해요')
    AS new
ON DUPLICATE KEY UPDATE label = new.label;


-- =====================================================
-- 7. 리뷰 데이터 (5개 - agreement=true, designGallery 있음)
-- =====================================================
INSERT INTO review (
    id,
    order_id,
    user_id,
    shop_id,
    design_id,
    rating,
    content,
    agreement,

    created_at,
    updated_at,
    deleted_at
) VALUES
      -- 리뷰 1: 로맨틱 플라워 (좋아요 15개 예정 - BEST 1위)
      (301, 201, 201, 1, 101, 5,
       '정말 너무 예뻐요! 핑크 장미 장식이 생각보다 훨씬 화려하고 금박도 고급스러워요. 맛도 달지 않고 부드러워서 최고였습니다. 생일 케이크로 강추합니다!',
       true,
       '2026-01-21 20:00:00', '2026-01-21 20:00:00', NULL),

      -- 리뷰 2: 초코 드림 (좋아요 12개 예정 - BEST 2위)
      (302, 202, 202, 1, 102, 5,
       '초코 마니아라면 꼭 드셔보세요! 초코 시트와 초코 크림의 조화가 환상적이에요. 너무 달지 않고 진한 초콜릿 맛이 일품입니다. 마카롱도 맛있어요!',
       true,
       '2026-01-22 19:00:00', '2026-01-22 19:00:00', NULL),

      -- 리뷰 3: 베리 파라다이스 (좋아요 10개 예정 - BEST 3위)
      (303, 203, 203, 1, 103, 4,
       '딸기와 블루베리가 정말 신선했어요! 상큼한 맛이 좋았고 비주얼도 화려해서 사진 찍기 좋았습니다. 다만 가격이 조금 비싼 것 같아요.',
       true,
       '2026-01-23 18:00:00', '2026-01-23 18:00:00', NULL),

      -- 리뷰 4: 엘레강스 화이트 (좋아요 8개 예정 - BEST 4위)
      (304, 204, 204, 1, 104, 5,
       '결혼 기념일 케이크로 주문했는데 정말 우아하고 고급스러웠어요. 마카롱 색상도 제가 원하는 파스텔톤으로 잘 나왔고, 금박 장식이 포인트예요!',
       true,
       '2026-01-24 17:00:00', '2026-01-24 17:00:00', NULL),

      -- 리뷰 5: 그린티 블리스 (좋아요 6개 예정 - BEST 5위)
      (305, 205, 205, 1, 105, 4,
       '녹차 좋아하시는 분들께 추천합니다. 녹차 향이 진하고 치즈 크림과 잘 어울려요. 달달한 케이크 싫어하시는 분들에게 딱이에요!',
       true,
       '2026-01-25 16:00:00', '2026-01-25 16:00:00', NULL)
    AS new
ON DUPLICATE KEY UPDATE
                     rating = new.rating,
                     content = new.content;


-- =====================================================
-- 8. 리뷰 선택 키워드 (각 리뷰당 2-3개씩)
-- =====================================================
INSERT INTO review_selected_keyword (
    id, review_id, keyword_id
) VALUES
      -- 리뷰 301 키워드
      (3001, 301, 1), -- 디자인 예뻐요
      (3002, 301, 4), -- 맛있어요
      (3003, 301, 6), -- 친절해요

      -- 리뷰 302 키워드
      (3004, 302, 2), -- 디자인 독특해요
      (3005, 302, 4), -- 맛있어요
      (3006, 302, 3), -- 주문과 동일해요

      -- 리뷰 303 키워드
      (3007, 303, 1), -- 디자인 예뻐요
      (3008, 303, 5), -- 신선해요

      -- 리뷰 304 키워드
      (3009, 304, 1), -- 디자인 예뻐요
      (3010, 304, 4), -- 맛있어요
      (3011, 304, 7), -- 픽업 편해요

      -- 리뷰 305 키워드
      (3012, 305, 2), -- 디자인 독특해요
      (3013, 305, 4)  -- 맛있어요
    AS new
ON DUPLICATE KEY UPDATE keyword_id = new.keyword_id;


-- =====================================================
-- 9. 리뷰 좋아요 (ReviewLike) - 도움이 되었어요
-- =====================================================
-- 더미 유저들 (좋아요 누를 유저들)
INSERT INTO users (
    user_id, created_at, updated_at, email, kakao_id, name, nickname, phone, status
) VALUES
      (301, NOW(6), NOW(6), 'liker1@test.com', 3000000301, '좋아요1', '좋아요유저1', '010-3001-0001', 'ACTIVE'),
      (302, NOW(6), NOW(6), 'liker2@test.com', 3000000302, '좋아요2', '좋아요유저2', '010-3002-0002', 'ACTIVE'),
      (303, NOW(6), NOW(6), 'liker3@test.com', 3000000303, '좋아요3', '좋아요유저3', '010-3003-0003', 'ACTIVE'),
      (304, NOW(6), NOW(6), 'liker4@test.com', 3000000304, '좋아요4', '좋아요유저4', '010-3004-0004', 'ACTIVE'),
      (305, NOW(6), NOW(6), 'liker5@test.com', 3000000305, '좋아요5', '좋아요유저5', '010-3005-0005', 'ACTIVE'),
      (306, NOW(6), NOW(6), 'liker6@test.com', 3000000306, '좋아요6', '좋아요유저6', '010-3006-0006', 'ACTIVE'),
      (307, NOW(6), NOW(6), 'liker7@test.com', 3000000307, '좋아요7', '좋아요유저7', '010-3007-0007', 'ACTIVE'),
      (308, NOW(6), NOW(6), 'liker8@test.com', 3000000308, '좋아요8', '좋아요유저8', '010-3008-0008', 'ACTIVE'),
      (309, NOW(6), NOW(6), 'liker9@test.com', 3000000309, '좋아요9', '좋아요유저9', '010-3009-0009', 'ACTIVE'),
      (310, NOW(6), NOW(6), 'liker10@test.com', 3000000310, '좋아요10', '좋아요유저10', '010-3010-0010', 'ACTIVE'),
      (311, NOW(6), NOW(6), 'liker11@test.com', 3000000311, '좋아요11', '좋아요유저11', '010-3011-0011', 'ACTIVE'),
      (312, NOW(6), NOW(6), 'liker12@test.com', 3000000312, '좋아요12', '좋아요유저12', '010-3012-0012', 'ACTIVE'),
      (313, NOW(6), NOW(6), 'liker13@test.com', 3000000313, '좋아요13', '좋아요유저13', '010-3013-0013', 'ACTIVE'),
      (314, NOW(6), NOW(6), 'liker14@test.com', 3000000314, '좋아요14', '좋아요유저14', '010-3014-0014', 'ACTIVE'),
      (315, NOW(6), NOW(6), 'liker15@test.com', 3000000315, '좋아요15', '좋아요유저15', '010-3015-0015', 'ACTIVE')
    AS new
ON DUPLICATE KEY UPDATE status = new.status;

-- 리뷰 301에 15개 좋아요 (BEST 1위)
INSERT INTO review_like (review_id, user_id, created_at, updated_at)
SELECT 301, user_id, NOW(6), NOW(6)
FROM users
WHERE user_id BETWEEN 301 AND 315
    ON DUPLICATE KEY UPDATE updated_at = NOW(6);

-- 리뷰 302에 12개 좋아요 (BEST 2위)
INSERT INTO review_like (review_id, user_id, created_at, updated_at)
SELECT 302, user_id, NOW(6), NOW(6)
FROM users
WHERE user_id BETWEEN 301 AND 312
    ON DUPLICATE KEY UPDATE updated_at = NOW(6);

-- 리뷰 303에 10개 좋아요 (BEST 3위)
INSERT INTO review_like (review_id, user_id, created_at, updated_at)
SELECT 303, user_id, NOW(6), NOW(6)
FROM users
WHERE user_id BETWEEN 301 AND 310
    ON DUPLICATE KEY UPDATE updated_at = NOW(6);

-- 리뷰 304에 8개 좋아요 (BEST 4위)
INSERT INTO review_like (review_id, user_id, created_at, updated_at)
SELECT 304, user_id, NOW(6), NOW(6)
FROM users
WHERE user_id BETWEEN 301 AND 308
    ON DUPLICATE KEY UPDATE updated_at = NOW(6);

-- 리뷰 305에 6개 좋아요 (BEST 5위)
INSERT INTO review_like (review_id, user_id, created_at, updated_at)
SELECT 305, user_id, NOW(6), NOW(6)
FROM users
WHERE user_id BETWEEN 301 AND 306
    ON DUPLICATE KEY UPDATE updated_at = NOW(6);
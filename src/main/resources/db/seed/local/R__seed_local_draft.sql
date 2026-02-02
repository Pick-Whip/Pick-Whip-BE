-- ======================================================
-- Swagger Test Seed Data (Repeatable)
-- Goal   : Test 'Create Order' API Flow
-- Pattern: Self-Contained & Idempotent
-- ======================================================

-- 1. 유저 생성 (User ID: 100)
INSERT INTO users (
    user_id,
    created_at,
    updated_at,
    email,
    kakao_id,
    name,
    nickname,
    birthdate,
    phone,
    profile_image_url,
    status
) VALUES (
             100,
             NOW(6),
             NOW(6),
             'swagger.test@picknwhip.com',
             9999999999,
             '테스트유저',
             '스웨거테스터',
             NULL,
             '010-0000-0000',
             NULL,
             'ACTIVE'
         ) AS new
ON DUPLICATE KEY UPDATE
                     email = new.email,
                     nickname = new.nickname,
                     status = new.status;


-- 2. 가게 생성 (Shop ID: 2)
INSERT INTO shops (
    shop_id,
    average_rating,
    description,
    location,
    phone,
    shop_name,
    status,
    verification_status,
    owner_id,
    address
) VALUES (
             2,
             0.0,
             -- NOW(6) 값 제거
             '테스트 케이크샵입니다.',
             ST_GeomFromText('POINT(37.0 127.0)', 4326),
             '02-1234-5678',
             '테스트 케이크샵',
             'ACTIVE',
             'VERIFIED',
            2, -- 위에서 만든 100번 유저
             '서울 강남구'
         ) AS new
ON DUPLICATE KEY UPDATE
                     shop_name = new.shop_name,
                     location = new.location;


-- 3. 케이크 사이즈 생성
INSERT INTO shop_cake_sizes (
    size_id, shop_id, size_name, diameter, price
) VALUES (
             1, 2, '1호', '15cm', 30000
         ) AS new
ON DUPLICATE KEY UPDATE price = new.price;


-- 4. 디자인 갤러리 생성 (Design ID: 2)
INSERT INTO design_gallery (
    id, shop_id, shop_cake_size_id, design_name, base_price, description
    -- created_at, updated_at이 없을 수 있으므로 제거 (있다면 DB Default 사용)
) VALUES (
             1, 1, 1, '기본 생크림 케이크', 30000, '테스트용 기본 디자인'
         ) AS new
ON DUPLICATE KEY UPDATE design_name = new.design_name;


-- 5. 커스텀 옵션 생성 (Option ID: 1)
INSERT INTO custom_options (
    id, shop_id, category, option_name, additional_price
) VALUES (
             1, 1, 'SHEET', '초코 시트', 2000
         ) AS new
ON DUPLICATE KEY UPDATE additional_price = new.additional_price;


-- 6. 임시저장 주문서 (Draft) 생성 (Draft ID: 100)
INSERT INTO orders_drafts (
    id,
    user_id,
    shop_id,
    shop_cake_size_id,
    design_id,
    pickup_datetime,
    lettering_text,
    lettering_line_count,
    lettering_alignment,
    additional_request,
    reference_image_url,
    created_at,
    updated_at
) VALUES (
             100,
             100,
             2,
             1,
             1, -- Design ID 1 연결
             DATE_ADD(CURRENT_DATE, INTERVAL 1 DAY) + INTERVAL 14 HOUR,
             'Happy Swagger Day',
             'ONE_LINE',
             'CENTER',
             '디자인 요청: 배경을 연하게 해주세요',
             'http://test.image/cake.jpg',
             NOW(6),
             NOW(6)
         ) AS new
ON DUPLICATE KEY UPDATE
                     pickup_datetime = new.pickup_datetime,
                     design_id = new.design_id;


-- 7. Draft Items (옵션 연결)
INSERT INTO order_draft_items (
    id, draft_id, custom_option_id, position_x, position_y
) VALUES (
             100, 100, 1, NULL, NULL
         ) AS new
ON DUPLICATE KEY UPDATE custom_option_id = new.custom_option_id;

-- 8. 가게 운영 시간 (월~일, 10:00 ~ 22:00)
-- 이게 없으면 픽업 캘린더가 텅 비어서 나옵니다.
INSERT INTO shop_business_hours (
    shop_id, schedule_type, day_of_week, open_time, close_time, is_closed
) VALUES
      (2, 'WEEKLY', 1, '10:00:00', '22:00:00', false), -- 월
      (2, 'WEEKLY', 2, '10:00:00', '22:00:00', false), -- 화
      (2, 'WEEKLY', 3, '10:00:00', '22:00:00', false), -- 수
      (2, 'WEEKLY', 4, '10:00:00', '22:00:00', false), -- 목
      (2, 'WEEKLY', 5, '10:00:00', '22:00:00', false), -- 금
      (2, 'WEEKLY', 6, '10:00:00', '22:00:00', false), -- 토
      (2, 'WEEKLY', 7, '10:00:00', '22:00:00', false)  -- 일
    AS new
ON DUPLICATE KEY UPDATE
                     open_time = new.open_time,
                     close_time = new.close_time;
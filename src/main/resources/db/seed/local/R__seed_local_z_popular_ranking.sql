-- ======================================================
-- Popular Design Ranking Seed Data (Repeatable)
-- Goal: GET /api/home/popular-designs/top4 API가
--       꽉 찬 데이터를 반환하도록 함 (이미지, 레터링 포함)
-- ======================================================

-- 1. 기존 랭킹 데이터 초기화
DELETE FROM popular_design_rankings;

-- 2. 맛/모양 옵션 확보
INSERT INTO custom_options (id, shop_id, category, option_name, additional_price, color_rgb_code)
VALUES
    (201, 1, 'SHEET', '바닐라 시트', 0, NULL),
    (202, 1, 'SHEET', '초코 시트', 1000, NULL),
    (203, 1, 'CREAM', '생크림', 0, NULL),
    (204, 1, 'CREAM', '오레오 크림', 1000, NULL),
    (205, 1, 'SHAPE', '하트', 2000, NULL),
    (206, 1, 'SHAPE', '사각', 2000, NULL)
    ON DUPLICATE KEY UPDATE option_name = VALUES(option_name);

-- 3. [핵심] 디자인 갤러리 데이터 보강 (NULL 값 채우기)
-- 랭킹 1위: 이미지와 레터링 추가
UPDATE design_gallery
SET image_url = 'https://cdn.picknwhip.com/designs/simple_flower.jpg', -- 가짜 이미지 URL
    lettering_text = 'Love You',
    lettering_line_count = 'ONE_LINE',
    lettering_alignment = 'CENTER'
WHERE id = 1;

-- 랭킹 3위: 레터링 추가
UPDATE design_gallery
SET lettering_text = 'Happy Birthday',
    lettering_line_count = 'TWO_LINE',
    lettering_alignment = 'CURVE_UP'
WHERE id = 3;

-- 랭킹 4위: 레터링 추가
UPDATE design_gallery
SET image_url = 'https://cdn.picknwhip.com/designs/vintage_flower.jpg',
    lettering_text = 'Congratulations',
    lettering_line_count = 'THREE_LINE',
    lettering_alignment = 'CENTER'
WHERE id = 4;


-- 4. 디자인별 상세 스펙 연결 (맛, 모양 등)

-- [Design 1] 1위: 심플 플라워 (바닐라+생크림, 원형)
DELETE FROM design_options WHERE design_id = 1;
INSERT INTO design_options (design_id, custom_option_id, position_x, position_y)
VALUES (1, 201, 0, 0), (1, 203, 0, 0);

DELETE FROM design_gallery_keywords WHERE design_gallery_id = 1;
INSERT INTO design_gallery_keywords (design_gallery_id, keyword) VALUES (1, 'ROUND');

-- [Design 2] 2위: 미니멀 레터링 (초코+오레오, 하트)
DELETE FROM design_options WHERE design_id = 2;
INSERT INTO design_options (design_id, custom_option_id, position_x, position_y)
VALUES (2, 202, 0, 0), (2, 204, 0, 0), (2, 205, 0, 0);

DELETE FROM design_gallery_keywords WHERE design_gallery_id = 2;
INSERT INTO design_gallery_keywords (design_gallery_id, keyword) VALUES (2, 'HEART'); -- 하트 모양 강제 지정

-- [Design 3] 3위: 아이돌 포토 (바닐라+오레오, 사각)
DELETE FROM design_options WHERE design_id = 3;
INSERT INTO design_options (design_id, custom_option_id, position_x, position_y)
VALUES (3, 201, 0, 0), (3, 204, 0, 0), (3, 206, 0, 0);

DELETE FROM design_gallery_keywords WHERE design_gallery_id = 3;
INSERT INTO design_gallery_keywords (design_gallery_id, keyword) VALUES (3, 'SQUARE'); -- 사각 모양 강제 지정

-- [Design 4] 4위: 빈티지 레터링 (초코+생크림, 원형)
DELETE FROM design_options WHERE design_id = 4;
INSERT INTO design_options (design_id, custom_option_id, position_x, position_y)
VALUES (4, 202, 0, 0), (4, 203, 0, 0);

DELETE FROM design_gallery_keywords WHERE design_gallery_id = 4;
INSERT INTO design_gallery_keywords (design_gallery_id, keyword) VALUES (4, 'ROUND');


-- 5. 랭킹 데이터 직접 삽입
INSERT INTO popular_design_rankings (ranking, design_id, shop_id, order_count, calculated_at)
VALUES
    (1, 1, 1, 150, NOW()),
    (2, 2, 1, 120, NOW()),
    (3, 3, 1, 85,  NOW()),
    (4, 4, 1, 40,  NOW());


-- 6. 데이터 정합성을 위한 더미 주문 데이터 (결제 완료 상태)
INSERT INTO orders (
    id, user_id, shop_id, design_id, shop_cake_size_id,
    status, payment_status, payment_method,
    pickup_datetime,
    order_code, customer_name, customer_phone, total_price, created_at, updated_at
) VALUES
      (501, 100, 1, 1, 1, 'COMPLETED', 'PAID', 'CARD', NOW(), 'TEST_RANK_1', '테스터', '010-0000-0000', 35000, NOW(), NOW()),
      (502, 100, 1, 1, 1, 'COMPLETED', 'PAID', 'CARD', NOW(), 'TEST_RANK_2', '테스터', '010-0000-0000', 35000, NOW(), NOW()),
      (503, 100, 1, 1, 1, 'COMPLETED', 'PAID', 'CARD', NOW(), 'TEST_RANK_3', '테스터', '010-0000-0000', 35000, NOW(), NOW()),
      (504, 100, 1, 1, 1, 'COMPLETED', 'PAID', 'CARD', NOW(), 'TEST_RANK_4', '테스터', '010-0000-0000', 35000, NOW(), NOW()),
      (505, 100, 1, 1, 1, 'COMPLETED', 'PAID', 'CARD', NOW(), 'TEST_RANK_5', '테스터', '010-0000-0000', 35000, NOW(), NOW())
    ON DUPLICATE KEY UPDATE
                         status = VALUES(status),
                         payment_status = VALUES(payment_status),
                         payment_method = VALUES(payment_method);
-- ======================================================
-- Popular Design Ranking Seed Data (Repeatable)
-- Goal: GET /api/home/popular-designs/top4 API가
--       꽉 찬 데이터를 반환하도록 함 (이미지, 레터링 포함)
-- shop_id=1, design_id=1~4 기반
-- ======================================================

-- 1. 기존 랭킹 데이터 초기화
DELETE FROM popular_design_rankings;


-- 2. 디자인 1~4 정보 보강 (R__seed_local_design.sql 에서 이미 생성됨)
-- 랭킹용 lettering/image_url 업데이트
UPDATE design_gallery
SET image_url               = 'https://cdn.picknwhip.com/designs/simple_flower.jpg',
    lettering_text          = 'Love You',
    lettering_line_count    = 'ONE_LINE',
    lettering_alignment     = 'CENTER'
WHERE id = 1;

UPDATE design_gallery
SET lettering_text          = 'Happy Birthday',
    lettering_line_count    = 'TWO_LINE',
    lettering_alignment     = 'CURVE_UP'
WHERE id = 3;

UPDATE design_gallery
SET image_url               = 'https://cdn.picknwhip.com/designs/vintage_flower.jpg',
    lettering_text          = 'Congratulations',
    lettering_line_count    = 'THREE_LINE',
    lettering_alignment     = 'CENTER'
WHERE id = 4;


-- 3. 랭킹 데이터 직접 삽입 (shop 1, design 1~4)
INSERT INTO popular_design_rankings (ranking, design_id, shop_id, order_count, calculated_at)
VALUES
    (1, 1, 1, 150, NOW()),
    (2, 2, 1, 120, NOW()),
    (3, 3, 1, 85,  NOW()),
    (4, 4, 1, 40,  NOW());


-- 4. 랭킹 연동용 더미 주문 (user 1 기준, order id 32~36)
INSERT INTO orders (
    id, user_id, shop_id, design_id, shop_cake_size_id,
    status, payment_status, payment_method,
    pickup_datetime,
    order_code, customer_name, customer_phone, total_price, created_at, updated_at
) VALUES
      (32, 1, 1, 1, 2, 'COMPLETED', 'PAID', 'CARD', NOW(), 'DESIGN_RANK_1', '테스터', '010-1111-1111', 35000, NOW(), NOW()),
      (33, 1, 1, 1, 2, 'COMPLETED', 'PAID', 'CARD', NOW(), 'DESIGN_RANK_2', '테스터', '010-1111-1111', 35000, NOW(), NOW()),
      (34, 1, 1, 2, 2, 'COMPLETED', 'PAID', 'CARD', NOW(), 'DESIGN_RANK_3', '테스터', '010-1111-1111', 35000, NOW(), NOW()),
      (35, 1, 1, 3, 2, 'COMPLETED', 'PAID', 'CARD', NOW(), 'DESIGN_RANK_4', '테스터', '010-1111-1111', 42000, NOW(), NOW()),
      (36, 1, 1, 4, 2, 'COMPLETED', 'PAID', 'CARD', NOW(), 'DESIGN_RANK_5', '테스터', '010-1111-1111', 45000, NOW(), NOW())
    ON DUPLICATE KEY UPDATE
                         status         = VALUES(status),
                         payment_status = VALUES(payment_status),
                         payment_method = VALUES(payment_method);
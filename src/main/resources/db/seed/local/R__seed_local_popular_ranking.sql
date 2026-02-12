-- ======================================================
-- Popular Cake Ranking Test Seed Data (Repeatable)
-- Goal   : Test 'Get Popular Cakes Top 5' API
-- Context: 최근 14일 내 주문량 순위 (판매량 집계 테스트)
-- Shop: 3 (랭킹 테스트용), Design: 13~18
-- Orders: 15~29
-- ======================================================

-- 1. 랭킹 테스트용 주문 데이터 (Source Data for Scheduler)
-- [1위 Design 13]: 5건 (order id 15~19)
INSERT INTO orders (id, user_id, shop_id, shop_cake_size_id, design_id, status, payment_status, payment_method, total_price, pickup_datetime, created_at, updated_at, order_code, customer_name, customer_phone)
VALUES
    (15, 1, 3, 7, 13, 'COMPLETED', 'PAID', 'CARD', 30000, NOW(), NOW(), NOW(), 'RANK_001', '테스터', '010-1111-1111'),
    (16, 1, 3, 7, 13, 'COMPLETED', 'PAID', 'CARD', 30000, NOW(), NOW(), NOW(), 'RANK_002', '테스터', '010-1111-1111'),
    (17, 1, 3, 7, 13, 'COMPLETED', 'PAID', 'CARD', 30000, NOW(), NOW(), NOW(), 'RANK_003', '테스터', '010-1111-1111'),
    (18, 1, 3, 7, 13, 'COMPLETED', 'PAID', 'CARD', 30000, NOW(), NOW(), NOW(), 'RANK_004', '테스터', '010-1111-1111'),
    (19, 1, 3, 7, 13, 'COMPLETED', 'PAID', 'CARD', 30000, NOW(), NOW(), NOW(), 'RANK_005', '테스터', '010-1111-1111')
    ON DUPLICATE KEY UPDATE status = VALUES(status);

-- [2위 Design 14]: 4건 (order id 20~23)
INSERT INTO orders (id, user_id, shop_id, shop_cake_size_id, design_id, status, payment_status, payment_method, total_price, pickup_datetime, created_at, updated_at, order_code, customer_name, customer_phone)
VALUES
    (20, 1, 3, 7, 14, 'COMPLETED', 'PAID', 'CARD', 32000, NOW(), NOW(), NOW(), 'RANK_006', '테스터', '010-1111-1111'),
    (21, 1, 3, 7, 14, 'COMPLETED', 'PAID', 'CARD', 32000, NOW(), NOW(), NOW(), 'RANK_007', '테스터', '010-1111-1111'),
    (22, 1, 3, 7, 14, 'COMPLETED', 'PAID', 'CARD', 32000, NOW(), NOW(), NOW(), 'RANK_008', '테스터', '010-1111-1111'),
    (23, 1, 3, 7, 14, 'COMPLETED', 'PAID', 'CARD', 32000, NOW(), NOW(), NOW(), 'RANK_009', '테스터', '010-1111-1111')
    ON DUPLICATE KEY UPDATE status = VALUES(status);

-- [3위 Design 15]: 3건 (order id 24~26)
INSERT INTO orders (id, user_id, shop_id, shop_cake_size_id, design_id, status, payment_status, payment_method, total_price, pickup_datetime, created_at, updated_at, order_code, customer_name, customer_phone)
VALUES
    (24, 1, 3, 7, 15, 'COMPLETED', 'PAID', 'CARD', 28000, NOW(), NOW(), NOW(), 'RANK_010', '테스터', '010-1111-1111'),
    (25, 1, 3, 7, 15, 'COMPLETED', 'PAID', 'CARD', 28000, NOW(), NOW(), NOW(), 'RANK_011', '테스터', '010-1111-1111'),
    (26, 1, 3, 7, 15, 'COMPLETED', 'PAID', 'CARD', 28000, NOW(), NOW(), NOW(), 'RANK_012', '테스터', '010-1111-1111')
    ON DUPLICATE KEY UPDATE status = VALUES(status);

-- [4위 Design 16]: 2건 (order id 27~28)
INSERT INTO orders (id, user_id, shop_id, shop_cake_size_id, design_id, status, payment_status, payment_method, total_price, pickup_datetime, created_at, updated_at, order_code, customer_name, customer_phone)
VALUES
    (27, 1, 3, 7, 16, 'COMPLETED', 'PAID', 'CARD', 35000, NOW(), NOW(), NOW(), 'RANK_013', '테스터', '010-1111-1111'),
    (28, 1, 3, 7, 16, 'COMPLETED', 'PAID', 'CARD', 35000, NOW(), NOW(), NOW(), 'RANK_014', '테스터', '010-1111-1111')
    ON DUPLICATE KEY UPDATE status = VALUES(status);

-- [5위 Design 17]: 1건 (order id 29)
INSERT INTO orders (id, user_id, shop_id, shop_cake_size_id, design_id, status, payment_status, payment_method, total_price, pickup_datetime, created_at, updated_at, order_code, customer_name, customer_phone)
VALUES
    (29, 1, 3, 7, 17, 'COMPLETED', 'PAID', 'CARD', 31000, NOW(), NOW(), NOW(), 'RANK_015', '테스터', '010-1111-1111')
    ON DUPLICATE KEY UPDATE status = VALUES(status);

-- [순위 밖 Design 18]: 2건이지만 날짜가 한 달 전 (order id 30~31)
INSERT INTO orders (id, user_id, shop_id, shop_cake_size_id, design_id, status, payment_status, payment_method, total_price, pickup_datetime, created_at, updated_at, order_code, customer_name, customer_phone)
VALUES
    (30, 1, 3, 7, 18, 'COMPLETED', 'PAID', 'CARD', 40000, DATE_SUB(NOW(), INTERVAL 1 MONTH), DATE_SUB(NOW(), INTERVAL 1 MONTH), DATE_SUB(NOW(), INTERVAL 1 MONTH), 'OLD_001', '테스터', '010-1111-1111'),
    (31, 1, 3, 7, 18, 'COMPLETED', 'PAID', 'CARD', 40000, DATE_SUB(NOW(), INTERVAL 1 MONTH), DATE_SUB(NOW(), INTERVAL 1 MONTH), DATE_SUB(NOW(), INTERVAL 1 MONTH), 'OLD_002', '테스터', '010-1111-1111')
    ON DUPLICATE KEY UPDATE status = VALUES(status);


-- 2. 인기 랭킹 테이블 직접 주입 (Result Data for API)
DELETE FROM popular_cake_rankings;

INSERT INTO popular_cake_rankings (
    ranking, design_id, shop_id, order_count, window_start, window_end, calculated_at
) VALUES
      (1, 13, 3, 5, DATE_SUB(NOW(), INTERVAL 14 DAY), NOW(), NOW()),
      (2, 14, 3, 4, DATE_SUB(NOW(), INTERVAL 14 DAY), NOW(), NOW()),
      (3, 15, 3, 3, DATE_SUB(NOW(), INTERVAL 14 DAY), NOW(), NOW()),
      (4, 16, 3, 2, DATE_SUB(NOW(), INTERVAL 14 DAY), NOW(), NOW()),
      (5, 17, 3, 1, DATE_SUB(NOW(), INTERVAL 14 DAY), NOW(), NOW());
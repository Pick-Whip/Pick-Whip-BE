-- ======================================================
-- Popular Cake Ranking Test Seed Data (Repeatable)
-- Goal   : Test 'Get Popular Cakes Top 5' API
-- Context: 최근 14일 내 주문량 순위 (판매량 집계 테스트)
-- IDs    : Shop(50), Design(501~506), Order(5000~)
-- ======================================================

-- 1. 랭킹 테스트용 매장 생성 (ID: 50)
-- 1. 랭킹 테스트용 매장 생성 (ID: 50)
-- [Pick&Whip 수정] average_rating(4.8)과 min_price(15000) 데이터를 추가했습니다.
INSERT INTO shops (
    shop_id, owner_id, shop_name, phone, address, location, status, verification_status,
    description, created_at,
    average_rating, min_price
) VALUES (
             50, 1, '랭킹 테스트용 빵집', '010-5555-5555', '서울시 성수동', ST_GeomFromText('POINT(37.5 127.0)', 4326),
             'ACTIVE', 'VERIFIED', '인기 랭킹 테스트를 위한 매장입니다.', NOW(6),
             4.8, 15000
         ) ON DUPLICATE KEY UPDATE
    shop_name = VALUES(shop_name),
    average_rating = VALUES(average_rating),
    min_price = VALUES(min_price);
-- 2. 케이크 사이즈 생성 (ID: 50)
INSERT INTO shop_cake_sizes (size_id, shop_id, size_name, diameter, price)
VALUES (50, 50, '1호', '15cm', 30000)
    ON DUPLICATE KEY UPDATE price = VALUES(price);

-- 3. 디자인 생성 (501~506)
-- 시나리오: 1위(501), 2위(502), 3위(503), 4위(504), 5위(505), 순위밖(506-오래된주문)
INSERT INTO design_gallery (
    id, shop_id, shop_cake_size_id, design_name, base_price, image_url, description
) VALUES
      (501, 50, 50, '초코케이크', 30000, 'https://cdn.picknwhip.com/test/rank1.jpg', '주문수 5개 - 1등'),
      (502, 50, 50, '딸기케이크', 32000, 'https://cdn.picknwhip.com/test/rank2.jpg', '주문수 4개 - 2등'),
      (503, 50, 50, '바닐라케이크', 28000, 'https://cdn.picknwhip.com/test/rank3.jpg', '주문수 3개 - 3등'),
      (504, 50, 50, '녹차케이크', 35000, 'https://cdn.picknwhip.com/test/rank4.jpg', '주문수 2개 - 4등'),
      (505, 50, 50, '치즈케이크', 31000, 'https://cdn.picknwhip.com/test/rank5.jpg', '주문수 1개 - 5등'),
      (506, 50, 50, '순위밖_오래된케이크', 40000, 'https://cdn.picknwhip.com/test/old.jpg', '주문은 많지만 1달 전이라 집계 제외')
    ON DUPLICATE KEY UPDATE design_name = VALUES(design_name);

-- 4. 주문 데이터 생성 (Source Data for Scheduler)
-- [Pick&Whip 수정] payment_method 컬럼 추가 ('CARD')

-- [1위 Design 501]: 5건
INSERT INTO orders (id, user_id, shop_id, shop_cake_size_id, design_id, status, payment_status, payment_method, total_price, pickup_datetime, created_at, updated_at, order_code, customer_name, customer_phone) VALUES
                                                                                                                                                                                                                     (5001, 1, 50, 50, 501, 'COMPLETED', 'PAID', 'CARD', 30000, NOW(), NOW(), NOW(), 'RANK_001', '테스터', '010-0000-0000'),
                                                                                                                                                                                                                     (5002, 1, 50, 50, 501, 'COMPLETED', 'PAID', 'CARD', 30000, NOW(), NOW(), NOW(), 'RANK_002', '테스터', '010-0000-0000'),
                                                                                                                                                                                                                     (5003, 1, 50, 50, 501, 'COMPLETED', 'PAID', 'CARD', 30000, NOW(), NOW(), NOW(), 'RANK_003', '테스터', '010-0000-0000'),
                                                                                                                                                                                                                     (5004, 1, 50, 50, 501, 'COMPLETED', 'PAID', 'CARD', 30000, NOW(), NOW(), NOW(), 'RANK_004', '테스터', '010-0000-0000'),
                                                                                                                                                                                                                     (5005, 1, 50, 50, 501, 'COMPLETED', 'PAID', 'CARD', 30000, NOW(), NOW(), NOW(), 'RANK_005', '테스터', '010-0000-0000')
    ON DUPLICATE KEY UPDATE status = VALUES(status);

-- [2위 Design 502]: 4건
INSERT INTO orders (id, user_id, shop_id, shop_cake_size_id, design_id, status, payment_status, payment_method, total_price, pickup_datetime, created_at, updated_at, order_code, customer_name, customer_phone) VALUES
                                                                                                                                                                                                                     (5006, 1, 50, 50, 502, 'COMPLETED', 'PAID', 'CARD', 32000, NOW(), NOW(), NOW(), 'RANK_006', '테스터', '010-0000-0000'),
                                                                                                                                                                                                                     (5007, 1, 50, 50, 502, 'COMPLETED', 'PAID', 'CARD', 32000, NOW(), NOW(), NOW(), 'RANK_007', '테스터', '010-0000-0000'),
                                                                                                                                                                                                                     (5008, 1, 50, 50, 502, 'COMPLETED', 'PAID', 'CARD', 32000, NOW(), NOW(), NOW(), 'RANK_008', '테스터', '010-0000-0000'),
                                                                                                                                                                                                                     (5009, 1, 50, 50, 502, 'COMPLETED', 'PAID', 'CARD', 32000, NOW(), NOW(), NOW(), 'RANK_009', '테스터', '010-0000-0000')
    ON DUPLICATE KEY UPDATE status = VALUES(status);

-- [3위 Design 503]: 3건
INSERT INTO orders (id, user_id, shop_id, shop_cake_size_id, design_id, status, payment_status, payment_method, total_price, pickup_datetime, created_at, updated_at, order_code, customer_name, customer_phone) VALUES
                                                                                                                                                                                                                     (5010, 1, 50, 50, 503, 'COMPLETED', 'PAID', 'CARD', 28000, NOW(), NOW(), NOW(), 'RANK_010', '테스터', '010-0000-0000'),
                                                                                                                                                                                                                     (5011, 1, 50, 50, 503, 'COMPLETED', 'PAID', 'CARD', 28000, NOW(), NOW(), NOW(), 'RANK_011', '테스터', '010-0000-0000'),
                                                                                                                                                                                                                     (5012, 1, 50, 50, 503, 'COMPLETED', 'PAID', 'CARD', 28000, NOW(), NOW(), NOW(), 'RANK_012', '테스터', '010-0000-0000')
    ON DUPLICATE KEY UPDATE status = VALUES(status);

-- [4위 Design 504]: 2건
INSERT INTO orders (id, user_id, shop_id, shop_cake_size_id, design_id, status, payment_status, payment_method, total_price, pickup_datetime, created_at, updated_at, order_code, customer_name, customer_phone) VALUES
                                                                                                                                                                                                                     (5013, 1, 50, 50, 504, 'COMPLETED', 'PAID', 'CARD', 35000, NOW(), NOW(), NOW(), 'RANK_013', '테스터', '010-0000-0000'),
                                                                                                                                                                                                                     (5014, 1, 50, 50, 504, 'COMPLETED', 'PAID', 'CARD', 35000, NOW(), NOW(), NOW(), 'RANK_014', '테스터', '010-0000-0000')
    ON DUPLICATE KEY UPDATE status = VALUES(status);

-- [5위 Design 505]: 1건
INSERT INTO orders (id, user_id, shop_id, shop_cake_size_id, design_id, status, payment_status, payment_method, total_price, pickup_datetime, created_at, updated_at, order_code, customer_name, customer_phone) VALUES
    (5015, 1, 50, 50, 505, 'COMPLETED', 'PAID', 'CARD', 31000, NOW(), NOW(), NOW(), 'RANK_015', '테스터', '010-0000-0000')
    ON DUPLICATE KEY UPDATE status = VALUES(status);

-- [순위 밖 Design 506]: 10건이지만 날짜가 한 달 전
INSERT INTO orders (id, user_id, shop_id, shop_cake_size_id, design_id, status, payment_status, payment_method, total_price, pickup_datetime, created_at, updated_at, order_code, customer_name, customer_phone) VALUES
                                                                                                                                                                                                                     (5016, 1, 50, 50, 506, 'COMPLETED', 'PAID', 'CARD', 40000, DATE_SUB(NOW(), INTERVAL 1 MONTH), DATE_SUB(NOW(), INTERVAL 1 MONTH), DATE_SUB(NOW(), INTERVAL 1 MONTH), 'OLD_001', '테스터', '010-0000-0000'),
                                                                                                                                                                                                                     (5017, 1, 50, 50, 506, 'COMPLETED', 'PAID', 'CARD', 40000, DATE_SUB(NOW(), INTERVAL 1 MONTH), DATE_SUB(NOW(), INTERVAL 1 MONTH), DATE_SUB(NOW(), INTERVAL 1 MONTH), 'OLD_002', '테스터', '010-0000-0000')
    ON DUPLICATE KEY UPDATE status = VALUES(status);


-- 5. 인기 랭킹 테이블 직접 주입 (Result Data for API)
DELETE FROM popular_cake_rankings;

INSERT INTO popular_cake_rankings (
    ranking, design_id, shop_id, order_count, window_start, window_end, calculated_at
) VALUES
      (1, 501, 50, 5, DATE_SUB(NOW(), INTERVAL 14 DAY), NOW(), NOW()),
      (2, 502, 50, 4, DATE_SUB(NOW(), INTERVAL 14 DAY), NOW(), NOW()),
      (3, 503, 50, 3, DATE_SUB(NOW(), INTERVAL 14 DAY), NOW(), NOW()),
      (4, 504, 50, 2, DATE_SUB(NOW(), INTERVAL 14 DAY), NOW(), NOW()),
      (5, 505, 50, 1, DATE_SUB(NOW(), INTERVAL 14 DAY), NOW(), NOW());
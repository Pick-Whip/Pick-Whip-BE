-- ======================================================
-- 주문 상세조회 / 주문 목록 API 테스트용 시드 데이터 (Repeatable)
-- Goal: GET /api/orders, GET /api/orders/{orderId} 테스트
-- 모든 주문: user_id = 1, shop_id = 1
-- 주문 ID: 2~7 (모든 Status 커버)
-- ======================================================

-- 기존 테스트 데이터 정리 (멱등성 보장)
DELETE FROM order_histories WHERE order_id BETWEEN 2 AND 7;
DELETE FROM order_items     WHERE order_id BETWEEN 2 AND 7;
DELETE FROM orders          WHERE id       BETWEEN 2 AND 7;


-- ★ 필수 옵션 데이터 (FK 에러 방지 - R__seed_local_design.sql 에서 이미 생성되나 보장용)
INSERT INTO custom_options (id, shop_id, category, option_name, additional_price, color_rgb_code)
VALUES
    (1, 1, 'SHEET',   '바닐라 시트', 0,    NULL),
    (2, 1, 'SHEET',   '초코 시트',   1000, NULL),
    (3, 1, 'CREAM',   '생크림',      0,    '#FFFFFF'),
    (4, 1, 'CREAM',   '초코 크림',   1500, '#8B4513'),
    (5, 1, 'TOPPING', '딸기',        3000, NULL),
    (6, 1, 'TOPPING', '마카롱',      5000, NULL)
    AS new
ON DUPLICATE KEY UPDATE
                     additional_price = new.additional_price;


-- ======================================================
-- 테스트 케이스 1: 주문서 확인 중 (CONFIRM_WAIT) - 주문 ID 2
-- ======================================================
INSERT INTO orders (
    id, user_id, shop_id, shop_cake_size_id, design_id, status,
    pickup_datetime, payment_status, payment_method, total_price,
    customer_name, customer_phone, order_code,
    lettering_text, lettering_line_count, lettering_alignment,
    additional_request, order_additional_request, reference_image_url,
    created_at, updated_at
) VALUES (
             2, 1, 1, 2, 1, 'CONFIRM_WAIT',
             DATE_ADD(NOW(), INTERVAL 3 DAY) + INTERVAL 14 HOUR,
             'WAITING', 'TBD', 38500,
             '김픽휩', '010-1111-1111', '260207_002',
             'Happy Birthday!', 'ONE_LINE', 'CENTER',
             '딸기 많이 올려주세요', '포크 3개 부탁드립니다',
             'https://example.com/cake1.jpg',
             NOW(), NOW()
         );

INSERT INTO order_items (order_id, custom_option_id, option_category, option_name, unit_price)
VALUES
    (2, 2, 'SHEET',   '초코 시트', 1000),
    (2, 3, 'CREAM',   '생크림',    0),
    (2, 5, 'TOPPING', '딸기',      3000);

INSERT INTO order_histories (order_id, status, created_at, updated_at)
VALUES (2, 'CONFIRM_WAIT', NOW(), NOW());


-- ======================================================
-- 테스트 케이스 2: 결제 요청 중 (PAYMENT_WAIT) - 주문 ID 3
-- ======================================================
INSERT INTO orders (
    id, user_id, shop_id, shop_cake_size_id, design_id, status,
    pickup_datetime, payment_status, payment_method, total_price,
    customer_name, customer_phone, order_code,
    lettering_text, lettering_line_count, lettering_alignment,
    additional_request, order_additional_request, reference_image_url,
    created_at, updated_at
) VALUES (
             3, 1, 1, 2, 2, 'PAYMENT_WAIT',
             DATE_ADD(NOW(), INTERVAL 4 DAY) + INTERVAL 15 HOUR,
             'WAITING', 'TBD', 38500,
             '김픽휩', '010-1111-1111', '260207_003',
             'Congratulations!', 'TWO_LINE', 'CURVE_UP',
             '알러지 주의해주세요', '배송지 주소 확인 부탁드립니다',
             'https://example.com/cake2.jpg',
             DATE_SUB(NOW(), INTERVAL 2 HOUR), DATE_SUB(NOW(), INTERVAL 2 HOUR)
         );

INSERT INTO order_items (order_id, custom_option_id, option_category, option_name, unit_price)
VALUES
    (3, 2, 'SHEET',   '초코 시트', 1000),
    (3, 3, 'CREAM',   '생크림',    0),
    (3, 5, 'TOPPING', '딸기',      3000);

INSERT INTO order_histories (order_id, status, created_at, updated_at)
VALUES
    (3, 'CONFIRM_WAIT', DATE_SUB(NOW(), INTERVAL 2 HOUR),  DATE_SUB(NOW(), INTERVAL 2 HOUR)),
    (3, 'PAYMENT_WAIT', DATE_SUB(NOW(), INTERVAL 30 MINUTE), DATE_SUB(NOW(), INTERVAL 30 MINUTE));


-- ======================================================
-- 테스트 케이스 3: 제작 불가 (CANCELED_BY_SHOP) - 주문 ID 4
-- ======================================================
INSERT INTO orders (
    id, user_id, shop_id, shop_cake_size_id, design_id, status,
    pickup_datetime, payment_status, payment_method, total_price,
    customer_name, customer_phone, order_code,
    lettering_text, lettering_line_count, lettering_alignment,
    additional_request, order_additional_request, reference_image_url,
    rejection_reason,
    created_at, updated_at
) VALUES (
             4, 1, 1, 2, 3, 'CANCELED_BY_SHOP',
             DATE_ADD(NOW(), INTERVAL 5 DAY) + INTERVAL 16 HOUR,
             'WAITING', 'TBD', 42000,
             '김픽휩', '010-1111-1111', '260207_004',
             'Welcome!', 'ONE_LINE', 'CENTER',
             '특별한 디자인 요청', '초는 빼주세요',
             'https://example.com/cake3.jpg',
             '죄송합니다. 해당 날짜는 예약이 모두 마감되어 제작이 어렵습니다. 다른 날짜로 변경 부탁드립니다.',
             DATE_SUB(NOW(), INTERVAL 3 HOUR), DATE_SUB(NOW(), INTERVAL 3 HOUR)
         );

INSERT INTO order_items (order_id, custom_option_id, option_category, option_name, unit_price)
VALUES
    (4, 2, 'SHEET',   '초코 시트', 1000),
    (4, 3, 'CREAM',   '생크림',    0),
    (4, 5, 'TOPPING', '딸기',      3000);

INSERT INTO order_histories (order_id, status, created_at, updated_at)
VALUES
    (4, 'CONFIRM_WAIT',   DATE_SUB(NOW(), INTERVAL 3 HOUR), DATE_SUB(NOW(), INTERVAL 3 HOUR)),
    (4, 'CANCELED_BY_SHOP', NOW(), NOW());


-- ======================================================
-- 테스트 케이스 4: 제작 중 (MAKING) - 주문 ID 5
-- ======================================================
INSERT INTO orders (
    id, user_id, shop_id, shop_cake_size_id, design_id, status,
    pickup_datetime, payment_status, payment_method, total_price,
    customer_name, customer_phone, order_code,
    lettering_text, lettering_line_count, lettering_alignment,
    additional_request, order_additional_request, reference_image_url,
    created_at, updated_at
) VALUES (
             5, 1, 1, 2, 1, 'MAKING',
             DATE_ADD(NOW(), INTERVAL 1 DAY) + INTERVAL 17 HOUR,
             'PAID', 'CARD', 39000,
             '김픽휩', '010-1111-1111', '260207_005',
             'Thank You!', 'TWO_LINE', 'CURVE_UP_DOWN',
             '예쁘게 만들어주세요', '케이크 박스 2개로 나눠주세요',
             'https://example.com/cake4.jpg',
             DATE_SUB(NOW(), INTERVAL 1 DAY), DATE_SUB(NOW(), INTERVAL 1 DAY)
         );

INSERT INTO order_items (order_id, custom_option_id, option_category, option_name, unit_price)
VALUES
    (5, 2, 'SHEET',   '초코 시트', 1000),
    (5, 3, 'CREAM',   '생크림',    0),
    (5, 5, 'TOPPING', '딸기',      3000);

INSERT INTO order_histories (order_id, status, created_at, updated_at)
VALUES
    (5, 'CONFIRM_WAIT', DATE_SUB(NOW(), INTERVAL 1 DAY),   DATE_SUB(NOW(), INTERVAL 1 DAY)),
    (5, 'PAYMENT_WAIT', DATE_SUB(NOW(), INTERVAL 23 HOUR), DATE_SUB(NOW(), INTERVAL 23 HOUR)),
    (5, 'PROD_CONFIRM', DATE_SUB(NOW(), INTERVAL 20 HOUR), DATE_SUB(NOW(), INTERVAL 20 HOUR)),
    (5, 'MAKING',       DATE_SUB(NOW(), INTERVAL 2 HOUR),  DATE_SUB(NOW(), INTERVAL 2 HOUR));


-- ======================================================
-- 테스트 케이스 5: 픽업 대기 중 (PICKUP_WAIT) - 주문 ID 6
-- ======================================================
INSERT INTO orders (
    id, user_id, shop_id, shop_cake_size_id, design_id, status,
    pickup_datetime, payment_status, payment_method, total_price,
    customer_name, customer_phone, order_code,
    lettering_text, lettering_line_count, lettering_alignment,
    additional_request, order_additional_request, reference_image_url,
    created_at, updated_at
) VALUES (
             6, 1, 1, 2, 4, 'PICKUP_WAIT',
             DATE_ADD(NOW(), INTERVAL 3 HOUR),
             'PAID', 'CARD', 46000,
             '김픽휩', '010-1111-1111', '260207_006',
             'Good Luck!', 'ONE_LINE', 'CENTER',
             '레터링 진하게 해주세요', '픽업 시간 정확히 지키겠습니다',
             'https://example.com/cake5.jpg',
             DATE_SUB(NOW(), INTERVAL 2 DAY), DATE_SUB(NOW(), INTERVAL 2 DAY)
         );

INSERT INTO order_items (order_id, custom_option_id, option_category, option_name, unit_price)
VALUES
    (6, 2, 'SHEET',   '초코 시트', 1000),
    (6, 3, 'CREAM',   '생크림',    0),
    (6, 5, 'TOPPING', '딸기',      3000);

INSERT INTO order_histories (order_id, status, created_at, updated_at)
VALUES
    (6, 'CONFIRM_WAIT', DATE_SUB(NOW(), INTERVAL 2 DAY),   DATE_SUB(NOW(), INTERVAL 2 DAY)),
    (6, 'PAYMENT_WAIT', DATE_SUB(NOW(), INTERVAL 47 HOUR), DATE_SUB(NOW(), INTERVAL 47 HOUR)),
    (6, 'PROD_CONFIRM', DATE_SUB(NOW(), INTERVAL 44 HOUR), DATE_SUB(NOW(), INTERVAL 44 HOUR)),
    (6, 'MAKING',       DATE_SUB(NOW(), INTERVAL 5 HOUR),  DATE_SUB(NOW(), INTERVAL 5 HOUR)),
    (6, 'PICKUP_WAIT',  DATE_SUB(NOW(), INTERVAL 1 HOUR),  DATE_SUB(NOW(), INTERVAL 1 HOUR));


-- ======================================================
-- 테스트 케이스 6: 픽업 완료 (COMPLETED) - 주문 ID 7
-- ======================================================
INSERT INTO orders (
    id, user_id, shop_id, shop_cake_size_id, design_id, status,
    pickup_datetime, payment_status, payment_method, total_price,
    customer_name, customer_phone, order_code,
    lettering_text, lettering_line_count, lettering_alignment,
    additional_request, order_additional_request, reference_image_url,
    created_at, updated_at
) VALUES (
             7, 1, 1, 2, 2, 'COMPLETED',
             DATE_SUB(NOW(), INTERVAL 1 DAY),
             'PAID', 'CARD', 36000,
             '김픽휩', '010-1111-1111', '260207_007',
             'Best Wishes!', 'ONE_LINE', 'CENTER',
             '깔끔하게 포장해주세요', '감사합니다!',
             'https://example.com/cake6.jpg',
             DATE_SUB(NOW(), INTERVAL 3 DAY), DATE_SUB(NOW(), INTERVAL 3 DAY)
         );

INSERT INTO order_items (order_id, custom_option_id, option_category, option_name, unit_price)
VALUES
    (7, 2, 'SHEET',   '초코 시트', 1000),
    (7, 3, 'CREAM',   '생크림',    0),
    (7, 5, 'TOPPING', '딸기',      3000);

INSERT INTO order_histories (order_id, status, created_at, updated_at)
VALUES
    (7, 'CONFIRM_WAIT', DATE_SUB(NOW(), INTERVAL 3 DAY),   DATE_SUB(NOW(), INTERVAL 3 DAY)),
    (7, 'PAYMENT_WAIT', DATE_SUB(NOW(), INTERVAL 71 HOUR), DATE_SUB(NOW(), INTERVAL 71 HOUR)),
    (7, 'PROD_CONFIRM', DATE_SUB(NOW(), INTERVAL 68 HOUR), DATE_SUB(NOW(), INTERVAL 68 HOUR)),
    (7, 'MAKING',       DATE_SUB(NOW(), INTERVAL 30 HOUR), DATE_SUB(NOW(), INTERVAL 30 HOUR)),
    (7, 'PICKUP_WAIT',  DATE_SUB(NOW(), INTERVAL 25 HOUR), DATE_SUB(NOW(), INTERVAL 25 HOUR)),
    (7, 'COMPLETED',    DATE_SUB(NOW(), INTERVAL 1 DAY),   DATE_SUB(NOW(), INTERVAL 1 DAY));


-- ======================================================
-- 테스트 요약
-- ======================================================
-- 주문 ID 1 : COMPLETED  (R__seed_local_order.sql 에서 생성)
-- 주문 ID 2 : CONFIRM_WAIT   (주문서 확인 중)
-- 주문 ID 3 : PAYMENT_WAIT   (결제 요청 중)
-- 주문 ID 4 : CANCELED_BY_SHOP (제작 불가)
-- 주문 ID 5 : MAKING         (제작 중)
-- 주문 ID 6 : PICKUP_WAIT    (픽업 대기)
-- 주문 ID 7 : COMPLETED      (픽업 완료)
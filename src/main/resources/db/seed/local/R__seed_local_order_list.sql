-- -- ======================================================
-- -- Cursor Pagination & Status UI Test Seed Data
-- -- Goal: Test 6 distinct order statuses for 'Request' and 'Complete' tabs
-- -- User: 1 (local.customer@picknwhip.com)
-- -- Shop: 1 (테스트 케이크샵)
-- -- ======================================================
--
-- -- [주문 요청 내역] 테스트 데이터 (3건)
--
-- -- 1. 주문서 확인 중 (Step 1)
-- -- 상태: CONFIRM_WAIT / 결제: WAITING
-- INSERT INTO orders (
--     id, user_id, shop_id, shop_cake_size_id, design_id, status,
--     pickup_datetime,
--     payment_status, payment_method, total_price, deposit_amount,
--     customer_name, customer_phone,
--     order_code, created_at, updated_at,
--     lettering_text, lettering_line_count, lettering_alignment
-- ) VALUES (
--              201, 1, 1, 1, 1, 'CONFIRM_WAIT',
--              DATE_ADD(NOW(), INTERVAL 7 DAY), -- 픽업 7일 뒤
--              'WAITING', 'TBD', 30000, 0,
--              '테스트유저', '010-1111-1111',
--              'TEST_REQ_001', NOW(), NOW(),
--              '주문서 확인중입니다', 'ONE_LINE', 'CENTER'
--          ) ON DUPLICATE KEY UPDATE status = VALUES(status);
--
-- -- 2. 결제 요청 중 (Step 2 - 정상)
-- -- 상태: PROD_CONFIRM / 결제: WAITING
-- INSERT INTO orders (
--     id, user_id, shop_id, shop_cake_size_id, design_id, status,
--     pickup_datetime,
--     payment_status, payment_method, total_price, deposit_amount,
--     customer_name, customer_phone,
--     order_code, created_at, updated_at,
--     lettering_text, lettering_line_count, lettering_alignment
-- ) VALUES (
--              202, 1, 1, 1, 1, 'PROD_CONFIRM',
--              DATE_ADD(NOW(), INTERVAL 6 DAY), -- 픽업 6일 뒤
--              'WAITING', 'CARD', 35000, 35000,
--              '테스트유저', '010-1111-1111',
--              'TEST_REQ_002', NOW(), NOW(),
--              '결제 요청중입니다', 'ONE_LINE', 'CENTER'
--          ) ON DUPLICATE KEY UPDATE status = VALUES(status);
--
-- -- 3. 제작 불가 (Step 3 - 사장님 거절)
-- -- 상태: IMPOSSIBLE / 결제: WAITING / 사유 포함
-- INSERT INTO orders (
--     id, user_id, shop_id, shop_cake_size_id, design_id, status,
--     pickup_datetime,
--     payment_status, payment_method, total_price, deposit_amount,
--     customer_name, customer_phone,
--     order_code, created_at, updated_at,
--     rejection_reason,
--     lettering_text, lettering_line_count, lettering_alignment
-- ) VALUES (
--              203, 1, 1, 1, 1, 'IMPOSSIBLE',
--              DATE_ADD(NOW(), INTERVAL 5 DAY), -- 픽업 5일 뒤
--              'WAITING', 'CARD', 40000, 0,
--              '테스트유저', '010-1111-1111',
--              'TEST_REQ_003', NOW(), NOW(),
--              '해당 날짜는 주문 폭주로 제작이 어렵습니다. 죄송합니다.',
--              '제작 불가입니다', 'ONE_LINE', 'CENTER'
--          ) ON DUPLICATE KEY UPDATE status = VALUES(status);
--
-- -- (보너스) 결제 미완료/실패 (Step 3 - 에러 UI 테스트용)
-- -- 상태: PROD_CONFIRM / 결제: CANCELLED
-- INSERT INTO orders (
--     id, user_id, shop_id, shop_cake_size_id, design_id, status,
--     pickup_datetime,
--     payment_status, payment_method, total_price, deposit_amount,
--     customer_name, customer_phone,
--     order_code, created_at, updated_at,
--     lettering_text, lettering_line_count, lettering_alignment
-- ) VALUES (
--              204, 1, 1, 1, 1, 'PROD_CONFIRM',
--              DATE_ADD(NOW(), INTERVAL 4 DAY),
--              'CANCELLED', 'CARD', 42000, 0,
--              '테스트유저', '010-1111-1111',
--              'TEST_REQ_004', NOW(), NOW(),
--              '결제 실패 테스트', 'ONE_LINE', 'CENTER'
--          ) ON DUPLICATE KEY UPDATE status = VALUES(status);
--
--
-- -- [주문 완료 내역] 테스트 데이터 (3건)
--
-- -- 4. 제작 중 (Step 3 - 정상)
-- -- 상태: MAKING / 결제: PAID
-- INSERT INTO orders (
--     id, user_id, shop_id, shop_cake_size_id, design_id, status,
--     pickup_datetime,
--     payment_status, payment_method, total_price, deposit_amount,
--     customer_name, customer_phone,
--     order_code, created_at, updated_at,
--     lettering_text, lettering_line_count, lettering_alignment
-- ) VALUES (
--              205, 1, 1, 1, 1, 'MAKING',
--              DATE_ADD(NOW(), INTERVAL 3 DAY), -- 픽업 3일 뒤
--              'PAID', 'CARD', 45000, 45000,
--              '테스트유저', '010-1111-1111',
--              'TEST_COMP_001', NOW(), NOW(),
--              '열심히 제작중입니다', 'TWO_LINE', 'CENTER'
--          ) ON DUPLICATE KEY UPDATE status = VALUES(status);
--
-- -- 5. 픽업 대기 중 (Step 4)
-- -- 상태: PICKUP_WAIT / 결제: PAID
-- INSERT INTO orders (
--     id, user_id, shop_id, shop_cake_size_id, design_id, status,
--     pickup_datetime,
--     payment_status, payment_method, total_price, deposit_amount,
--     customer_name, customer_phone,
--     order_code, created_at, updated_at,
--     lettering_text, lettering_line_count, lettering_alignment
-- ) VALUES (
--              206, 1, 1, 1, 1, 'PICKUP_WAIT',
--              DATE_ADD(NOW(), INTERVAL 1 HOUR), -- 1시간 뒤 픽업
--              'PAID', 'CARD', 50000, 50000,
--              '테스트유저', '010-1111-1111',
--              'TEST_COMP_002', NOW(), NOW(),
--              '픽업을 기다립니다', 'TWO_LINE', 'CURVE_UP'
--          ) ON DUPLICATE KEY UPDATE status = VALUES(status);
--
-- -- 6. 픽업 완료 (Step 5)
-- -- 상태: COMPLETED / 결제: PAID
-- INSERT INTO orders (
--     id, user_id, shop_id, shop_cake_size_id, design_id, status,
--     pickup_datetime,
--     payment_status, payment_method, total_price, deposit_amount,
--     customer_name, customer_phone,
--     order_code, created_at, updated_at,
--     lettering_text, lettering_line_count, lettering_alignment
-- ) VALUES (
--              207, 1, 1, 1, 1, 'COMPLETED',
--              DATE_SUB(NOW(), INTERVAL 1 DAY), -- 어제 픽업함
--              'PAID', 'CARD', 55000, 55000,
--              '테스트유저', '010-1111-1111',
--              'TEST_COMP_003', NOW(), NOW(),
--              '맛있게 드세요', 'THREE_LINE', 'CURVE_UP_DOWN'
--          ) ON DUPLICATE KEY UPDATE status = VALUES(status);
--
-- ======================================================
-- Cursor Pagination Test Data (Updated)
-- User: 1, Shop: 1
-- ======================================================

-- 1. [주문 요청 내역]

-- 201: 주문서 확인 중 (Step 1)
INSERT INTO orders (id, user_id, shop_id, shop_cake_size_id, design_id, status, pickup_datetime, payment_status, payment_method, total_price, customer_name, customer_phone, order_code, created_at, updated_at, lettering_text)
VALUES (201, 1, 1, 1, 1, 'CONFIRM_WAIT', DATE_ADD(NOW(), INTERVAL 7 DAY), 'WAITING', 'TBD', 30000, '테스트유저', '010-1111-1111', 'TEST_REQ_001', NOW(), NOW(), '주문서 확인중')
    ON DUPLICATE KEY UPDATE status = VALUES(status);

-- 202: 결제 요청 중 (Step 2 - 정상) -> payment_status = WAITING
INSERT INTO orders (id, user_id, shop_id, shop_cake_size_id, design_id, status, pickup_datetime, payment_status, payment_method, total_price, customer_name, customer_phone, order_code, created_at, updated_at, lettering_text)
VALUES (202, 1, 1, 1, 1, 'PROD_CONFIRM', DATE_ADD(NOW(), INTERVAL 6 DAY), 'WAITING', 'CARD', 35000, '테스트유저', '010-1111-1111', 'TEST_REQ_002', NOW(), NOW(), '결제 요청중')
    ON DUPLICATE KEY UPDATE status = VALUES(status), payment_status = VALUES(payment_status);

-- 203: 제작 불가 (Step 3 - 사장님 거절)
INSERT INTO orders (id, user_id, shop_id, shop_cake_size_id, design_id, status, pickup_datetime, payment_status, payment_method, total_price, customer_name, customer_phone, order_code, created_at, updated_at, rejection_reason)
VALUES (203, 1, 1, 1, 1, 'IMPOSSIBLE', DATE_ADD(NOW(), INTERVAL 5 DAY), 'WAITING', 'CARD', 40000, '테스트유저', '010-1111-1111', 'TEST_REQ_003', NOW(), NOW(), '주문 폭주로 제작 불가')
    ON DUPLICATE KEY UPDATE status = VALUES(status);

-- 204: 결제 미완료/실패 (Step 3 - 에러) -> payment_status = CANCELLED
INSERT INTO orders (id, user_id, shop_id, shop_cake_size_id, design_id, status, pickup_datetime, payment_status, payment_method, total_price, customer_name, customer_phone, order_code, created_at, updated_at)
VALUES (204, 1, 1, 1, 1, 'PROD_CONFIRM', DATE_ADD(NOW(), INTERVAL 4 DAY), 'CANCELLED', 'CARD', 42000, '테스트유저', '010-1111-1111', 'TEST_REQ_004', NOW(), NOW())
    ON DUPLICATE KEY UPDATE status = VALUES(status), payment_status = VALUES(payment_status);


-- 2. [주문 완료 내역]

-- 205: 제작 중 (Step 3 - 정상)
INSERT INTO orders (id, user_id, shop_id, shop_cake_size_id, design_id, status, pickup_datetime, payment_status, payment_method, total_price, customer_name, customer_phone, order_code, created_at, updated_at)
VALUES (205, 1, 1, 1, 1, 'MAKING', DATE_ADD(NOW(), INTERVAL 3 DAY), 'PAID', 'CARD', 45000, '테스트유저', '010-1111-1111', 'TEST_COMP_001', NOW(), NOW())
    ON DUPLICATE KEY UPDATE status = VALUES(status);

-- 206: 픽업 대기 (Step 4)
INSERT INTO orders (id, user_id, shop_id, shop_cake_size_id, design_id, status, pickup_datetime, payment_status, payment_method, total_price, customer_name, customer_phone, order_code, created_at, updated_at)
VALUES (206, 1, 1, 1, 1, 'PICKUP_WAIT', DATE_ADD(NOW(), INTERVAL 1 HOUR), 'PAID', 'CARD', 50000, '테스트유저', '010-1111-1111', 'TEST_COMP_002', NOW(), NOW())
    ON DUPLICATE KEY UPDATE status = VALUES(status);

-- 207: 픽업 완료 (Step 5)
INSERT INTO orders (id, user_id, shop_id, shop_cake_size_id, design_id, status, pickup_datetime, payment_status, payment_method, total_price, customer_name, customer_phone, order_code, created_at, updated_at)
VALUES (207, 1, 1, 1, 1, 'COMPLETED', DATE_SUB(NOW(), INTERVAL 1 DAY), 'PAID', 'CARD', 55000, '테스트유저', '010-1111-1111', 'TEST_COMP_003', NOW(), NOW())
    ON DUPLICATE KEY UPDATE status = VALUES(status);


-- 3. [주문 아이템 추가] (필수: 옵션 정보를 위해 추가)
-- Order 201 ~ 207에 대해 각각 '초코 시트' 옵션 1개씩 추가
INSERT INTO order_items (order_id, custom_option_id, option_category, option_name, unit_price)
VALUES
    (201, 1, 'SHEET', '초코 시트', 2000),
    (202, 1, 'SHEET', '초코 시트', 2000),
    (203, 1, 'SHEET', '초코 시트', 2000),
    (204, 1, 'SHEET', '초코 시트', 2000),
    (205, 1, 'SHEET', '초코 시트', 2000),
    (206, 1, 'SHEET', '초코 시트', 2000),
    (207, 1, 'SHEET', '초코 시트', 2000)
    ON DUPLICATE KEY UPDATE unit_price = VALUES(unit_price);
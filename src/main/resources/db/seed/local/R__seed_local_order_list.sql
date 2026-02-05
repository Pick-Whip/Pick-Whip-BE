-- ======================================================
-- Cursor Pagination & Status UI Test Seed Data
-- Goal: Test 6 distinct order statuses for 'Request' and 'Complete' tabs
-- User: 1 (local.customer@picknwhip.com)
-- Shop: 1 (테스트 케이크샵)
-- ======================================================

-- 1. [초기화] 기존 테스트 데이터 삭제 (멱등성 보장)
-- 외래키 제약조건 때문에 자식 테이블(Items, Histories)부터 지워야 합니다.
DELETE FROM order_histories WHERE order_id BETWEEN 201 AND 207;
DELETE FROM order_items WHERE order_id BETWEEN 201 AND 207;
DELETE FROM orders WHERE id BETWEEN 201 AND 207;

-- ★ [핵심 수정] 옵션 데이터 사전 생성 (FK 에러 방지)
-- order_items가 참조할 '초코 시트(1)'와 '생크림(2)'이 없으면 에러가 납니다.
-- 따라서 여기서 미리 존재 여부를 확인하고 넣어줍니다.
INSERT INTO custom_options (id, shop_id, category, option_name, additional_price)
VALUES
    (1, 1, 'SHEET', '초코 시트', 2000),
    (2, 1, 'CREAM', '생크림', 1000)
    ON DUPLICATE KEY UPDATE
                         additional_price = VALUES(additional_price),
                         option_name = VALUES(option_name);


-- 2. [주문 요청 내역] 테스트 데이터 생성 (3건)

-- (1) 주문서 확인 중 (Step 1)
INSERT INTO orders (
    id, user_id, shop_id, shop_cake_size_id, design_id, status,
    pickup_datetime,
    payment_status, payment_method, total_price, deposit_amount,
    customer_name, customer_phone,
    order_code, created_at, updated_at,
    lettering_text, lettering_line_count, lettering_alignment
) VALUES (
             201, 1, 1, 1, 1, 'CONFIRM_WAIT',
             DATE_ADD(NOW(), INTERVAL 7 DAY),
             'WAITING', 'TBD', 30000, 0,
             '테스트유저', '010-1111-1111',
             'TEST_REQ_001', NOW(), NOW(),
             '주문서 확인중입니다', 'ONE_LINE', 'CENTER'
         );

-- (2) 결제 요청 중 (Step 2)
INSERT INTO orders (
    id, user_id, shop_id, shop_cake_size_id, design_id, status,
    pickup_datetime,
    payment_status, payment_method, total_price, deposit_amount,
    customer_name, customer_phone,
    order_code, created_at, updated_at,
    lettering_text, lettering_line_count, lettering_alignment
) VALUES (
             202, 1, 1, 1, 1, 'PROD_CONFIRM',
             DATE_ADD(NOW(), INTERVAL 6 DAY),
             'WAITING', 'TBD', 32000, 0,
             '테스트유저', '010-1111-1111',
             'TEST_REQ_002', NOW(), NOW(),
             '결제요청 테스트', 'ONE_LINE', 'CENTER'
         );

-- (3) 제작 불가 (Step 3 - Error Case)
-- ★ 이미지 2번 케이스: 2시간 전 주문, 1시간 전 확인, 지금 거절
INSERT INTO orders (
    id, user_id, shop_id, shop_cake_size_id, design_id, status,
    pickup_datetime,
    payment_status, payment_method, total_price, deposit_amount,
    customer_name, customer_phone,
    order_code, created_at, updated_at,
    lettering_text, lettering_line_count, lettering_alignment,
    rejection_reason
) VALUES (
             203, 1, 1, 1, 1, 'IMPOSSIBLE',
             DATE_ADD(NOW(), INTERVAL 5 DAY),
             'WAITING', 'TBD', 0, 0,
             '테스트유저', '010-1111-1111',
             'TEST_REQ_003', DATE_SUB(NOW(), INTERVAL 2 HOUR), DATE_SUB(NOW(), INTERVAL 2 HOUR),
             '제작 불가입니다', 'ONE_LINE', 'CENTER',
             '해당 날짜는 주문 폭주로 제작이 어렵습니다. 죄송합니다.'
         );


-- 3. [주문 완료 내역] 테스트 데이터 생성 (3건)

-- (4) 제작 확정 / 제작 중 (Step 4)
INSERT INTO orders (
    id, user_id, shop_id, shop_cake_size_id, design_id, status,
    pickup_datetime,
    payment_status, payment_method, total_price, deposit_amount,
    customer_name, customer_phone,
    order_code, created_at, updated_at,
    lettering_text, lettering_line_count, lettering_alignment
) VALUES (
             204, 1, 1, 1, 1, 'MAKING',
             DATE_ADD(NOW(), INTERVAL 1 DAY),
             'PAID', 'CARD', 45000, 45000,
             '테스트유저', '010-1111-1111',
             'TEST_COMP_001', NOW(), NOW(),
             '제작중 테스트', 'TWO_LINE', 'CURVE_UP'
         );

-- (5) 픽업 대기 중 (Step 5)
INSERT INTO orders (
    id, user_id, shop_id, shop_cake_size_id, design_id, status,
    pickup_datetime,
    payment_status, payment_method, total_price, deposit_amount,
    customer_name, customer_phone,
    order_code, created_at, updated_at,
    lettering_text, lettering_line_count, lettering_alignment
) VALUES (
             205, 1, 1, 1, 1, 'PICKUP_WAIT',
             NOW(),
             'PAID', 'CARD', 45000, 45000,
             '테스트유저', '010-1111-1111',
             'TEST_COMP_002', NOW(), NOW(),
             '픽업대기 테스트', 'ONE_LINE', 'CENTER'
         );

-- (6) 픽업 완료 (Step 6)
INSERT INTO orders (
    id, user_id, shop_id, shop_cake_size_id, design_id, status,
    pickup_datetime,
    payment_status, payment_method, total_price, deposit_amount,
    customer_name, customer_phone,
    order_code, created_at, updated_at,
    lettering_text, lettering_line_count, lettering_alignment
) VALUES (
             206, 1, 1, 1, 1, 'COMPLETED',
             DATE_SUB(NOW(), INTERVAL 1 DAY),
             'PAID', 'CARD', 45000, 45000,
             '테스트유저', '010-1111-1111',
             'TEST_COMP_003', NOW(), NOW(),
             '픽업완료 테스트', 'ONE_LINE', 'CENTER'
         );


-- 4. [주문 아이템 연결] (옵션 정보 매핑)
-- 위에서 생성한 custom_option_id (1, 2)를 사용하여 매핑합니다.

-- (1) '초코 시트' (ID: 1) 매핑
INSERT INTO order_items (order_id, custom_option_id, option_category, option_name, unit_price)
VALUES
    (201, 1, 'SHEET', '초코 시트', 2000),
    (202, 1, 'SHEET', '초코 시트', 2000),
    (203, 1, 'SHEET', '초코 시트', 2000),
    (204, 1, 'SHEET', '초코 시트', 2000),
    (205, 1, 'SHEET', '초코 시트', 2000),
    (206, 1, 'SHEET', '초코 시트', 2000);

-- (2) '생크림' (ID: 2) 매핑
INSERT INTO order_items (order_id, custom_option_id, option_category, option_name, unit_price)
VALUES
    (201, 2, 'CREAM', '생크림', 1000),
    (202, 2, 'CREAM', '생크림', 1000),
    (203, 2, 'CREAM', '생크림', 1000),
    (204, 2, 'CREAM', '생크림', 1000),
    (205, 2, 'CREAM', '생크림', 1000),
    (206, 2, 'CREAM', '생크림', 1000);


-- 5. [타임라인 히스토리 추가] (이미지 2 구현용)

-- 201번 (주문서 확인 중) -> 작성 완료
INSERT INTO order_histories (order_id, status, created_at, updated_at)
VALUES (201, 'CONFIRM_WAIT', NOW(), NOW());

-- 202번 (결제 요청 중) -> 작성 -> 확인
INSERT INTO order_histories (order_id, status, created_at, updated_at)
VALUES
    (202, 'CONFIRM_WAIT', DATE_SUB(NOW(), INTERVAL 1 HOUR), DATE_SUB(NOW(), INTERVAL 1 HOUR)),
    (202, 'PROD_CONFIRM', NOW(), NOW());

-- ★ 203번 (제작 불가) -> 작성(2시간전) -> 확인(1시간전) -> 불가(현재)
INSERT INTO order_histories (order_id, status, created_at, updated_at)
VALUES
    (203, 'CONFIRM_WAIT', DATE_SUB(NOW(), INTERVAL 2 HOUR), DATE_SUB(NOW(), INTERVAL 2 HOUR)),
    (203, 'PROD_CONFIRM', DATE_SUB(NOW(), INTERVAL 1 HOUR), DATE_SUB(NOW(), INTERVAL 1 HOUR)),
    (203, 'IMPOSSIBLE', NOW(), NOW());

-- 204번 (제작 중)
INSERT INTO order_histories (order_id, status, created_at, updated_at)
VALUES
    (204, 'CONFIRM_WAIT', DATE_SUB(NOW(), INTERVAL 3 HOUR), DATE_SUB(NOW(), INTERVAL 3 HOUR)),
    (204, 'PROD_CONFIRM', DATE_SUB(NOW(), INTERVAL 2 HOUR), DATE_SUB(NOW(), INTERVAL 2 HOUR)),
    (204, 'MAKING', NOW(), NOW());
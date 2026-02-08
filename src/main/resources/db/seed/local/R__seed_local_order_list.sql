-- ======================================================
-- 주문 상세조회 API 테스트용 시드 데이터 (Repeatable)
-- Goal: GET /api/orders/{orderId} 완벽 테스트
-- design_id NULL 제거 버전 (모든 주문에 design_id = 1)
-- ======================================================

-- 기존 테스트 데이터 정리 (멱등성 보장)
DELETE FROM order_histories WHERE order_id BETWEEN 300 AND 305;
DELETE FROM order_items WHERE order_id BETWEEN 300 AND 305;
DELETE FROM orders WHERE id BETWEEN 300 AND 305;

-- ★ 필수 옵션 데이터 생성 (FK 에러 방지)
INSERT INTO custom_options (id, shop_id, category, option_name, additional_price, color_rgb_code)
VALUES
    (10, 1, 'SHEET', '초코 시트', 2000, NULL),
    (11, 1, 'CREAM', '생크림', 1000, NULL),
    (12, 1, 'TOPPING', '딸기', 3000, NULL),
    (13, 1, 'ICING', '초콜릿 데코', 2500, NULL)
    ON DUPLICATE KEY UPDATE
                         shop_id = VALUES(shop_id),
                         category = VALUES(category),
                         option_name = VALUES(option_name),
                         additional_price = VALUES(additional_price),
                         color_rgb_code = VALUES(color_rgb_code);


-- ======================================================
-- 테스트 케이스 1: 주문서 확인 중 (CONFIRM_WAIT)
-- ======================================================
INSERT INTO orders (
    id, user_id, shop_id, shop_cake_size_id, design_id, status,
    pickup_datetime, payment_status, payment_method, total_price,
    customer_name, customer_phone, order_code,
    lettering_text, lettering_line_count, lettering_alignment,
    additional_request, order_additional_request, reference_image_url,
    created_at, updated_at
) VALUES (
             300, 1, 1, 1, 1, 'CONFIRM_WAIT',
             DATE_ADD(NOW(), INTERVAL 3 DAY) + INTERVAL 14 HOUR,
             'WAITING', 'TBD', 38500,
             '김픽휩', '010-1111-1111', '260207_300',
             'Happy Birthday!', 'ONE_LINE', 'CENTER',
             '딸기 많이 올려주세요', '포크 3개 부탁드립니다',
             'https://example.com/cake1.jpg',
             NOW(), NOW()
         );

-- 주문 아이템
INSERT INTO order_items (order_id, custom_option_id, option_category, option_name, unit_price)
VALUES
    (300, 10, 'SHEET', '초코 시트', 2000),
    (300, 11, 'CREAM', '생크림', 1000),
    (300, 12, 'TOPPING', '딸기', 3000);

-- 히스토리 (주문서 작성만 완료)
INSERT INTO order_histories (order_id, status, created_at, updated_at)
VALUES (300, 'CONFIRM_WAIT', NOW(), NOW());


-- ======================================================
-- 테스트 케이스 2: 결제 요청 중 (PAYMENT_WAIT)
-- [수정] 기존 PROD_CONFIRM + WAITING 조합 -> PAYMENT_WAIT 상태로 변경
-- ======================================================
INSERT INTO orders (
    id, user_id, shop_id, shop_cake_size_id, design_id, status,
    pickup_datetime, payment_status, payment_method, total_price,
    customer_name, customer_phone, order_code,
    lettering_text, lettering_line_count, lettering_alignment,
    additional_request, order_additional_request, reference_image_url,
    created_at, updated_at
) VALUES (
             301, 1, 1, 1, 1, 'PAYMENT_WAIT', -- [수정] 새로운 상태값 적용
             DATE_ADD(NOW(), INTERVAL 4 DAY) + INTERVAL 15 HOUR,
             'WAITING', 'TBD', 38500,
             '김픽휩', '010-1111-1111', '260207_301',
             'Congratulations!', 'TWO_LINE', 'CURVE_UP',
             '알러지 주의해주세요', '배송지 주소 확인 부탁드립니다',
             'https://example.com/cake2.jpg',
             DATE_SUB(NOW(), INTERVAL 2 HOUR), DATE_SUB(NOW(), INTERVAL 2 HOUR)
         );

INSERT INTO order_items (order_id, custom_option_id, option_category, option_name, unit_price)
VALUES
    (301, 10, 'SHEET', '초코 시트', 2000),
    (301, 11, 'CREAM', '생크림', 1000),
    (301, 12, 'TOPPING', '딸기', 3000);

-- 히스토리 (작성 -> 사장님 수락/결제요청)
INSERT INTO order_histories (order_id, status, created_at, updated_at)
VALUES
    (301, 'CONFIRM_WAIT', DATE_SUB(NOW(), INTERVAL 2 HOUR), DATE_SUB(NOW(), INTERVAL 2 HOUR)),
    (301, 'PAYMENT_WAIT', DATE_SUB(NOW(), INTERVAL 30 MINUTE), DATE_SUB(NOW(), INTERVAL 30 MINUTE)); -- [수정]


-- ======================================================
-- 테스트 케이스 3: 제작 불가 (CANCELED_BY_SHOP)
-- [수정] 기존 IMPOSSIBLE -> CANCELED_BY_SHOP (사장님 거절)
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
             302, 1, 1, 1, 1, 'CANCELED_BY_SHOP', -- [수정] 새로운 상태값 적용
             DATE_ADD(NOW(), INTERVAL 5 DAY) + INTERVAL 16 HOUR,
             'WAITING', 'TBD', 38500,
             '김픽휩', '010-1111-1111', '260207_302',
             'Welcome!', 'ONE_LINE', 'CENTER',
             '특별한 디자인 요청', '초는 빼주세요',
             'https://example.com/cake3.jpg',
             '죄송합니다. 해당 날짜는 예약이 모두 마감되어 제작이 어렵습니다. 다른 날짜로 변경 부탁드립니다.',
             DATE_SUB(NOW(), INTERVAL 3 HOUR), DATE_SUB(NOW(), INTERVAL 3 HOUR)
         );

INSERT INTO order_items (order_id, custom_option_id, option_category, option_name, unit_price)
VALUES
    (302, 10, 'SHEET', '초코 시트', 2000),
    (302, 11, 'CREAM', '생크림', 1000),
    (302, 12, 'TOPPING', '딸기', 3000);

-- 히스토리 (작성 -> 확인(결제요청 단계) -> 거절)
-- 참고: 로직상 결제 요청 후 거절된 시나리오라고 가정 (또는 바로 거절일 수도 있음)
INSERT INTO order_histories (order_id, status, created_at, updated_at)
VALUES
    (302, 'CONFIRM_WAIT', DATE_SUB(NOW(), INTERVAL 3 HOUR), DATE_SUB(NOW(), INTERVAL 3 HOUR)),
    -- (302, 'PAYMENT_WAIT', ...) -- 사장님이 바로 거절했다면 이 단계 생략 가능
    (302, 'CANCELED_BY_SHOP', NOW(), NOW()); -- [수정]


-- ======================================================
-- 테스트 케이스 4: 제작 중 (MAKING)
-- ======================================================
INSERT INTO orders (
    id, user_id, shop_id, shop_cake_size_id, design_id, status,
    pickup_datetime, payment_status, payment_method, total_price,
    customer_name, customer_phone, order_code,
    lettering_text, lettering_line_count, lettering_alignment,
    additional_request, order_additional_request, reference_image_url,
    created_at, updated_at
) VALUES (
             303, 1, 1, 1, 1, 'MAKING',
             DATE_ADD(NOW(), INTERVAL 1 DAY) + INTERVAL 17 HOUR,
             'PAID', 'CARD', 41000, -- [확인] PaymentStatus.PAID 사용
             '김픽휩', '010-1111-1111', '260207_303',
             'Thank You!', 'TWO_LINE', 'CURVE_UP_DOWN',
             '예쁘게 만들어주세요', '케이크 박스 2개로 나눠주세요',
             'https://example.com/cake4.jpg',
             DATE_SUB(NOW(), INTERVAL 1 DAY), DATE_SUB(NOW(), INTERVAL 1 DAY)
         );

INSERT INTO order_items (order_id, custom_option_id, option_category, option_name, unit_price)
VALUES
    (303, 10, 'SHEET', '초코 시트', 2000),
    (303, 11, 'CREAM', '생크림', 1000),
    (303, 12, 'TOPPING', '딸기', 3000),
    (303, 13, 'ICING', '초콜릿 데코', 2500);

-- 히스토리 (작성 -> 결제요청 -> 제작확정 -> 제작중)
INSERT INTO order_histories (order_id, status, created_at, updated_at)
VALUES
    (303, 'CONFIRM_WAIT', DATE_SUB(NOW(), INTERVAL 1 DAY), DATE_SUB(NOW(), INTERVAL 1 DAY)),
    (303, 'PAYMENT_WAIT', DATE_SUB(NOW(), INTERVAL 23 HOUR), DATE_SUB(NOW(), INTERVAL 23 HOUR)), -- [수정] 결제 요청 단계 추가
    (303, 'PROD_CONFIRM', DATE_SUB(NOW(), INTERVAL 20 HOUR), DATE_SUB(NOW(), INTERVAL 20 HOUR)),
    (303, 'MAKING', DATE_SUB(NOW(), INTERVAL 2 HOUR), DATE_SUB(NOW(), INTERVAL 2 HOUR));


-- ======================================================
-- 테스트 케이스 5: 픽업 대기 중 (PICKUP_WAIT)
-- ======================================================
INSERT INTO orders (
    id, user_id, shop_id, shop_cake_size_id, design_id, status,
    pickup_datetime, payment_status, payment_method, total_price,
    customer_name, customer_phone, order_code,
    lettering_text, lettering_line_count, lettering_alignment,
    additional_request, order_additional_request, reference_image_url,
    created_at, updated_at
) VALUES (
             304, 1, 1, 1, 1, 'PICKUP_WAIT',
             DATE_ADD(NOW(), INTERVAL 3 HOUR),
             'PAID', 'CARD', 41000,
             '김픽휩', '010-1111-1111', '260207_304',
             'Good Luck!', 'ONE_LINE', 'CENTER',
             '레터링 진하게 해주세요', '픽업 시간 정확히 지키겠습니다',
             'https://example.com/cake5.jpg',
             DATE_SUB(NOW(), INTERVAL 2 DAY), DATE_SUB(NOW(), INTERVAL 2 DAY)
         );

INSERT INTO order_items (order_id, custom_option_id, option_category, option_name, unit_price)
VALUES
    (304, 10, 'SHEET', '초코 시트', 2000),
    (304, 11, 'CREAM', '생크림', 1000),
    (304, 12, 'TOPPING', '딸기', 3000),
    (304, 13, 'ICING', '초콜릿 데코', 2500);

-- 히스토리 (전체 프로세스)
INSERT INTO order_histories (order_id, status, created_at, updated_at)
VALUES
    (304, 'CONFIRM_WAIT', DATE_SUB(NOW(), INTERVAL 2 DAY), DATE_SUB(NOW(), INTERVAL 2 DAY)),
    (304, 'PAYMENT_WAIT', DATE_SUB(NOW(), INTERVAL 47 HOUR), DATE_SUB(NOW(), INTERVAL 47 HOUR)), -- [수정]
    (304, 'PROD_CONFIRM', DATE_SUB(NOW(), INTERVAL 44 HOUR), DATE_SUB(NOW(), INTERVAL 44 HOUR)),
    (304, 'MAKING', DATE_SUB(NOW(), INTERVAL 5 HOUR), DATE_SUB(NOW(), INTERVAL 5 HOUR)),
    (304, 'PICKUP_WAIT', DATE_SUB(NOW(), INTERVAL 1 HOUR), DATE_SUB(NOW(), INTERVAL 1 HOUR));


-- ======================================================
-- 테스트 케이스 6: 픽업 완료 (COMPLETED)
-- ======================================================
INSERT INTO orders (
    id, user_id, shop_id, shop_cake_size_id, design_id, status,
    pickup_datetime, payment_status, payment_method, total_price,
    customer_name, customer_phone, order_code,
    lettering_text, lettering_line_count, lettering_alignment,
    additional_request, order_additional_request, reference_image_url,
    created_at, updated_at
) VALUES (
             305, 1, 1, 1, 1, 'COMPLETED',
             DATE_SUB(NOW(), INTERVAL 1 DAY),
             'PAID', 'CARD', 38500,
             '김픽휩', '010-1111-1111', '260207_305',
             'Best Wishes!', 'ONE_LINE', 'CENTER',
             '깔끔하게 포장해주세요', '감사합니다!',
             'https://example.com/cake6.jpg',
             DATE_SUB(NOW(), INTERVAL 3 DAY), DATE_SUB(NOW(), INTERVAL 3 DAY)
         );

INSERT INTO order_items (order_id, custom_option_id, option_category, option_name, unit_price)
VALUES
    (305, 10, 'SHEET', '초코 시트', 2000),
    (305, 11, 'CREAM', '생크림', 1000),
    (305, 12, 'TOPPING', '딸기', 3000);

-- 히스토리 (전체 프로세스 + 완료)
INSERT INTO order_histories (order_id, status, created_at, updated_at)
VALUES
    (305, 'CONFIRM_WAIT', DATE_SUB(NOW(), INTERVAL 3 DAY), DATE_SUB(NOW(), INTERVAL 3 DAY)),
    (305, 'PAYMENT_WAIT', DATE_SUB(NOW(), INTERVAL 71 HOUR), DATE_SUB(NOW(), INTERVAL 71 HOUR)), -- [수정]
    (305, 'PROD_CONFIRM', DATE_SUB(NOW(), INTERVAL 68 HOUR), DATE_SUB(NOW(), INTERVAL 68 HOUR)),
    (305, 'MAKING', DATE_SUB(NOW(), INTERVAL 30 HOUR), DATE_SUB(NOW(), INTERVAL 30 HOUR)),
    (305, 'PICKUP_WAIT', DATE_SUB(NOW(), INTERVAL 25 HOUR), DATE_SUB(NOW(), INTERVAL 25 HOUR)),
    (305, 'COMPLETED', DATE_SUB(NOW(), INTERVAL 1 DAY), DATE_SUB(NOW(), INTERVAL 1 DAY));


-- ======================================================
-- 테스트 요약
-- ======================================================
-- 주문 ID 300: CONFIRM_WAIT (주문서 확인 중)
-- 주문 ID 301: PAYMENT_WAIT (결제 요청 중) [수정됨]
-- 주문 ID 302: CANCELED_BY_SHOP (제작 불가) [수정됨]
-- 주문 ID 303: MAKING (제작 중)
-- 주문 ID 304: PICKUP_WAIT (픽업 대기)
-- 주문 ID 305: COMPLETED (픽업 완료)
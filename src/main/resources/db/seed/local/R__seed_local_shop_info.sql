-- ======================================================
-- Shop Info Tab API 테스트용 시드 데이터 (Repeatable)
-- Goal: GET /api/shops/{shopId}/info 완벽 테스트
-- Shop ID: 1 (R__seed_local.sql 에서 이미 생성된 shop 사용)
-- 이 파일은 shop 1의 추가 정보를 보완합니다.
-- ======================================================

-- shop 1의 케이크 사이즈는 R__seed_local.sql 에서 이미 생성됨 (size_id 1~3)
-- 여기서는 별도로 삽입할 필요 없음

-- 계좌 정보는 R__seed_local.sql 에서 이미 생성됨
-- 이벤트 데이터는 R__seed_local.sql 에서 이미 생성됨
-- 운영 시간은 R__seed_local.sql 에서 이미 생성됨

-- 필요시 shop 1 정보 업데이트 (이 파일에서 추가 보완)
UPDATE shops
SET
    pickup_time_guide   = 4,
    day_order_guide     = 1,
    parking_guide       = '매장 앞 공영주차장 이용 가능',
    payment_notice      = '온라인 결제 시 주문 확정 후 3일 이내 취소 가능',
    precaution_notice   = '주문 후 변경은 픽업 2일 전까지 가능합니다.\n생과일 케이크는 당일 소비를 권장합니다.\n알러지가 있으신 경우 주문 시 꼭 알려주세요.',
    prepayment          = 20000
WHERE shop_id = 1;

-- ======================================================

-- 1. 테스트용 사장님 계정 (users 테이블에는 updated_at이 존재함)
INSERT INTO users (user_id, created_at, updated_at, email, kakao_id, name, nickname, phone, status)
VALUES (999, NOW(6), NOW(6), 'info.test@picknwhip.com', 9000000001, '정보탭테스터', '정보사장님', '010-9999-9999', 'ACTIVE')
    ON DUPLICATE KEY UPDATE updated_at = NOW(6);

-- 2. 테스트용 매장 (ID: 10)
-- [수정] shops 테이블에 'updated_at' 컬럼 제거
INSERT INTO shops (
    shop_id, owner_id, shop_name, phone, address, location, status, verification_status,
    description,
    pickup_time_guide, day_order_guide, parking_guide,
    payment_notice, precaution_notice, prepayment,
    created_at
) VALUES (
             10, 999, '정보 탭 테스트 매장', '02-555-5555', '서울시 강남구 테헤란로', ST_GeomFromText('POINT(37.5 127.0)', 4326), 'ACTIVE', 'VERIFIED',
             '정보 탭 API 테스트를 위한 매장입니다.',
             3,           -- pickup_time_guide
             0,  -- day_order_guide
             '매장 앞 공영주차장 이용 가능',     -- parking_guide
             '온라인 결제 시 주문 확정 후 3일 이내 취소 가능', -- payment_notice
             '주문 후 변경은 픽업 2일 전까지 가능합니다.\n생과일 케이크는 당일 소비를 권장합니다.\n알러지가 있으신 경우 주문 시 꼭 알려주세요.', -- precaution_notice
             20000, -- prepayment (선입금 2만원)
             NOW(6) -- created_at만 입력
         )
    ON DUPLICATE KEY UPDATE
                         pickup_time_guide = VALUES(pickup_time_guide),
                         precaution_notice = VALUES(precaution_notice);

-- 3. 케이크 사이즈 (가격 차액 테스트용)
DELETE FROM shop_cake_sizes WHERE shop_id = 10;
INSERT INTO shop_cake_sizes (shop_id, size_name, diameter, price) VALUES
                                                                      (10, '도시락', '10cm', 20000),
                                                                      (10, '1호', '15cm', 30000),
                                                                      (10, '2호', '18cm', 40000);

-- 5. 계좌 정보 (계좌이체 아이콘 뜨는지 확인용)
DELETE FROM bank_accounts WHERE shop_id = 10;
INSERT INTO bank_accounts (shop_id, bank_name, account_number, account_holder, is_primary) VALUES
    (10, '국민은행', '123-456-7890', '정보사장님', true);

-- 6. 진행 중인 이벤트 (오늘 날짜 포함)
DELETE FROM shop_events WHERE shop_id = 10;
INSERT INTO shop_events (shop_id, title, content, start_date, end_date, is_active) VALUES
                                                                                       (10, '11월 특별 할인', '레터링 케이크 주문 시 10% 할인해드립니다!', CURRENT_DATE, DATE_ADD(CURRENT_DATE, INTERVAL 10 DAY), true),
                                                                                       (10, '종료된 이벤트', '이 이벤트는 보이면 안 됩니다.', DATE_SUB(CURRENT_DATE, INTERVAL 20 DAY), DATE_SUB(CURRENT_DATE, INTERVAL 10 DAY), true);

-- 4. 운영 시간 (스마트 포맷팅 테스트용: 평일/주말 다르게 설정)
DELETE FROM shop_business_hours WHERE shop_id = 10;
INSERT INTO shop_business_hours (shop_id, schedule_type, day_of_week, open_time, close_time, is_closed) VALUES
-- 평일 (월~금): 10:00 ~ 20:00
(10, 'WEEKLY', 1, '00:00:00', '00:00:00', true), -- 월
(10, 'WEEKLY', 2, '10:00:00', '20:00:00', false), -- 화
(10, 'WEEKLY', 3, '10:00:00', '20:00:00', false), -- 수
(10, 'WEEKLY', 4, '10:00:00', '20:00:00', false), -- 목
(10, 'WEEKLY', 5, '10:00:00', '20:00:00', false), -- 금

-- 주말 (토~일): 12:00 ~ 22:00
(10, 'WEEKLY', 6, '12:00:00', '22:00:00', false), -- 토
(10, 'WEEKLY', 7, '12:00:00', '22:00:00', false); -- 일
-- 테스트 확인 사항
-- ======================================================
-- GET /api/shops/1/info 호출 시 아래 항목이 모두 반환되어야 함:
--   - 케이크 사이즈: 도시락(10cm, 20,000원), 1호(15cm, 30,000원), 2호(18cm, 40,000원)
--   - 계좌: 국민은행 123-456-7890 마포사장님
--   - 이벤트: 봄맞이 특별 할인 (진행 중만 노출)
--   - 운영 시간: 월 휴무, 화~금 10:00~20:00, 토~일 12:00~22:00
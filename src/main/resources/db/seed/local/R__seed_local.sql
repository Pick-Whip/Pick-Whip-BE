-- ======================================================
-- Local Seed Data (Repeatable)
-- Profile: local
-- Goal   : Safe to run multiple times (idempotent)
-- User 1번을 기준으로 모든 API 테스트 가능
-- ======================================================

/* ------------------------------------------------------
   1) 유저 데이터
   - user_id 1: Customer (테스트 주인공) - 모든 API 테스트용
   - user_id 2: Shop Owner (가게 1번 사장님)
   - user_id 3: Shop Owner (가게 2번 사장님)
   - user_id 4~8: 추가 리뷰/좋아요 테스터
   - user_id 9~23: 리뷰 좋아요 더미 유저
   ------------------------------------------------------ */
INSERT INTO users (
    user_id, created_at, updated_at, deleted_at,
    email, kakao_id, name, nickname, birthdate, phone, profile_image_url, status
) VALUES
      -- user 1: 테스트 주인공 (모든 API 테스트)
      (1, NOW(6), NOW(6), NULL, 'local.customer@picknwhip.com', 2000000001, '사용자', '초코케이크', NULL, '010-1111-1111', NULL, 'ACTIVE'),
      -- user 2: 가게 1번 사장님
      (2, NOW(6), NOW(6), NULL, 'local.owner1@picknwhip.com', 1000000001, '사장님1', '마포사장님', NULL, '010-2222-2222', NULL, 'ACTIVE'),
      -- user 3: 가게 2번 사장님
      (3, NOW(6), NOW(6), NULL, 'local.owner2@picknwhip.com', 1000000002, '사장님2', '강남사장님', NULL, '010-3333-3333', NULL, 'ACTIVE'),
      -- user 4~8: 리뷰어 & 좋아요 테스터
      (4, NOW(6), NOW(6), NULL, 'local.tester2@picknwhip.com', 2000000002, '테스터2', '딸기케이크', NULL, '010-4444-4444', NULL, 'ACTIVE'),
      (5, NOW(6), NOW(6), NULL, 'local.tester3@picknwhip.com', 2000000003, '테스터3', '바닐라케이크', NULL, '010-5555-5555', NULL, 'ACTIVE'),
      (6, NOW(6), NOW(6), NULL, 'local.tester4@picknwhip.com', 2000000004, '테스터4', '당근케이크', NULL, '010-6666-6666', NULL, 'ACTIVE'),
      (7, NOW(6), NOW(6), NULL, 'reviewer1@picknwhip.com', 2000000005, '리뷰어1', '초코러버', NULL, '010-7777-7777', NULL, 'ACTIVE'),
      (8, NOW(6), NOW(6), NULL, 'reviewer2@picknwhip.com', 2000000006, '리뷰어2', '딸기공주', NULL, '010-8888-8888', NULL, 'ACTIVE'),
      -- user 9~23: 리뷰 좋아요 더미 유저
      (9,  NOW(6), NOW(6), NULL, 'liker1@picknwhip.com', 3000000001, '좋아요1', '좋아요유저1', NULL, '010-0001-0001', NULL, 'ACTIVE'),
      (10, NOW(6), NOW(6), NULL, 'liker2@picknwhip.com', 3000000002, '좋아요2', '좋아요유저2', NULL, '010-0002-0002', NULL, 'ACTIVE'),
      (11, NOW(6), NOW(6), NULL, 'liker3@picknwhip.com', 3000000003, '좋아요3', '좋아요유저3', NULL, '010-0003-0003', NULL, 'ACTIVE'),
      (12, NOW(6), NOW(6), NULL, 'liker4@picknwhip.com', 3000000004, '좋아요4', '좋아요유저4', NULL, '010-0004-0004', NULL, 'ACTIVE'),
      (13, NOW(6), NOW(6), NULL, 'liker5@picknwhip.com', 3000000005, '좋아요5', '좋아요유저5', NULL, '010-0005-0005', NULL, 'ACTIVE'),
      (14, NOW(6), NOW(6), NULL, 'liker6@picknwhip.com', 3000000006, '좋아요6', '좋아요유저6', NULL, '010-0006-0006', NULL, 'ACTIVE'),
      (15, NOW(6), NOW(6), NULL, 'liker7@picknwhip.com', 3000000007, '좋아요7', '좋아요유저7', NULL, '010-0007-0007', NULL, 'ACTIVE'),
      (16, NOW(6), NOW(6), NULL, 'liker8@picknwhip.com', 3000000008, '좋아요8', '좋아요유저8', NULL, '010-0008-0008', NULL, 'ACTIVE'),
      (17, NOW(6), NOW(6), NULL, 'liker9@picknwhip.com', 3000000009, '좋아요9', '좋아요유저9', NULL, '010-0009-0009', NULL, 'ACTIVE'),
      (18, NOW(6), NOW(6), NULL, 'liker10@picknwhip.com', 3000000010, '좋아요10', '좋아요유저10', NULL, '010-0010-0010', NULL, 'ACTIVE'),
      (19, NOW(6), NOW(6), NULL, 'liker11@picknwhip.com', 3000000011, '좋아요11', '좋아요유저11', NULL, '010-0011-0011', NULL, 'ACTIVE'),
      (20, NOW(6), NOW(6), NULL, 'liker12@picknwhip.com', 3000000012, '좋아요12', '좋아요유저12', NULL, '010-0012-0012', NULL, 'ACTIVE'),
      (21, NOW(6), NOW(6), NULL, 'liker13@picknwhip.com', 3000000013, '좋아요13', '좋아요유저13', NULL, '010-0013-0013', NULL, 'ACTIVE'),
      (22, NOW(6), NOW(6), NULL, 'liker14@picknwhip.com', 3000000014, '좋아요14', '좋아요유저14', NULL, '010-0014-0014', NULL, 'ACTIVE'),
      (23, NOW(6), NOW(6), NULL, 'liker15@picknwhip.com', 3000000015, '좋아요15', '좋아요유저15', NULL, '010-0015-0015', NULL, 'ACTIVE')
    AS new
ON DUPLICATE KEY UPDATE
                     updated_at = new.updated_at,
                     status = new.status;


/* ------------------------------------------------------
   2) 가게 데이터
   - shop_id 1: 마포구 케이크샵 (owner: user 2) - 메인 테스트용
   - shop_id 2: 강남구 케이크샵 (owner: user 3)
   - shop_id 3: 랭킹 테스트용 매장 (owner: user 2)
   ------------------------------------------------------ */
INSERT INTO shops (
    shop_id, owner_id, shop_name, phone, address, district, location,
    average_rating, min_price, max_price,
    status, verification_status, description,
    pickup_time_guide, day_order_guide, parking_guide,
    payment_notice, precaution_notice, prepayment,
    chat_nickname, created_at
) VALUES
      -- shop 1: 마포구 (홍대입구) - 주 테스트 가게
      (1, 2, '마포 스윗 케이크', '02-1234-5678', '서울 마포구 양화로 160', '마포구',
       ST_GeomFromText('POINT(126.9241 37.5565)', 4326),
       4.5, 30000, 80000,
       'ACTIVE', 'VERIFIED', '홍대 근처 감성 케이크샵입니다.',
       '주문 후 최소 3일 소요', '재고 케이크 한정 당일 예약 가능', '매장 앞 공영주차장 이용 가능',
       '온라인 결제 시 주문 확정 후 3일 이내 취소 가능',
       '주문 후 변경은 픽업 2일 전까지 가능합니다.\n생과일 케이크는 당일 소비를 권장합니다.\n알러지가 있으신 경우 주문 시 꼭 알려주세요.',
       20000,
       '마포 스윗 케이크', NOW(6)),
      -- shop 2: 강남구 (강남역)
      (2, 3, '강남 스윗 케이크', '02-555-5555', '서울 강남구 강남대로 396', '강남구',
       ST_GeomFromText('POINT(127.0276 37.4979)', 4326),
       4.8, 45000, 90000,
       'ACTIVE', 'VERIFIED', '강남역 최고의 케이크 맛집',
       '주문 후 최소 2일 소요', NULL, '인근 유료 주차장 이용',
       '카드 결제만 가능합니다.',
       '픽업 당일 환불 불가',
       30000,
       '강남 스윗 케이크', NOW(6)),
      -- shop 3: 랭킹 테스트용 매장
      (3, 2, '랭킹 테스트용 케이크샵', '010-5555-5555', '서울시 성수동', '성동구',
       ST_GeomFromText('POINT(127.0566 37.5445)', 4326),
       4.8, 15000, 50000,
       'ACTIVE', 'VERIFIED', '인기 랭킹 테스트를 위한 매장입니다.',
       NULL, NULL, NULL, NULL, NULL, NULL,
       '랭킹샵', NOW(6))
    AS new
ON DUPLICATE KEY UPDATE
                     shop_name = new.shop_name,
                     phone = new.phone,
                     address = new.address,
                     district = new.district,
                     location = new.location,
                     description = new.description,
                     average_rating = new.average_rating,
                     min_price = new.min_price,
                     max_price = new.max_price,
                     pickup_time_guide = new.pickup_time_guide,
                     day_order_guide = new.day_order_guide,
                     parking_guide = new.parking_guide,
                     payment_notice = new.payment_notice,
                     precaution_notice = new.precaution_notice,
                     prepayment = new.prepayment,
                     chat_nickname = new.chat_nickname,
                     status = new.status,
                     verification_status = new.verification_status;


/* ------------------------------------------------------
   3) 케이크 사이즈
   - size_id 1~3: shop 1 사이즈
   - size_id 4~6: shop 2 사이즈
   - size_id 7: shop 3 사이즈 (랭킹 테스트용)
   ------------------------------------------------------ */
INSERT INTO shop_cake_sizes (size_id, shop_id, size_name, diameter, price)
VALUES
    -- shop 1
    (1, 1, '도시락', '10cm', 20000),
    (2, 1, '1호',   '15cm', 30000),
    (3, 1, '2호',   '18cm', 40000),
    -- shop 2
    (4, 2, '도시락', '10cm', 25000),
    (5, 2, '1호',   '15cm', 45000),
    (6, 2, '2호',   '18cm', 60000),
    -- shop 3 (랭킹 테스트용)
    (7, 3, '1호',   '15cm', 30000)
    AS new
ON DUPLICATE KEY UPDATE
                     size_name = new.size_name,
                     diameter = new.diameter,
                     price = new.price;


/* ------------------------------------------------------
   4) 가게 운영 시간
   - shop 1: 월 휴무, 화~금 10:00~20:00, 토~일 12:00~22:00
   - shop 2: 매일 10:00~22:00
   - shop 3: 매일 10:00~22:00
   ------------------------------------------------------ */
DELETE FROM shop_business_hours WHERE shop_id IN (1, 2, 3);

INSERT INTO shop_business_hours (shop_id, schedule_type, day_of_week, open_time, close_time, is_closed)
VALUES
    -- shop 1
    (1, 'WEEKLY', 1, '00:00:00', '00:00:00', true),   -- 월 휴무
    (1, 'WEEKLY', 2, '10:00:00', '20:00:00', false),   -- 화
    (1, 'WEEKLY', 3, '10:00:00', '20:00:00', false),   -- 수
    (1, 'WEEKLY', 4, '10:00:00', '20:00:00', false),   -- 목
    (1, 'WEEKLY', 5, '10:00:00', '20:00:00', false),   -- 금
    (1, 'WEEKLY', 6, '12:00:00', '22:00:00', false),   -- 토
    (1, 'WEEKLY', 7, '12:00:00', '22:00:00', false),   -- 일
    -- shop 2
    (2, 'WEEKLY', 1, '10:00:00', '22:00:00', false),
    (2, 'WEEKLY', 2, '10:00:00', '22:00:00', false),
    (2, 'WEEKLY', 3, '10:00:00', '22:00:00', false),
    (2, 'WEEKLY', 4, '10:00:00', '22:00:00', false),
    (2, 'WEEKLY', 5, '10:00:00', '22:00:00', false),
    (2, 'WEEKLY', 6, '10:00:00', '22:00:00', false),
    (2, 'WEEKLY', 7, '10:00:00', '22:00:00', false),
    -- shop 3
    (3, 'WEEKLY', 1, '10:00:00', '22:00:00', false),
    (3, 'WEEKLY', 2, '10:00:00', '22:00:00', false),
    (3, 'WEEKLY', 3, '10:00:00', '22:00:00', false),
    (3, 'WEEKLY', 4, '10:00:00', '22:00:00', false),
    (3, 'WEEKLY', 5, '10:00:00', '22:00:00', false),
    (3, 'WEEKLY', 6, '10:00:00', '22:00:00', false),
    (3, 'WEEKLY', 7, '10:00:00', '22:00:00', false);


/* ------------------------------------------------------
   5) 계좌 정보 (shop 1, 2)
   ------------------------------------------------------ */
DELETE FROM bank_accounts WHERE shop_id IN (1, 2);

INSERT INTO bank_accounts (shop_id, bank_name, account_number, account_holder, is_primary)
VALUES
    (1, '국민은행', '123-456-7890', '마포사장님', true),
    (2, '신한은행', '987-654-3210', '강남사장님', true);


/* ------------------------------------------------------
   6) 진행 중인 이벤트 (shop 1)
   ------------------------------------------------------ */
DELETE FROM shop_events WHERE shop_id IN (1, 2);

INSERT INTO shop_events (shop_id, title, content, start_date, end_date, is_active)
VALUES
    (1, '봄맞이 특별 할인', '레터링 케이크 주문 시 10% 할인해드립니다!',
     CURRENT_DATE, DATE_ADD(CURRENT_DATE, INTERVAL 30 DAY), true),
    (1, '종료된 이벤트', '이 이벤트는 보이면 안 됩니다.',
     DATE_SUB(CURRENT_DATE, INTERVAL 20 DAY), DATE_SUB(CURRENT_DATE, INTERVAL 10 DAY), true),
    (2, '강남 오픈 기념', '첫 주문 5,000원 할인!',
     CURRENT_DATE, DATE_ADD(CURRENT_DATE, INTERVAL 14 DAY), true);

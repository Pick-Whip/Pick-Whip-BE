/* ------------------------------------------------------
   가게 데이터 (서울시청 인근 추가) - 뷰포트 내 조회용
   ------------------------------------------------------ */
INSERT INTO shops (
    shop_id, owner_id, shop_name, phone, address, district, location,
    average_rating, min_price, max_price,
    status, verification_status, description,
    pickup_time_guide, day_order_guide, parking_guide,
    payment_notice, precaution_notice, prepayment,
    chat_nickname, created_at
) VALUES
      (11, 2, '레망도레 광화문점', '02-777-1234', '서울 중구 무교로 17 무교빌딩 1-3층', '중구',
       ST_GeomFromText('POINT(37.5679 126.9787)', 4326),
       4.7, 35000, 75000,
       'ACTIVE', 'VERIFIED', '광화문 직장인들이 사랑하는 디저트 카페',
       2, 1, '무교빌딩 주차장 이용',
       '현장 결제 가능',
       '단체 주문은 3일 전 예약 필수',
       15000,
       '레망도레', NOW(6)),

      (12, 2, '카라멜솔티드', '02-777-5678', '서울 중구 을지로3길 19 1층', '중구',
       ST_GeomFromText('POINT(37.5667 126.9805)', 4326),
       4.9, 28000, 60000,
       'ACTIVE', 'VERIFIED', '단짠단짠 솔티드 카라멜 케이크 전문점',
       1, 1, '주차 공간 협소',
       '예약금 100% 입금 시 확정',
       '당일 취소 불가',
       28000,
       '카라멜솔티드', NOW(6)),

      (13, 2, '곤트란쉐리에', '02-777-9012', '서울 중구 세종대로22길 16 1층', '중구',
       ST_GeomFromText('POINT(37.5664 126.9775)', 4326),
       4.6, 40000, 90000,
       'ACTIVE', 'VERIFIED', '프랑스 정통 베이커리와 케이크',
       3, 0, '인근 공영 주차장 이용',
       '카드 결제 환영',
       '픽업 시간 엄수 부탁드립니다.',
       20000,
       '곤트란쉐리에', NOW(6)),

      (14, 2, '더플라자호텔 블랑제리', '02-777-3456', '서울 중구 소공로 119 LL층', '중구',
       ST_GeomFromText('POINT(37.5649 126.9784)', 4326),
       4.9, 60000, 150000,
       'ACTIVE', 'VERIFIED', '호텔 셰프가 만드는 프리미엄 케이크',
       5, 0, '호텔 주차장 2시간 무료',
       '호텔 멤버십 적립 가능',
       '특별 주문 제작은 1주일 전 문의',
       50000,
       '블랑제리', NOW(6))
    AS new
ON DUPLICATE KEY UPDATE
                     shop_name = new.shop_name,
                     phone = new.phone,
                     address = new.address,
                     district = new.district,
                     location = new.location,
                     description = new.description,
                     pickup_time_guide = new.pickup_time_guide,
                     day_order_guide = new.day_order_guide;


DELETE FROM favorite_shops WHERE user_id = 1 AND shop_id IN (11, 14);

INSERT INTO favorite_shops (user_id, shop_id, created_at)
VALUES
    (1, 11, NOW(6)), -- 레망도레 찜
    (1, 14, NOW(6))  -- 더플라자 찜
    AS new
ON DUPLICATE KEY UPDATE
                     created_at = new.created_at;
/* ======================================================
   1. 기존 design_options 좌표값 보정 (0 -> NULL)
====================================================== */
UPDATE design_options SET position_x = NULL WHERE position_x = 0 OR position_x = 0.0;
UPDATE design_options SET position_y = NULL WHERE position_y = 0 OR position_y = 0.0;

/* ======================================================
   2. 기존 테스트 데이터 초기화 (충돌 방지용)
   - 자식 테이블부터 먼저 삭제해야 외래 키 에러가 발생하지 않습니다.
====================================================== */
-- 자식 1: 키워드 및 매핑 테이블 삭제
DELETE FROM design_gallery_keywords WHERE design_gallery_id IN (11, 12, 13, 14);
DELETE FROM design_options WHERE design_id IN (11, 12, 13, 14);
DELETE FROM avail_option WHERE design_id IN (11, 12, 13, 14);

-- 자식 2: 부모 테이블 삭제
DELETE FROM design_gallery WHERE id IN (11, 12, 13, 14);
DELETE FROM custom_options WHERE id BETWEEN 101 AND 140;
DELETE FROM shop_cake_sizes WHERE size_id BETWEEN 11 AND 18;

/* ======================================================
   3. 부모 가게(shops) 데이터 생성
====================================================== */
INSERT INTO shops (
    shop_id, owner_id, shop_name, phone, address, district, location,
    average_rating, min_price, max_price, status, verification_status, description,
    pickup_time_guide, day_order_guide, parking_guide,
    payment_notice, precaution_notice, prepayment, chat_nickname, shop_image_url,
    slot_interval_minutes, max_orders_per_slot, created_at
) VALUES
      (11, 2, '레망도레 광화문점', '02-777-1234', '서울 중구 무교로 17 무교빌딩 1-3층', '중구', ST_GeomFromText('POINT(37.5679 126.9787)', 4326), 4.7, 35000, 75000, 'ACTIVE', 'VERIFIED', '광화문 디저트', 2, 1, '무교빌딩 주차장', '카드 계좌이체 네이버페이 카카오페이 토스페이', '3일 전 예약 필수', 15000, '레망도레', '1%E1%84%87%E1%85%A5%E1%86%AB%E1%84%8F%E1%85%A6%E1%84%8B%E1%85%B5%E1%84%8F%E1%85%B3%E1%84%89%E1%85%A3%E1%86%B8.webp', 30, 2, NOW(6)),
      (12, 2, '카라멜솔티드', '02-777-5678', '서울 중구 을지로3길 19 1층', '중구', ST_GeomFromText('POINT(37.5667 126.9805)', 4326), 4.9, 28000, 60000, 'ACTIVE', 'VERIFIED', '솔티드 카라멜', 1, 1, '주차 협소', '예약금 100%', '당일 취소 불가', 28000, '카라멜솔티드', '2%E1%84%87%E1%85%A5%E1%86%AB%E1%84%8F%E1%85%A6%E1%84%8B%E1%85%B5%E1%84%8F%E1%85%B3%E1%84%89%E1%85%A3%E1%86%B8.webp', 30, 2, NOW(6)),
      (13, 2, '곤트란쉐리에', '02-777-9012', '서울 중구 세종대로22길 16 1층', '중구', ST_GeomFromText('POINT(37.5664 126.9775)', 4326), 4.6, 40000, 90000, 'ACTIVE', 'VERIFIED', '프랑스 정통 베이커리', 3, 0, '공영 주차장', '카드 환영', '시간 엄수', 20000, '곤트란쉐리에', '3%E1%84%87%E1%85%A5%E1%86%AB%E1%84%8F%E1%85%A6%E1%84%8B%E1%85%B5%E1%84%8F%E1%85%B3%E1%84%89%E1%85%A3%E1%86%B8.webp', 30, 2, NOW(6)),
      (14, 2, '더플라자호텔 블랑제리', '02-777-3456', '서울 중구 소공로 119 LL층', '중구', ST_GeomFromText('POINT(37.5649 126.9784)', 4326), 4.9, 60000, 150000, 'ACTIVE', 'VERIFIED', '프리미엄 케이크', 5, 0, '호텔 2시간 무료', '멤버십 적립', '1주일 전 문의', 50000, '블랑제리', '4%E1%84%87%E1%85%A5%E1%86%AB+%E1%84%8F%E1%85%A6%E1%84%8B%E1%85%B5%E1%84%8F%E1%85%B3%E1%84%89%E1%85%A3%E1%86%B8.webp', 30, 2, NOW(6))
    ON DUPLICATE KEY UPDATE shop_name = VALUES(shop_name);


/* ======================================================
   4. 가게별 케이크 사이즈 추가
====================================================== */
INSERT INTO shop_cake_sizes (size_id, shop_id, size_name, diameter, price) VALUES
                                                                               (11, 11, '도시락', '10cm', 20000), (12, 11, '1호', '15cm', 35000),
                                                                               (13, 12, '도시락', '10cm', 22000), (14, 12, '1호', '15cm', 38000),
                                                                               (15, 13, '1호', '15cm', 40000), (16, 13, '2호', '18cm', 55000),
                                                                               (17, 14, '1호', '15cm', 60000), (18, 14, '2호', '18cm', 80000);

/* ======================================================
   5. 커스텀 마스터 옵션 (custom_options)
   - 카테고리별로 여러 개 생성 (RGB 코드 완벽 포함)
====================================================== */
INSERT INTO custom_options (id, shop_id, option_name, category, additional_price, color_rgb_code) VALUES
                                                                                                      -- Shop 11 (레망도레)
                                                                                                      (101, 11, '바닐라 시트', 'SHEET', 0, '#FFF8DC'),
                                                                                                      (102, 11, '초코 시트', 'SHEET', 1000, '#8B4513'),
                                                                                                      (103, 11, '우유 생크림', 'CREAM', 0, '#FFFFFF'),
                                                                                                      (104, 11, '딸기 생크림', 'CREAM', 1500, '#FFC0CB'),
                                                                                                      (105, 11, '화이트 아이싱', 'ICING', 0, '#FFFFFF'),
                                                                                                      (106, 11, '핑크 아이싱', 'ICING', 2000, '#FFB6C1'),
                                                                                                      (107, 11, '원형 쉐입', 'SHAPE', 0, '#FFFFFF'),
                                                                                                      (108, 11, '하트 쉐입', 'SHAPE', 2000, '#FF69B4'),
                                                                                                      (109, 11, '생딸기', 'TOPPING', 3000, NULL),
                                                                                                      (110, 11, '마카롱 꼬끄', 'TOPPING', 4500, NULL),

                                                                                                      (198, 11, '원형', 'SHAPE',15000 ,null),
                                                                                                      (199, 11, '네모', 'SHAPE',15000 ,null),
                                                                                                      (200, 11, '하트', 'SHAPE',15000 ,null),

                                                                                                      (201, 11, '레드벨벳', 'SHEET',15000,'#8B0000'),
                                                                                                      (202, 11, '바닐라', 'SHEET',15000 ,'#F4C998'),
                                                                                                      (203, 11, '딸기', 'SHEET',15000 ,'#FCE2E9'),
                                                                                                      (204, 11, '초콜릿', 'SHEET',15000 ,'#A15004'),

                                                                                                      (205, 11, '생크림', 'CREAM',15000 ,'#EAF9FF'),
                                                                                                      (206, 11, '크림치즈', 'CREAM',15000 ,'#FAFEDA'),
                                                                                                      (207, 11, '초코크림', 'CREAM',15000,'#A15004'),
                                                                                                      (208, 11, '딸기크림', 'CREAM',15000 ,'#FCE2E9'),



                                                                                                      (209, 11, '1번', 'ICING',0 ,'#FFFFFF'),
                                                                                                      (210, 11, '2번', 'ICING',0 ,'#F4D3D3'),
                                                                                                      (211, 11, '3번', 'ICING',0 ,'#FDF4EB'),
                                                                                                      (212, 11, '4번', 'ICING',0 ,'#CE1630'),
                                                                                                      (213, 11, '5번', 'ICING',0 ,'#000000'),
                                                                                                      (214, 11, '6번', 'ICING',0 ,'#D2EFFB'),


                                                                                                      (215, 11, '딸기', 'TOPPING',3000,null),
                                                                                                      (216, 11, '블루베리', 'TOPPING',3000,null),
                                                                                                      (217, 11, '오레오', 'TOPPING',3500,null),
                                                                                                      (218, 11, '스프링클', 'TOPPING',2500,null),
                                                                                                      (219, 11, '생화', 'TOPPING',5000,null),
                                                                                                      (220, 11, '마카롱', 'TOPPING',4500,null),

                                                                                                      -- Shop 12 (카라멜솔티드)
                                                                                                      (111, 12, '레드벨벳 시트', 'SHEET', 2000, '#8B0000'),
                                                                                                      (112, 12, '모카 시트', 'SHEET', 1500, '#A0522D'),
                                                                                                      (113, 12, '크림치즈', 'CREAM', 1000, '#FFFAF0'),
                                                                                                      (114, 12, '카라멜 크림', 'CREAM', 2500, '#D2691E'),
                                                                                                      (115, 12, '아이보리 아이싱', 'ICING', 0, '#FFFFF0'),
                                                                                                      (116, 12, '민트 아이싱', 'ICING', 2000, '#98FF98'),
                                                                                                      (117, 12, '원형 쉐입', 'SHAPE', 0, '#FFFFFF'),
                                                                                                      (118, 12, '사각 쉐입', 'SHAPE', 3000, '#FFFFFF'),
                                                                                                      (119, 12, '오레오 쿠키', 'TOPPING', 2000, NULL),
                                                                                                      (120, 12, '로투스 부스러기', 'TOPPING', 1500, NULL),

                                                                                                      -- Shop 13 (곤트란쉐리에)
                                                                                                      (121, 13, '얼그레이 시트', 'SHEET', 3000, '#D3B8AE'),
                                                                                                      (122, 13, '플레인 시트', 'SHEET', 0, '#F5DEB3'),
                                                                                                      (123, 13, '가나슈 크림', 'CREAM', 3500, '#3E2723'),
                                                                                                      (124, 13, '요거트 크림', 'CREAM', 2000, '#F0FFF0'),
                                                                                                      (125, 13, '블루 아이싱', 'ICING', 2000, '#87CEEB'),
                                                                                                      (126, 13, '블랙 아이싱', 'ICING', 3000, '#000000'),
                                                                                                      (127, 13, '원형 쉐입', 'SHAPE', 0, '#FFFFFF'),
                                                                                                      (128, 13, '돔 쉐입', 'SHAPE', 4000, '#FFFFFF'),
                                                                                                      (129, 13, '생블루베리', 'TOPPING', 4000, NULL),
                                                                                                      (130, 13, '초코 진주', 'TOPPING', 2500, NULL),

                                                                                                      -- Shop 14 (더플라자호텔)
                                                                                                      (131, 14, '프리미엄 바닐라', 'SHEET', 5000, '#FFFACD'),
                                                                                                      (132, 14, '블랙 카카오', 'SHEET', 5000, '#1A1A1A'),
                                                                                                      (133, 14, '마스카포네 크림', 'CREAM', 4000, '#FFFFF0'),
                                                                                                      (134, 14, '트러플 크림', 'CREAM', 8000, '#F5F5DC'),
                                                                                                      (135, 14, '다크초코 아이싱', 'ICING', 3000, '#2F4F4F'),
                                                                                                      (136, 14, '골드 아이싱', 'ICING', 7000, '#FFD700'),
                                                                                                      (137, 14, '원형 쉐입', 'SHAPE', 0, '#FFFFFF'),
                                                                                                      (138, 14, '2단 쉐입', 'SHAPE', 20000, '#FFFFFF'),
                                                                                                      (139, 14, '왕관 장식', 'TOPPING', 15000, NULL),
                                                                                                      (140, 14, '식용 생화', 'TOPPING', 12000, NULL);

/* ======================================================
   6. 디자인 갤러리 추가 (design_gallery)
====================================================== */
INSERT INTO design_gallery (
    id, shop_id, shop_cake_size_id, design_name, base_price, description,
    allergy_info, image_url, lettering_text, lettering_line_count,
    lettering_alignment, lettering_color
) VALUES
      (11, 11, 12, '광화문 핑크 하트', 45000, '사랑스러운 핑크색 하트 케이크', '우유, 밀', 'd1.png', '수고했어', 'ONE_LINE', 'CENTER', '#000000'),
      (12, 12, 14, '단짠 카라멜 폭탄', 48000, '솔티드 카라멜', '우유, 밀', 'd2.png', 'HBD', 'TWO_LINE', 'CENTER', '#FFFFFF'),
      (13, 13, 15, '얼그레이 갸또', 55000, '진한 얼그레이', '우유, 밀', 'd3.png', '사랑해', 'ONE_LINE', 'CURVE_UP', '#FF0000'),
      (14, 14, 17, '플라자 블랙', 95000, '프리미엄 블랙 케이크', '우유, 견과류', 'd4.png', 'Congrats', 'ONE_LINE', 'CURVE_UP_DOWN', '#FFD700');

/* ======================================================
   7. 갤러리 디자인에 적용된 찐 옵션 (design_options)
   - 조건: 카테고리당 무조건 1개만! (SHEET 1, CREAM 1, ICING 1, SHAPE 1)
   - TOPPING에만 X, Y 좌표 입력! (나머지는 NULL)
====================================================== */
INSERT INTO design_options (design_id, custom_option_id, position_x, position_y) VALUES
                                                                                     -- Design 11: 바닐라시트 + 딸기크림 + 핑크아이싱 + 하트쉐입 + 생딸기
                                                                                     (11, 101, NULL, NULL), (11, 104, NULL, NULL), (11, 106, NULL, NULL), (11, 108, NULL, NULL), (11, 109, 50.0, 50.0),

                                                                                     -- Design 12: 레드벨벳 + 카라멜크림 + 민트아이싱 + 원형쉐입 + 로투스
                                                                                     (12, 111, NULL, NULL), (12, 114, NULL, NULL), (12, 116, NULL, NULL), (12, 117, NULL, NULL), (12, 120, 40.0, 60.0),

                                                                                     -- Design 13: 얼그레이 + 가나슈 + 블루아이싱 + 돔쉐입 + 생블루베리
                                                                                     (13, 121, NULL, NULL), (13, 123, NULL, NULL), (13, 125, NULL, NULL), (13, 128, NULL, NULL), (13, 129, 30.0, 70.0),

                                                                                     -- Design 14: 블랙카카오 + 마스카포네 + 다크초코아이싱 + 2단쉐입 + 왕관
                                                                                     (14, 132, NULL, NULL), (14, 133, NULL, NULL), (14, 135, NULL, NULL), (14, 138, NULL, NULL), (14, 139, 50.0, 15.0);

/* ======================================================
   8. 선택 가능한 옵션 목록 (design_avail_options)
   - 조건: 카테고리별로 여러 개 제공
====================================================== */
INSERT INTO avail_option (design_id, custom_option_id) VALUES
                                                                   -- Design 11에서 유저가 고를 수 있는 옵션들 (시트 2종, 크림 2종, 아이싱 2종, 쉐입 2종, 토핑 2종 전부)
                                                                   (11, 101), (11, 102), (11, 103), (11, 104), (11, 105), (11, 106), (11, 107), (11, 108), (11, 109), (11, 110),
                                                                   -- Design 12
                                                                   (12, 111), (12, 112), (12, 113), (12, 114), (12, 115), (12, 116), (12, 117), (12, 118), (12, 119), (12, 120),
                                                                   -- Design 13
                                                                   (13, 121), (13, 122), (13, 123), (13, 124), (13, 125), (13, 126), (13, 127), (13, 128), (13, 129), (13, 130),
                                                                   -- Design 14
                                                                   (14, 131), (14, 132), (14, 133), (14, 134), (14, 135), (14, 136), (14, 137), (14, 138), (14, 139), (14, 140);

/* ======================================================
   9. 키워드 매핑 (design_gallery_keywords)
   - 필터링에 사용할 키워드(스타일/용도) 추가
====================================================== */
INSERT INTO design_gallery_keywords (design_gallery_id, keyword) VALUES
                                                                      (11, 'GORGEOUS'), (11, 'MODERN'),
                                                                      (12, 'LUXURY'), (12, 'MODERN'),
                                                                      (13, 'LUXURY'), (13, 'GORGEOUS'),
                                                                      (14, 'RED_VELVET'), (14, 'LOVELY');
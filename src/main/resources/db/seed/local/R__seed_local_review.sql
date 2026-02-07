-- ======================================================
-- Local Review Seed Data (Repeatable)
-- 가게 리뷰 목록 조회 테스트용
-- shopId=1, userId=1 기준
-- ======================================================

-- ------------------------------------------------------
-- 0) 테스트용 추가 유저 (좋아요 데이터용)
-- ------------------------------------------------------
INSERT INTO users (
    user_id, created_at, updated_at, deleted_at,
    email, kakao_id, name, nickname, birthdate, phone, profile_image_url, status
) VALUES
(3, NOW(6), NOW(6), NULL, 'local.tester2@picknwhip.com', 2000000002, '테스터2', '딸기케이크', NULL, '010-2222-2222', NULL, 'ACTIVE'),
(4, NOW(6), NOW(6), NULL, 'local.tester3@picknwhip.com', 2000000003, '테스터3', '바닐라케이크', NULL, '010-3333-3333', NULL, 'ACTIVE'),
(5, NOW(6), NOW(6), NULL, 'local.tester4@picknwhip.com', 2000000004, '테스터4', '당근케이크', NULL, '010-4444-4444', NULL, 'ACTIVE')
AS new
ON DUPLICATE KEY UPDATE updated_at = new.updated_at, status = new.status;

-- ------------------------------------------------------
-- 1) 추가 주문 데이터 (order id 2~8)
--    design_id: 1~4 갤러리 디자인 + NULL(커스텀)
-- ------------------------------------------------------
INSERT INTO orders (
    id, user_id, shop_id, shop_cake_size_id, design_id,
    status, pickup_datetime,
    lettering_text, lettering_line_count, lettering_alignment,
    additional_request, order_additional_request,
    reference_image_url, payment_method, payment_status,
    total_price, deposit_amount, rejection_reason,
    created_at, updated_at,
    order_code, customer_name, customer_phone
) VALUES
-- 주문 2: 디자인 2 (미니멀 레터링)
(2, 1, 1, 1, 2,
 'COMPLETED', '2026-01-10 14:00:00',
 '사랑해', 'ONE_LINE', 'CENTER',
 NULL, NULL,
 NULL, 'CARD', 'PAID',
 35000, 10000, NULL,
 '2026-01-08 10:00:00', '2026-01-08 10:00:00',
 '260110_001', '김픽휩', '010-1234-5678'),

-- 주문 3: 디자인 3 (아이돌 포토 하트)
(3, 1, 1, 1, 3,
 'COMPLETED', '2026-01-15 16:00:00',
 NULL, NULL, NULL,
 '포토카드 2장 넣어주세요', NULL,
 NULL, 'CARD', 'PAID',
 42000, 15000, NULL,
 '2026-01-13 09:00:00', '2026-01-13 09:00:00',
 '260115_001', '김픽휩', '010-1234-5678'),

-- 주문 4: 디자인 4 (빈티지 생화)
(4, 1, 1, 1, 4,
 'COMPLETED', '2026-01-18 13:00:00',
 NULL, NULL, NULL,
 '장미 위주로 올려주세요', NULL,
 NULL, 'CARD', 'PAID',
 45000, 15000, NULL,
 '2026-01-16 11:00:00', '2026-01-16 11:00:00',
 '260118_001', '김픽휩', '010-1234-5678'),

-- 주문 5: 디자인 1 (심플 플라워) 재주문
(5, 1, 1, 1, 1,
 'COMPLETED', '2026-01-22 15:00:00',
 '축하해', 'ONE_LINE', 'CENTER',
 '이번엔 핑크 장미로 해주세요', NULL,
 NULL, 'CARD', 'PAID',
 35000, 10000, NULL,
 '2026-01-20 14:00:00', '2026-01-20 14:00:00',
 '260122_001', '김픽휩', '010-1234-5678'),

-- 주문 6: 커스텀 디자인 (design_id = NULL)
(6, 1, 1, 1, NULL,
 'COMPLETED', '2026-01-25 11:00:00',
 '우리 1주년', 'ONE_LINE', 'CENTER',
 '하트 모양 케이크에 핑크 아이싱 부탁드려요', NULL,
 'https://example.com/reference/custom1.jpg', 'CARD', 'PAID',
 50000, 20000, NULL,
 '2026-01-23 08:00:00', '2026-01-23 08:00:00',
 '260125_001', '김픽휩', '010-1234-5678'),

-- 주문 7: 커스텀 디자인 (design_id = NULL)
(7, 1, 1, 1, NULL,
 'COMPLETED', '2026-01-28 17:00:00',
 NULL, NULL, NULL,
 '강아지 얼굴 그려주세요', NULL,
 'https://example.com/reference/custom2.jpg', 'CARD', 'PAID',
 55000, 20000, NULL,
 '2026-01-26 16:00:00', '2026-01-26 16:00:00',
 '260128_001', '김픽휩', '010-1234-5678'),

-- 주문 8: 디자인 2 (미니멀 레터링) 재주문
(8, 1, 1, 1, 2,
 'COMPLETED', '2026-02-01 12:00:00',
 '졸업 축하해', 'ONE_LINE', 'CENTER',
 NULL, NULL,
 NULL, 'CARD', 'PAID',
 35000, 10000, NULL,
 '2026-01-30 10:00:00', '2026-01-30 10:00:00',
 '260201_001', '김픽휩', '010-1234-5678')

AS new
ON DUPLICATE KEY UPDATE
    user_id = new.user_id,
    shop_id = new.shop_id,
    design_id = new.design_id,
    status = new.status,
    updated_at = NOW();


-- ------------------------------------------------------
-- 2) 리뷰 데이터 (review id 1~8)
--    다양한 별점, 날짜 (좋아요 수는 review_like로 결정)
-- ------------------------------------------------------
INSERT INTO review (
    id, order_id, user_id, shop_id, design_id,
    rating, content, agreement,
    deleted_at, created_at, updated_at
) VALUES
-- 리뷰 1: 주문1, 디자인1, 별점5
(1, 1, 1, 1, 1,
 5, '심플 플라워 디자인 너무 예뻤어요! 생일파티에서 모두가 감탄했습니다. 딸기도 많이 올려주셔서 맛도 좋았어요.',
 true,
 NULL, '2026-01-05 10:00:00', '2026-01-05 10:00:00'),

-- 리뷰 2: 주문2, 디자인2, 별점4
(2, 2, 1, 1, 2,
 4, '미니멀 레터링이 깔끔하고 좋았어요. 다만 크림이 조금 달았으면 더 좋았을 것 같아요. 전체적으로 만족합니다!',
 true,
 NULL, '2026-01-12 09:00:00', '2026-01-12 09:00:00'),

-- 리뷰 3: 주문3, 디자인3, 별점5
(3, 3, 1, 1, 3,
 5, '아이돌 포토 케이크 퀄리티가 정말 높아요! 포토카드도 예쁘게 넣어주셨고 하트 모양도 완벽했습니다. 최애 생일에 딱이에요!',
 true,
 NULL, '2026-01-17 14:00:00', '2026-01-17 14:00:00'),

-- 리뷰 4: 주문4, 디자인4, 별점3
(4, 4, 1, 1, 4,
 3, '빈티지 분위기는 좋았는데 생화가 생각보다 빨리 시들었어요. 케이크 맛 자체는 괜찮았습니다. 다음엔 조화로 해달라고 할게요.',
 true,
 NULL, '2026-01-20 11:00:00', '2026-01-20 11:00:00'),

-- 리뷰 5: 주문5, 디자인1, 별점5
(5, 5, 1, 1, 1,
 5, '재주문했는데 역시 실망시키지 않네요! 핑크 장미로 바꿔주셨는데 더 예뻐요. 사장님이 항상 친절하세요.',
 true,
 NULL, '2026-01-24 16:00:00', '2026-01-24 16:00:00'),

-- 리뷰 6: 주문6, 커스텀(NULL), 별점4
(6, 6, 1, 1, NULL,
 4, '커스텀 주문이라 걱정했는데 레퍼런스 사진보다 더 예쁘게 만들어주셨어요! 핑크 아이싱 색감이 정말 좋았습니다.',
 true,
 NULL, '2026-01-27 13:00:00', '2026-01-27 13:00:00'),

-- 리뷰 7: 주문7, 커스텀(NULL), 별점2
(7, 7, 1, 1, NULL,
 2, '강아지 얼굴이 레퍼런스와 좀 달랐어요. 맛은 괜찮았지만 디자인이 기대에 못 미쳤습니다. 조금 아쉬워요.',
 true,
 NULL, '2026-01-30 18:00:00', '2026-01-30 18:00:00'),

-- 리뷰 8: 주문8, 디자인2, 별점5
(8, 8, 1, 1, 2,
 5, '졸업 선물로 주문했는데 레터링이 너무 예쁘게 나왔어요! 받는 사람이 정말 좋아했습니다. 미니멀한 디자인 최고!',
 true,
 NULL, '2026-02-02 09:00:00', '2026-02-02 09:00:00')

AS new
ON DUPLICATE KEY UPDATE
    rating = new.rating,
    content = new.content,
    updated_at = NOW();


-- ------------------------------------------------------
-- 3) 리뷰 키워드 매핑
--    review_keyword id는 V5 INSERT 순서 기준:
--    1=MATCH_REQUEST, 2=PRETTY, 3=CREATIVE, 4=SPECIAL, 5=LUXURIOUS
--    6=SAME_AS_PHOTO, 7=BEYOND_EXPECTATION, 8=ACCURATE, 9=BETTER
--    10=DELICIOUS, 11=FRESH, 12=SWEET, 13=MOIST, 14=NOT_TOO_SWEET
--    15=KIND, 16=DETAILED, 17=FAST_RESPONSE, 18=GOOD_LISTENER, 19=EASY
--    20=EASY_PICKUP, 21=CLEAN, 22=ON_TIME
-- ------------------------------------------------------
DELETE FROM review_selected_keyword WHERE review_id IN (1,2,3,4,5,6,7,8);

INSERT INTO review_selected_keyword (review_id, keyword_id) VALUES
-- 리뷰 1: 예뻐요, 맛있어요, 요청사항 잘 들어줘요
(1, 2), (1, 10), (1, 18),

-- 리뷰 2: 디자인 예뻐요, 친절해요
(2, 2), (2, 15),

-- 리뷰 3: 원하는 디자인 잘해줬어요, 사진과 똑같아요, 기대 이상, 친절해요, 픽업 편해요
(3, 1), (3, 6), (3, 7), (3, 15), (3, 20),

-- 리뷰 4: 맛있어요
(4, 10),

-- 리뷰 5: 예뻐요, 친절해요, 요청사항 잘 들어줘요, 시간 잘 지켜요
(5, 2), (5, 15), (5, 18), (5, 22),

-- 리뷰 6: 기대 이상이에요, 창의적이에요, 소통이 편해요
(6, 7), (6, 3), (6, 19),

-- 리뷰 7: (키워드 없음 - 낮은 별점)

-- 리뷰 8: 예뻐요, 정확해요, 매장 깨끗해요
(8, 2), (8, 8), (8, 21);


-- ------------------------------------------------------
-- 4) 리뷰 좋아요 (review_like)
--    좋아요 수로 도움순(HELPFUL) 정렬 테스트
--    리뷰3: 4개, 리뷰5: 3개, 리뷰1: 2개, 리뷰2: 1개, 리뷰6: 1개, 리뷰8: 1개
--    리뷰4: 0개, 리뷰7: 0개
-- ------------------------------------------------------
DELETE FROM review_like WHERE review_id IN (1,2,3,4,5,6,7,8);

INSERT INTO review_like (review_id, user_id, created_at, updated_at) VALUES
-- 리뷰 3: 좋아요 4개 (userId 1,3,4,5)
(3, 1, NOW(6), NOW(6)),
(3, 3, NOW(6), NOW(6)),
(3, 4, NOW(6), NOW(6)),
(3, 5, NOW(6), NOW(6)),

-- 리뷰 5: 좋아요 3개 (userId 1,3,4)
(5, 1, NOW(6), NOW(6)),
(5, 3, NOW(6), NOW(6)),
(5, 4, NOW(6), NOW(6)),

-- 리뷰 1: 좋아요 2개 (userId 3,4)
(1, 3, NOW(6), NOW(6)),
(1, 4, NOW(6), NOW(6)),

-- 리뷰 2: 좋아요 1개 (userId 3)
(2, 3, NOW(6), NOW(6)),

-- 리뷰 6: 좋아요 1개 (userId 4)
(6, 4, NOW(6), NOW(6)),

-- 리뷰 8: 좋아요 1개 (userId 5)
(8, 5, NOW(6), NOW(6));

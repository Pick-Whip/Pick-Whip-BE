-- ======================================================
-- Design Seed Data (Repeatable)
-- custom_options, design_gallery, design_gallery_keywords
-- shop_cake_sizes(size_id=2) 이후 실행됨
-- ======================================================

/* ------------------------------------------------------
   1) 커스텀 옵션 (custom_options)
   - id 1~11  : shop 1 옵션
   - id 12~15 : shop 2 옵션
   ------------------------------------------------------ */
INSERT INTO custom_options (id, shop_id, category, option_name, additional_price, color_rgb_code)
VALUES
    -- shop 1 옵션
    (1,  1, 'SHEET',   '바닐라 시트',  0,    NULL),
    (2,  1, 'SHEET',   '초코 시트',    1000, NULL),
    (3,  1, 'CREAM',   '생크림',       0,    '#FFFFFF'),
    (4,  1, 'CREAM',   '초코 크림',    1500, '#8B4513'),
    (5,  1, 'TOPPING', '딸기',         3000, NULL),
    (6,  1, 'TOPPING', '마카롱',       5000, NULL),
    (7,  1, 'TOPPING', '금박',         8000, NULL),
    (8,  1, 'ICING',   '핑크 아이싱',  2000, '#FFB6C1'),
    (9,  1, 'ICING',   '블루 아이싱',  2000, '#87CEEB'),
    (10, 1, 'SHAPE',   '하트',         2000, NULL),
    (11, 1, 'SHAPE',   '사각',         2000, NULL),
    -- shop 2 옵션
    (12, 2, 'SHEET',   '바닐라 시트',  0,    NULL),
    (13, 2, 'SHEET',   '초코 시트',    2000, NULL),
    (14, 2, 'CREAM',   '생크림',       0,    '#FFFFFF'),
    (15, 2, 'TOPPING', '딸기',         4000, NULL)
    AS new
ON DUPLICATE KEY UPDATE
                     option_name      = new.option_name,
                     additional_price = new.additional_price,
                     color_rgb_code   = new.color_rgb_code;


/* ------------------------------------------------------
   2) 디자인 갤러리 (design_gallery)
   - id 1~8  : shop 1 디자인
   - id 9~12 : shop 2 디자인
   - id 13~18: shop 3 디자인 (랭킹 테스트용, 1위~5위 + 순위밖)
   ------------------------------------------------------ */
INSERT INTO design_gallery (
    id, shop_id, shop_cake_size_id,
    design_name, base_price, image_url,
    allergy_info, description,
    lettering_text, lettering_line_count, lettering_alignment
) VALUES
      -- shop 1 디자인
      (1, 1, 2, '심플 플라워',          35000, 'https://cdn.picknwhip.com/designs/simple_flower.jpg',  '우유, 계란, 밀',       '화이트 크림 베이스에 플라워 장식',                   'Love You',       'ONE_LINE', 'CENTER'),
      (2, 1, 2, '미니멀 레터링',        35000, 'https://cdn.picknwhip.com/designs/minimal_lettering.jpg', '우유, 계란, 밀',  '심플한 디자인의 미니멀 레터링 케이크',               'Happy Day',      'ONE_LINE', 'CENTER'),
      (3, 1, 2, '아이돌 포토 하트',     42000, 'https://cdn.picknwhip.com/designs/idol_photo_heart.jpg',  '우유, 계란',      '아이돌 사진이 올라간 하트 모양 케이크',              'Happy Birthday', 'TWO_LINE', 'CURVE_UP'),
      (4, 1, 2, '빈티지 생화',          45000, 'https://cdn.picknwhip.com/designs/vintage_flower.jpg',    '우유, 계란, 견과류','빈티지한 무드에 생화 장식 케이크',                'Congratulations','THREE_LINE','CENTER'),
      (5, 1, 2, '로맨틱 플라워',        45000, 'https://placehold.co/300x300?text=Romantic+Flower',       '우유, 계란',      '핑크 장미와 금박 장식',                              NULL, NULL, NULL),
      (6, 1, 2, '초코 드림',            42000, 'https://placehold.co/300x300?text=Choco+Dream',           '우유, 계란, 밀',  '진한 초코 시트와 초코 크림',                         NULL, NULL, NULL),
      (7, 1, 2, '마포구 생일 곰돌이',   35000, 'https://placehold.co/300x300?text=Birthday+Bear',         '우유, 계란',      '귀여운 곰돌이 생일 케이크',                          NULL, NULL, NULL),
      (8, 1, 2, '마포구 1주년 하트',    40000, 'https://placehold.co/300x300?text=Love+Heart',            '우유, 계란',      '기념일에 딱인 하트 케이크',                          NULL, NULL, NULL),
      -- shop 2 디자인
      (9,  2, 5, '강남 졸업 축하 케이크', 50000, 'https://placehold.co/300x300?text=Graduation',    '우유, 계란', '화려한 티아라 케이크',        NULL, NULL, NULL),
      (10, 2, 5, '강남 오픈 기원 2단',   80000, 'https://placehold.co/300x300?text=Opening',       '우유, 계란', '개업 선물용 2단 케이크',       NULL, NULL, NULL),
      (11, 2, 5, '강남 미니멀 레터링',   48000, 'https://placehold.co/300x300?text=Minimal',       '우유, 계란', '깔끔한 레터링 케이크',         'Best Wishes', 'ONE_LINE', 'CENTER'),
      (12, 2, 5, '강남 프리미엄 플라워', 70000, 'https://placehold.co/300x300?text=Premium+Flower','우유, 계란, 견과류', '고급 생화 장식 케이크', NULL, NULL, NULL),
      -- shop 3 디자인 (랭킹 테스트용)
      (13, 3, 7, '초코케이크',          30000, 'https://cdn.picknwhip.com/test/rank1.jpg', NULL, '주문수 5개 - 1등', NULL, NULL, NULL),
      (14, 3, 7, '딸기케이크',          32000, 'https://cdn.picknwhip.com/test/rank2.jpg', NULL, '주문수 4개 - 2등', NULL, NULL, NULL),
      (15, 3, 7, '바닐라케이크',        28000, 'https://cdn.picknwhip.com/test/rank3.jpg', NULL, '주문수 3개 - 3등', NULL, NULL, NULL),
      (16, 3, 7, '녹차케이크',          35000, 'https://cdn.picknwhip.com/test/rank4.jpg', NULL, '주문수 2개 - 4등', NULL, NULL, NULL),
      (17, 3, 7, '치즈케이크',          31000, 'https://cdn.picknwhip.com/test/rank5.jpg', NULL, '주문수 1개 - 5등', NULL, NULL, NULL),
      (18, 3, 7, '순위밖_오래된케이크', 40000, 'https://cdn.picknwhip.com/test/old.jpg',   NULL, '1달 전 주문이라 집계 제외', NULL, NULL, NULL)
    AS new
ON DUPLICATE KEY UPDATE
                     design_name  = new.design_name,
                     base_price   = new.base_price,
                     image_url    = new.image_url,
                     lettering_text          = new.lettering_text,
                     lettering_line_count    = new.lettering_line_count,
                     lettering_alignment     = new.lettering_alignment;


/* ------------------------------------------------------
   3) 디자인 갤러리 키워드
   ------------------------------------------------------ */
DELETE FROM design_gallery_keywords WHERE design_gallery_id BETWEEN 1 AND 18;

INSERT INTO design_gallery_keywords (design_gallery_id, keyword) VALUES
                                                                     -- shop 1 디자인 키워드
                                                                     (1, 'ROUND'), (1, 'SIMPLE'),
                                                                     (2, 'MINIMAL'), (2, 'ROUND'), (2, 'LETTERING'),
                                                                     (3, 'IDOL'), (3, 'HEART'), (3, 'PHOTO_CAKE'),
                                                                     (4, 'VINTAGE'), (4, 'ROUND'),
                                                                     (5, 'ANNIVERSARY'), (5, 'LOVELY'),
                                                                     (6, 'BIRTHDAY'), (6, 'LOVELY'),
                                                                     (7, 'BIRTHDAY'), (7, 'LOVELY'),
                                                                     (8, 'ANNIVERSARY'), (8, 'HEART'),
                                                                     -- shop 2 디자인 키워드
                                                                     (9,  'GRADUATION'), (9,  'GORGEOUS'),
                                                                     (10, 'OPENING'),
                                                                     (11, 'MINIMAL'), (11, 'LETTERING'),
                                                                     (12, 'GORGEOUS'), (12, 'ROUND'),
                                                                     -- shop 3 디자인 키워드 (없음 - 랭킹 테스트 전용)
                                                                     (13, 'ROUND'),
                                                                     (14, 'HEART'),
                                                                     (15, 'ROUND'),
                                                                     (16, 'SQUARE'),
                                                                     (17, 'ROUND');


/* ------------------------------------------------------
   4) 디자인 옵션 매핑 (design_options)
   - shop 1 디자인 1~6 옵션 연결
   ------------------------------------------------------ */
DELETE FROM design_options WHERE design_id BETWEEN 1 AND 12;

INSERT INTO design_options (design_id, custom_option_id, position_x, position_y)
VALUES
    -- design 1: 바닐라 시트 + 생크림
    (1, 1, 0, 0), (1, 3, 0, 0),
    -- design 2: 초코 시트 + 생크림 + 하트
    (2, 2, 0, 0), (2, 3, 0, 0), (2, 10, 0, 0),
    -- design 3: 바닐라 시트 + 생크림 + 사각
    (3, 1, 0, 0), (3, 3, 0, 0), (3, 11, 0, 0),
    -- design 4: 초코 시트 + 생크림
    (4, 2, 0, 0), (4, 3, 0, 0),
    -- design 5: 바닐라 시트 + 생크림 + 딸기 + 금박 + 핑크 아이싱
    (5, 1, 0, 0), (5, 3, 0, 0), (5, 5, 0, 0), (5, 7, 0, 0), (5, 8, 0, 0),
    -- design 6: 초코 시트 + 초코 크림 + 마카롱
    (6, 2, 0, 0), (6, 4, 0, 0), (6, 6, 0, 0);
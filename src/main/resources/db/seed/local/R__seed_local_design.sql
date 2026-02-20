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
   - id 1~8  : shop 1 (마포 스윗 케이크) 디자인
   - id 9~12 : shop 2 (강남 스윗 케이크) 디자인
   - id 13~18: shop 3 (성수 르뽀띠 케이크) 디자인
   ------------------------------------------------------ */
INSERT INTO design_gallery (
    id, shop_id, shop_cake_size_id,
    design_name, base_price, image_url,
    allergy_info, description,
    lettering_text, lettering_line_count, lettering_alignment
) VALUES
      -- shop 1 디자인 (마포 스윗 케이크)
      (1, 1, 2, '심플 플라워 케이크',      35000,
       'd1.png',
       '우유, 계란, 밀',
       '화이트 생크림 베이스 위에 수제 버터크림 꽃을 올린 케이크예요. 은은한 바닐라 향과 부드러운 크림이 조화로운 플라워 케이크입니다.',
       'Love You', 'ONE_LINE', 'CENTER'),

      (2, 1, 2, '미니멀 레터링 케이크',     35000,
       'd2.png',
       '우유, 계란, 밀',
       '깔끔한 크림 마감 위에 손글씨 레터링을 올린 심플한 케이크예요. 어떤 기념일에도 잘 어울리는 미니멀 디자인입니다.',
       'Happy Day', 'ONE_LINE', 'CENTER'),

      (3, 1, 2, '포토 하트 케이크',         42000,
       'd3.png',
       '우유, 계란',
       '좋아하는 사진을 식용 잉크로 프린트해서 하트 모양 케이크 위에 올려드려요. 최애 생일이나 팬 이벤트에 딱 맞는 케이크입니다.',
       'Happy Birthday', 'TWO_LINE', 'CURVE_UP'),

      (4, 1, 2, '빈티지 생화 케이크',       45000,
       'd4.png',
       '우유, 계란, 견과류',
       '빈티지 톤의 크림 위에 제철 생화를 올린 고급스러운 케이크예요. 프로포즈나 기념일 선물로 인기가 많습니다.',
       'Congratulations', 'THREE_LINE', 'CENTER'),

      (5, 1, 2, '로맨틱 로즈 케이크',       45000,
       'd5.png',
       '우유, 계란',
       '핑크 장미 모양 버터크림과 금박 장식이 올라간 로맨틱한 케이크예요. 커플 기념일, 프로포즈에 추천드립니다.',
       NULL, NULL, NULL),

      (6, 1, 2, '트리플 초코 케이크',       42000,
       'd6.png',
       '우유, 계란, 밀, 대두',
       '초코 시트, 가나슈 크림, 초코 드리즐까지 초콜릿 삼중주 케이크예요. 진한 카카오 풍미를 좋아하시는 분께 추천합니다.',
       NULL, NULL, NULL),

      (7, 1, 2, '곰돌이 생일 케이크',       35000,
       'd1.png',
       '우유, 계란',
       '귀여운 곰돌이 피규어와 생일 초가 올라간 케이크예요. 아이 생일파티나 귀여운 걸 좋아하는 친구 생일에 딱이에요.',
       NULL, NULL, NULL),

      (8, 1, 2, '1주년 기념 하트 케이크',   40000,
       'd2.png',
       '우유, 계란',
       '하트 모양 틀에 구운 케이크 위에 딸기와 블루베리로 장식한 기념일 케이크예요. 연인, 부부 기념일에 인기 있는 디자인입니다.',
       NULL, NULL, NULL),

      -- shop 2 디자인 (강남 스윗 케이크)
      (9,  2, 5, '졸업 축하 티아라 케이크',   50000,
       'd3.png',
       '우유, 계란',
       '졸업을 축하하는 마음을 담아 반짝이는 티아라 장식을 올린 2호 케이크예요. 졸업식 당일 픽업 예약이 가능합니다.',
       NULL, NULL, NULL),

      (10, 2, 5, '개업 축하 2단 케이크',      80000,
       'd4.png',
       '우유, 계란',
       '화려한 2단 케이크에 축하 메시지와 꽃 장식을 올린 프리미엄 케이크예요. 개업, 승진 등 특별한 축하 자리에 어울립니다.',
       NULL, NULL, NULL),

      (11, 2, 5, '모던 레터링 케이크',         48000,
       'd5.png',
       '우유, 계란',
       '아이보리 크림 위에 세련된 영문 캘리그라피를 올린 모던한 케이크예요. 심플하지만 고급스러운 느낌을 원하시는 분께 추천합니다.',
       'Best Wishes', 'ONE_LINE', 'CENTER'),

      (12, 2, 5, '프리미엄 생화 케이크',       70000,
       'd6.png',
       '우유, 계란, 견과류',
       '엄선한 제철 생화를 풍성하게 올린 프리미엄 케이크예요. 생화는 당일 아침 공수하여 가장 싱싱한 상태로 준비합니다.',
       NULL, NULL, NULL),

      -- shop 3 디자인 (성수 르뽀띠 케이크)
      (13, 3, 7, '클래식 초코 가나슈 케이크',  30000,
       'd1.png',
       '우유, 계란, 대두',
       '벨기에산 다크 초콜릿으로 만든 가나슈를 듬뿍 올린 클래식 초코 케이크예요. 촉촉한 초코 시트와 진한 가나슈의 조합이 일품입니다.',
       NULL, NULL, NULL),

      (14, 3, 7, '딸기 생크림 케이크',         32000,
       'd2.png',
       '우유, 계란',
       '논산 딸기를 듬뿍 올린 생크림 케이크예요. 부드러운 생크림과 새콤달콤한 딸기의 밸런스가 좋아 남녀노소 모두 좋아하는 케이크입니다.',
       NULL, NULL, NULL),

      (15, 3, 7, '우유 생크림 케이크',         28000,
       'd3.png',
       '우유, 계란',
       '고소한 우유 생크림을 부드럽게 마감한 기본에 충실한 케이크예요. 담백하고 깔끔한 맛으로 어떤 토핑과도 잘 어울립니다.',
       NULL, NULL, NULL),

      (16, 3, 7, '녹차 크림치즈 케이크',       35000,
       'd4.png',
       '우유, 계란, 대두',
       '제주 유기농 말차 파우더와 크림치즈를 블렌딩한 케이크예요. 쌉싸름한 말차와 부드러운 크림치즈의 조화가 매력적입니다.',
       NULL, NULL, NULL),

      (17, 3, 7, '바스크 치즈 케이크',         31000,
       'd5.png',
       '우유, 계란',
       '겉은 캐러멜라이즈되어 바삭하고 속은 촉촉한 정통 바스크 스타일 치즈케이크예요. 커피와 함께 즐기시면 더욱 맛있습니다.',
       NULL, NULL, NULL),

      (18, 3, 7, '얼그레이 쉬폰 케이크',      40000,
       'd6.png',
       '우유, 계란, 밀',
       '얼그레이 찻잎을 우려낸 시트에 마스카포네 크림을 올린 쉬폰 케이크예요. 은은한 베르가못 향이 특징입니다.',
       NULL, NULL, NULL)
    AS new
ON DUPLICATE KEY UPDATE
                     design_name         = new.design_name,
                     base_price          = new.base_price,
                     image_url           = new.image_url,
                     allergy_info        = new.allergy_info,
                     description         = new.description,
                     lettering_text      = new.lettering_text,
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
                                                                     -- shop 3 디자인 키워드
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

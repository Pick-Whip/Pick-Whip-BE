/* ======================================================
   1. 기존 샵 11번 디자인 관련 데이터 삭제 (초기화)
====================================================== */
DELETE FROM design_gallery_keywords WHERE design_gallery_id IN (111, 112, 113, 114, 115, 116);
DELETE FROM design_options WHERE design_id IN (111, 112, 113, 114, 115, 116);
DELETE FROM avail_option WHERE design_id IN (111, 112, 113, 114, 115, 116);
DELETE FROM design_gallery WHERE id IN (111, 112, 113, 114, 115, 116);

/* ======================================================
   2. 디자인 갤러리 추가 (ID 111~116으로 할당)
====================================================== */
INSERT INTO design_gallery (
    id, shop_id, shop_cake_size_id, design_name, base_price, description,
    allergy_info, image_url, lettering_text, lettering_line_count,
    lettering_alignment, lettering_color
) VALUES
      (111, 11, 11, '생딸기 가득 케이크', 25000, '신선한 생딸기가 아낌없이 들어간 시그니처 케이크', '우유, 밀, 딸기', 'https://example.com/strawberry.jpg', 'Happy Birthday', 'ONE_LINE', 'CENTER', '#FF0000'),
      (112, 11, 11, '잔망루피 도안 케이크', 28000, '귀여운 루피 캐릭터가 그려진 커스텀 디자인', '우유, 밀, 대두', 'https://example.com/loopy.jpg', '루피처럼 행복해', 'TWO_LINE', 'CENTER', '#FFC0CB'),
      (113, 11, 12, '레터링 심플 케이크', 35000, '심플한 아이싱에 정갈한 레터링이 돋보이는 디자인', '우유, 밀', 'https://example.com/simple.jpg', 'Good Luck', 'ONE_LINE', 'CENTER', '#000000'),
      (114, 11, 11, '보라색 꽃 리스 케이크', 30000, '우아한 보라색 꽃들로 리스를 두른 감성 케이크', '우유, 밀', 'https://example.com/flower.jpg', '꽃길만 걷자', 'ONE_LINE', 'CURVE_UP', '#800080'),
      (115, 11, 12, '블루 티아라 케이크', 55000, '고급스러운 티아라 장식이 올라간 공주님 케이크', '우유, 밀', 'https://example.com/tiara.jpg', 'My Queen', 'ONE_LINE', 'CENTER', '#0000FF'),
      (116, 11, 11, '딥초코 곰돌이 케이크', 28000, '꾸덕한 초코 시트에 귀여운 곰돌이 데코레이션', '우유, 밀, 계란, 초콜릿', 'https://example.com/bear.jpg', 'HBD BEAR', 'ONE_LINE', 'CENTER', '#4B2C20');

/* ======================================================
   3. 각 디자인별 적용된 옵션 (design_options)
====================================================== */
INSERT INTO design_options (design_id, custom_option_id, position_x, position_y) VALUES
-- 111: 바닐라시트(101) + 우유크림(103) + 화이트아이싱(105) + 원형쉐입(107) + 생딸기(109, 좌표값 있음)
(111, 101, NULL, NULL), (111, 103, NULL, NULL), (111, 105, NULL, NULL), (111, 107, NULL, NULL), (111, 109, 50.0, 50.0),

-- 112: 바닐라시트(101) + 딸기크림(104) + 핑크아이싱(106) + 원형쉐입(107)
(112, 101, NULL, NULL), (112, 104, NULL, NULL), (112, 106, NULL, NULL), (112, 107, NULL, NULL),

-- 113: 초코시트(102) + 우유크림(103) + 화이트아이싱(105) + 원형쉐입(107)
(113, 102, NULL, NULL), (113, 103, NULL, NULL), (113, 105, NULL, NULL), (113, 107, NULL, NULL),

-- 114: 바닐라시트(101) + 우유크림(103) + 화이트아이싱(105) + 원형쉐입(107)
(114, 101, NULL, NULL), (114, 103, NULL, NULL), (114, 105, NULL, NULL), (114, 107, NULL, NULL),

-- 115: 바닐라시트(101) + 우유크림(103) + 화이트아이싱(105) + 원형쉐입(107)
(115, 101, NULL, NULL), (115, 103, NULL, NULL), (115, 105, NULL, NULL), (115, 107, NULL, NULL),

-- 116: 초코시트(102) + 초코크림(없으므로 딸기크림 104 대체) + 화이트아이싱(105) + 원형쉐입(107)
(116, 102, NULL, NULL), (116, 104, NULL, NULL), (116, 105, NULL, NULL), (116, 107, NULL, NULL);

/* ======================================================
   4. 키워드 매핑 (노션 기반 스타일 분류)
====================================================== */
INSERT INTO design_gallery_keywords (design_gallery_id, keyword) VALUES
                                                                     (111, 'LOVELY'), (111, 'COLORFUL'),
                                                                     (112, 'CHARACTER'), (112, 'LOVELY'),
                                                                     (113, 'MODERN'), (113, 'SIMPLE'),
                                                                     (114, 'ELEGANT'), (114, 'FLOWER'),
                                                                     (115, 'LUXURY'), (115, 'ELEGANT'),
                                                                     (116, 'CUTE'), (116, 'SIMPLE');

/* ======================================================
   5. 선택 가능 옵션 매핑 (avail_option)
   - 샵 11의 모든 마스터 옵션(101~110)을 선택 가능하게 연결
====================================================== */
INSERT INTO avail_option (design_id, custom_option_id)
SELECT d.id, c.id
FROM design_gallery d, custom_options c
WHERE d.id BETWEEN 111 AND 116 AND c.shop_id = 11;


UPDATE shops SET shop_image_url = '1%E1%84%87%E1%85%A5%E1%86%AB%E1%84%8F%E1%85%A6%E1%84%8B%E1%85%B5%E1%84%8F%E1%85%B3%E1%84%89%E1%85%A3%E1%86%B8.webp' WHERE shop_id = 11;
UPDATE shops SET shop_image_url = '2%E1%84%87%E1%85%A5%E1%86%AB%E1%84%8F%E1%85%A6%E1%84%8B%E1%85%B5%E1%84%8F%E1%85%B3%E1%84%89%E1%85%A3%E1%86%B8.webp' WHERE shop_id = 12;
UPDATE shops SET shop_image_url = '3%E1%84%87%E1%85%A5%E1%86%AB%E1%84%8F%E1%85%A6%E1%84%8B%E1%85%B5%E1%84%8F%E1%85%B3%E1%84%89%E1%85%A3%E1%86%B8.webp' WHERE shop_id = 13;
UPDATE shops SET shop_image_url = '4%E1%84%87%E1%85%A5%E1%86%AB+%E1%84%8F%E1%85%A6%E1%84%8B%E1%85%B5%E1%84%8F%E1%85%B3%E1%84%89%E1%85%A3%E1%86%B8.webp' WHERE shop_id = 14;


UPDATE design_gallery SET image_url = '1%E1%84%87%E1%85%A5%E1%86%AB%E1%84%83%E1%85%B5%E1%84%8C%E1%85%A1%E1%84%8B%E1%85%B5%E1%86%AB%E1%84%80%E1%85%A2%E1%86%AF%E1%84%85%E1%85%A5%E1%84%85%E1%85%B5.webp' WHERE id = 111; -- 1번: 생딸기
UPDATE design_gallery SET image_url = '2%E1%84%87%E1%85%A5%E1%86%AB%E1%84%83%E1%85%B5%E1%84%80%E1%85%A2%E1%86%AF.html' WHERE id = 112;                       -- 2번: 잔망루피
UPDATE design_gallery SET image_url = '3%E1%84%87%E1%85%A5%E1%86%AB%E3%84%B4%E1%84%83%E1%85%B5%E1%84%80%E1%85%A2%E1%86%AF.webp' WHERE id = 113;             -- 3번: 심플레터링
UPDATE design_gallery SET image_url = '5%E1%84%87%E1%85%A5%E1%86%AB%E1%84%83%E1%85%B5%E1%84%80%E1%85%A2%E1%86%AF.webp' WHERE id = 114;                       -- 4번: 꽃리스
UPDATE design_gallery SET image_url = '%E1%84%8D%E1%85%B5%E1%86%AB5%E1%84%87%E1%85%A5%E1%86%AB%E3%84%B7%E1%84%80%E1%85%A2%E1%86%AF.webp' WHERE id = 115;     -- 5번: 티아라
UPDATE design_gallery SET image_url = '6%E1%84%87%E1%85%A5%E1%86%AB%E1%84%83%E1%85%B5%E1%84%80%E1%85%A2%E1%86%AF.html' WHERE id = 116;                       -- 6번: 곰돌이
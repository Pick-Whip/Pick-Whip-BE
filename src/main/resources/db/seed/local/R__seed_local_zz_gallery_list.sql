-- ======================================================
-- 디자인 갤러리 API 테스트용 통합 시드 데이터 (Repeatable)
-- 파일명: R__seed_local_zz_gallery_list.sql
-- 실행 순서: 기존 시드(local, design, order 등) 실행 후 마지막에 적용
-- ======================================================

-- 1. [가게 위치/주소 현실화]
-- MySQL 8.0 SRID 4326 좌표 순서: POINT(위도 Latitude, 경도 Longitude)

-- 기존 Shop 1 (테스트 케이크샵) -> 마포구 (홍대입구)
UPDATE shops
SET
    address = '서울 마포구 양화로 160',
    district = '마포구',
    location = ST_GeomFromText('POINT(37.5565 126.9241)', 4326), -- (위도, 경도)
    average_rating = 4.5,
    min_price = 30000,
    max_price = 80000,
    status = 'ACTIVE'
WHERE shop_id = 1;

-- Shop 2 (가게 2) -> 강남구 (강남역)
INSERT INTO shops (
    shop_id, owner_id, shop_name, phone, address, district, location,
    average_rating, min_price, max_price, status, description
) VALUES (
             2, 1, '강남 스윗 케이크', '02-555-5555', '서울 강남구 강남대로 396', '강남구',
             ST_GeomFromText('POINT(37.4979 127.0276)', 4326), -- (위도, 경도)
             4.8, 45000, 90000, 'ACTIVE', '강남역 최고의 케이크 맛집'
         )
    ON DUPLICATE KEY UPDATE
                         address = VALUES(address),
                         district = VALUES(district),
                         location = VALUES(location),
                         average_rating = VALUES(average_rating),
                         shop_name = VALUES(shop_name),
                         status = 'ACTIVE';


-- 2. [디자인 갤러리 데이터 보강]
-- Shop 1 (마포구) 디자인
INSERT INTO design_gallery (
    id, shop_id, shop_cake_size_id, design_name, base_price, image_url, description
) VALUES
      (10, 1, 1, '마포구 생일 곰돌이', 35000, 'https://placehold.co/300x300?text=Birthday+Bear', '귀여운 곰돌이 생일 케이크'),
      (11, 1, 1, '마포구 1주년 하트', 40000, 'https://placehold.co/300x300?text=Love+Heart', '기념일에 딱인 하트 케이크'),
      (12, 1, 1, '마포구 산타 케이크', 45000, 'https://placehold.co/300x300?text=Santa+Cake', '크리스마스 한정판')
    ON DUPLICATE KEY UPDATE
                         design_name = VALUES(design_name),
                         image_url = VALUES(image_url);

-- Shop 2 (강남구) 디자인
INSERT INTO design_gallery (
    id, shop_id, shop_cake_size_id, design_name, base_price, image_url, description
) VALUES
      (20, 2, 1, '강남구 졸업 축하 티아라', 50000, 'https://placehold.co/300x300?text=Graduation', '화려한 티아라 케이크'),
      (21, 2, 1, '강남구 대박기원 2단', 80000, 'https://placehold.co/300x300?text=Opening', '개업 선물용 2단 케이크')
    ON DUPLICATE KEY UPDATE
                         design_name = VALUES(design_name),
                         image_url = VALUES(image_url);


-- 3. [키워드/카테고리 매핑]
-- 컬럼명: keyword (단수형)
DELETE FROM design_gallery_keywords WHERE design_gallery_id IN (10, 11, 12, 20, 21);

INSERT INTO design_gallery_keywords (design_gallery_id, keyword) VALUES
-- Shop 1 (마포구)
(10, 'BIRTHDAY'),
(10, 'CUTE'),
(11, 'ANNIVERSARY'),
(11, 'LOVELY'),
(12, 'CHRISTMAS'),

-- Shop 2 (강남구)
(20, 'GRADUATION'),
(20, 'GORGEOUS'),
(21, 'OPENING');


-- 4. [마이픽(찜) 데이터]
-- [수정] updated_at 컬럼 제거 (created_at만 사용)
INSERT INTO favorite_designs (user_id, design_id, created_at)
SELECT 1, 10, NOW()
FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM favorite_designs WHERE user_id = 1 AND design_id = 10);
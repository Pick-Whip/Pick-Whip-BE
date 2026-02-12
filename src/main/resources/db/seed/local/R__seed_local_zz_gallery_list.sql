-- ======================================================
-- 디자인 갤러리 API 테스트용 통합 시드 데이터 (Repeatable)
-- 파일명: R__seed_local_zz_gallery_list.sql
-- 실행 순서: 모든 시드 중 마지막에 적용
-- shop/design/user 데이터는 모두 앞 파일들에서 이미 생성됨
-- ======================================================

-- 1. [가게 위치/주소 현실화]
-- shop 1 (마포구 홍대입구) - 이미 R__seed_local.sql 에서 설정되었으나 최종 보정
UPDATE shops
SET
    address        = '서울 마포구 양화로 160',
    district       = '마포구',
    location       = ST_GeomFromText('POINT(37.5565 126.9241)', 4326),
    average_rating = 4.5,
    min_price      = 30000,
    max_price      = 80000,
    status         = 'ACTIVE'
WHERE shop_id = 1;

-- shop 2 (강남구 강남역) - 이미 R__seed_local.sql 에서 설정되었으나 최종 보정
UPDATE shops
SET
    address        = '서울 강남구 강남대로 396',
    district       = '강남구',
    location       = ST_GeomFromText('POINT(37.5565 126.9241)', 4326),
    average_rating = 4.8,
    min_price      = 45000,
    max_price      = 90000,
    status         = 'ACTIVE'
WHERE shop_id = 2;

-- 2. [shop 2 케이크 사이즈 FK 보완]
-- design_gallery id 9~12 는 shop_cake_size_id=5 를 참조하므로 size_id=5 가 존재해야 함
-- R__seed_local.sql 에서 이미 생성됨 (size_id 4~6: shop 2)


-- 3. [마이픽(찜) 데이터 - user 1 기준]
-- user 1이 shop 1의 design 1, 7, 8 을 찜
INSERT INTO favorite_designs (user_id, design_id, created_at)
SELECT 1, 1, NOW()
FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM favorite_designs WHERE user_id = 1 AND design_id = 1);

INSERT INTO favorite_designs (user_id, design_id, created_at)
SELECT 1, 7, NOW()
FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM favorite_designs WHERE user_id = 1 AND design_id = 7);

INSERT INTO favorite_designs (user_id, design_id, created_at)
SELECT 1, 8, NOW()
FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM favorite_designs WHERE user_id = 1 AND design_id = 8);

-- user 1이 shop 2의 design 9 를 찜
INSERT INTO favorite_designs (user_id, design_id, created_at)
SELECT 1, 9, NOW()
FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM favorite_designs WHERE user_id = 1 AND design_id = 9);


-- 4. [즐겨찾기 가게 - user 1 기준]
INSERT INTO favorite_shops (user_id, shop_id, created_at)
SELECT 1, 1, NOW()
FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM favorite_shops WHERE user_id = 1 AND shop_id = 1);

INSERT INTO favorite_shops (user_id, shop_id, created_at)
SELECT 1, 2, NOW()
FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM favorite_shops WHERE user_id = 1 AND shop_id = 2);


-- ======================================================
-- 최종 데이터 구조 요약
-- ======================================================
-- users     : 1~23
-- shops     : 1(마포), 2(강남), 3(랭킹)
-- cake sizes: 1~3(shop1), 4~6(shop2), 7(shop3)
-- custom_opt: 1~11(shop1), 12~15(shop2)
-- designs   : 1~8(shop1), 9~12(shop2), 13~18(shop3)
-- orders    : 1(완료), 2~7(전상태), 8~14(리뷰용), 15~31(랭킹용), 32~36(인기디자인), 37~41(베스트리뷰)
-- reviews   : 1~7(user1작성), 8~12(베스트리뷰)
-- drafts    : 1 (user 1)
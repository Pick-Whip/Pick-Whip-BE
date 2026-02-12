-- ======================================================
-- Local Seed Data (Repeatable)
-- Profile: local
-- Goal   : Safe to run multiple times (idempotent)
-- ======================================================

/* ------------------------------------------------------
   1) Customer User (손님)
   - Identified by UNIQUE(email) or UNIQUE(kakao_id)
   ------------------------------------------------------ */
INSERT INTO users (
    created_at,
    updated_at,
    deleted_at,
    email,
    kakao_id,
    name,
    nickname,
    birthdate,
    phone,
    profile_image_url,
    status
) VALUES (
             NOW(6),
             NOW(6),
             NULL,
             'local.customer@picknwhip.com',
             2000000001,
             '사용자',
             '초코케이크',
             NULL,
             '010-1111-1111',
             NULL,
             'ACTIVE'
         ) AS new
ON DUPLICATE KEY UPDATE
                     updated_at = new.updated_at,
                     status = new.status;


/* ------------------------------------------------------
   2) Shop Owner User (사장님)
   - Needed for shops.owner_id FK
   ------------------------------------------------------ */
INSERT INTO users (
    created_at,
    updated_at,
    deleted_at,
    email,
    kakao_id,
    name,
    nickname,
    birthdate,
    phone,
    profile_image_url,
    status
) VALUES (
             NOW(6),
             NOW(6),
             NULL,
             'local.owner@picknwhip.com',
             1000000001,
             '사장님',
             '사장님',
             NULL,
             '010-0000-0000',
             NULL,
             'ACTIVE'
         ) AS new
ON DUPLICATE KEY UPDATE
                     updated_at = new.updated_at,
                     status = new.status;


/* ------------------------------------------------------
   3) Shop (1개)
   - owner_id: resolved by owner's email
   - location: POINT + SRID 4326 (MySQL GIS)
   ------------------------------------------------------ */
INSERT INTO shops (
    shop_id,
    average_rating,
    chat_nickname,
    chat_profile_image_url,
    created_at,
    day_order_guide,
    description,
    location,
    min_price,
    parking_guide,
    payment_notice,
    phone,
    pickup_time_guide,
    precaution_notice,
    prepayment,
    registration_image_url,
    shop_image_url,
    shop_name,
    status,
    verification_status,
    owner_id
) VALUES (
             1,
             NULL,
             '테스트 케이크샵',
             NULL,
             NOW(6),
             1,
             '로컬 개발용 테스트 케이크샵입니다.',
             ST_GeomFromText('POINT(45.0000 90.0000)', 4326),
             NULL,
             NULL,
             NULL,
             '02-1234-5678',
             1,
             null,
             NULL,
             NULL,
             NULL,
             '테스트 케이크샵',
             'ACTIVE',
             'VERIFIED',
             (SELECT user_id FROM users WHERE email = 'local.owner@picknwhip.com')
         ) AS new
ON DUPLICATE KEY UPDATE
                     phone = new.phone,
                     shop_name = new.shop_name,
                     status = new.status,
                     verification_status = new.verification_status,
                     location = new.location,
                     owner_id = new.owner_id;

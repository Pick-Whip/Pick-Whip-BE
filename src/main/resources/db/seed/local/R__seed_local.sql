-- ======================================================
-- Local Seed Data (Repeatable)
-- Applied only in 'local' profile
-- Safe to run multiple times
-- ======================================================

/*
 * 1) Customer user (손님)
 */
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
)
VALUES (
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
       )
    ON DUPLICATE KEY UPDATE
                         updated_at = VALUES(updated_at),
                         status = VALUES(status);

/*
 * 2) Shop owner user (사장님) - shops.owner_id FK 때문에 필요
 */
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
)
VALUES (
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
       )
    ON DUPLICATE KEY UPDATE
                         updated_at = VALUES(updated_at),
                         status = VALUES(status);

/*
 * 3) Shop 1개 (owner는 위 local.owner)
 *  - location: POINT + SRID 4326 (MySQL GIS)
 *  - phone/shop_name/owner_id/location은 NOT NULL
 */
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
)
VALUES (
           1,
           NULL,
           '테스트 케이크샵',
           NULL,
           NOW(6),
           NULL,
           '로컬 개발용 테스트 케이크샵입니다.',
           ST_GeomFromText('POINT(45.0000 90.0000)', 4326),
           NULL,
           NULL,
           NULL,
           '02-1234-5678',
           NULL,
           NULL,
           NULL,
           NULL,
           NULL,
           '테스트 케이크샵',
           'ACTIVE',
           'VERIFIED',
           (SELECT user_id FROM users WHERE email = 'local.owner@picknwhip.com')
       )
    ON DUPLICATE KEY UPDATE
                         created_at = VALUES(created_at),
                         phone = VALUES(phone),
                         shop_name = VALUES(shop_name),
                         status = VALUES(status),
                         verification_status = VALUES(verification_status),
                         location = VALUES(location),
                         owner_id = VALUES(owner_id);

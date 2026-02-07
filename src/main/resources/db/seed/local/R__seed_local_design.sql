-- shop_cake_sizes가 R__seed_local_order.sql보다 먼저 실행되므로 여기서 선행 삽입
INSERT INTO shop_cake_sizes (size_id, shop_id, size_name, diameter, price)
VALUES (1, 1, '1호', '15cm', 30000) AS new
ON DUPLICATE KEY UPDATE
    shop_id   = new.shop_id,
    size_name = new.size_name,
    diameter  = new.diameter,
    price     = new.price;

insert into design_gallery (
    id,
    shop_id,
    shop_cake_size_id,
    design_name,
    base_price,
    image_url,
    allergy_info,
    description,
    lettering_text,
    lettering_line_count,
    lettering_alignment
) values
-- 디자인 2
(
    2,
    1,
    1,
    '미니멀 레터링 케이크',
    35000,
    'https://cdn.picknwhip.com/designs/minimal_lettering.jpg',
    '우유, 계란, 밀',
    '심플한 디자인의 미니멀 레터링 케이크입니다.',
    'Happy Day',
    'ONE_LINE',
    'CENTER'
),
-- 디자인 3
(
    3,
    1,
    1,
    '아이돌 포토 하트 케이크',
    42000,
    'https://cdn.picknwhip.com/designs/idol_photo_heart.jpg',
    '우유, 계란',
    '아이돌 사진이 올라간 하트 모양 케이크입니다.',
    null,
    null,
    null
),
-- 디자인 4
(
    4,
    1,
    1,
    '빈티지 생화 케이크',
    45000,
    'https://cdn.picknwhip.com/designs/vintage_flower.jpg',
    '우유, 계란, 견과류',
    '빈티지한 무드에 생화 장식이 들어간 케이크입니다.',
    null,
    null,
    null
);

insert into design_gallery_keywords (
    design_gallery_id,
    keyword
) values
-- 디자인 2: 미니멀 + 원형 + 레터링
(2, 'MINIMAL'),
(2, 'ROUND'),
(2, 'LETTERING'),

-- 디자인 3: 아이돌 + 하트 + 포토케이크
(3, 'IDOL'),
(3, 'HEART'),
(3, 'PHOTO_CAKE'),

-- 디자인 4: 빈티지 + 심플
(4, 'VINTAGE'),
(4, 'SIMPLE');

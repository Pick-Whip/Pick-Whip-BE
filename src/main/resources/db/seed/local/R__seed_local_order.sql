-- shop_cake_sizes 테이블 seed 데이터
INSERT INTO shop_cake_sizes (
    size_id,
    shop_id,
    size_name,
    diameter,
    price
) VALUES (
             1,
             1,
             '1호',
             '15cm',
             30000
         ) AS new
ON DUPLICATE KEY UPDATE
                     shop_id = new.shop_id,
                     size_name = new.size_name,
                     diameter = new.diameter,
                     price = new.price;


-- design_gallery 테이블 seed 데이터
INSERT INTO design_gallery (
    id,
    shop_id,
    shop_cake_size_id,
    design_name,
    base_price,
    image_url,
    allergy_info,
    description
) VALUES (
             1,
             1,
          1,
             '심플 플라워 디자인',
             35000,
             NULL,
             '우유, 계란, 밀 함유',
             '화이트 크림 베이스에 플라워 장식을 더한 심플한 디자인 케이크입니다.'
         ) AS new
ON DUPLICATE KEY UPDATE
                     shop_id = new.shop_id,
                     design_name = new.design_name,
                     base_price = new.base_price,
                     image_url = new.image_url,
                     allergy_info = new.allergy_info,
                     description = new.description;


-- orders 테이블 seed 데이터
INSERT INTO orders (
    id,
    user_id,
    shop_id,
    shop_cake_size_id,
    design_id,
    status,
    pickup_datetime,
    lettering_text,
    lettering_line_count,
    lettering_alignment,
    additional_request,
    reference_image_url,
    payment_method,
    payment_status,
    total_price,
    deposit_amount,
    rejection_reason,
    created_at,
    updated_at
) VALUES (
             1,
             1,
             1,
             1,
             1,
             'COMPLETED',
             '2026-02-01 15:00:00',
             '생일 축하해',
             'ONE_LINE',
             'CENTER',
             '딸기 많이 올려주세요',
             'https://example.com/reference/sample.jpg',
             'CARD',
             'PAID',
             35000,
             10000,
             NULL,
             NOW(),
             NOW()
         ) AS new
ON DUPLICATE KEY UPDATE
                     user_id = new.user_id,
                     shop_id = new.shop_id,
                     shop_cake_size_id = new.shop_cake_size_id,
                     design_id = new.design_id,
                     status = new.status,
                     pickup_datetime = new.pickup_datetime,
                     lettering_text = new.lettering_text,
                     lettering_line_count = new.lettering_line_count,
                     lettering_alignment = new.lettering_alignment,
                     additional_request = new.additional_request,
                     reference_image_url = new.reference_image_url,
                     payment_method = new.payment_method,
                     payment_status = new.payment_status,
                     total_price = new.total_price,
                     deposit_amount = new.deposit_amount,
                     rejection_reason = new.rejection_reason,
                     updated_at = NOW();

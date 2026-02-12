-- ======================================================
-- Order Basic Seed Data (Repeatable)
-- Goal: 기본 주문 1개 (user 1, shop 1)
-- design, shop_cake_sizes 이후 실행됨
-- ======================================================

-- orders 테이블 기본 주문 데이터 (주문 ID: 1)
INSERT INTO orders (
    id, user_id, shop_id, shop_cake_size_id, design_id, status,
    pickup_datetime,
    lettering_text, lettering_line_count, lettering_alignment,
    additional_request, order_additional_request,
    reference_image_url, payment_method, payment_status,
    total_price, deposit_amount, rejection_reason,
    created_at, updated_at,
    order_code, customer_name, customer_phone
) VALUES (
             1, 1, 1, 2, 1, 'COMPLETED',
             '2026-02-01 15:00:00',
             '생일 축하해', 'ONE_LINE', 'CENTER',
             '딸기 많이 올려주세요', '주문이 완료되면 문자 한번만 남겨주세요',
             'https://example.com/reference/sample.jpg', 'CARD', 'PAID',
             35000, 10000, NULL,
             '2026-01-28 10:00:00', '2026-01-28 10:00:00',
             '260201_001', '김픽휩', '010-1111-1111'
         ) AS new
ON DUPLICATE KEY UPDATE
                     user_id          = new.user_id,
                     shop_id          = new.shop_id,
                     design_id        = new.design_id,
                     status           = new.status,
                     payment_status   = new.payment_status,
                     total_price      = new.total_price,
                     order_code       = new.order_code,
                     updated_at       = NOW();
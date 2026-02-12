-- ======================================================
-- Draft (임시저장 주문서) Seed Data (Repeatable)
-- Goal   : Test 'Create Order' API Flow
-- user_id = 1 기준으로 테스트
-- ======================================================

-- draft ID 1: user 1, shop 1, design 1
INSERT INTO orders_drafts (
    id,
    user_id,
    shop_id,
    shop_cake_size_id,
    design_id,
    pickup_datetime,
    lettering_text,
    lettering_line_count,
    lettering_alignment,
    additional_request,
    reference_image_url,
    created_at,
    updated_at
) VALUES (
             1,
             1,
             1,
             2,
             1,
             DATE_ADD(CURRENT_DATE, INTERVAL 3 DAY) + INTERVAL 14 HOUR,
             'Happy Birthday',
             'ONE_LINE',
             'CENTER',
             '딸기 많이 올려주세요',
             'https://example.com/reference/draft1.jpg',
             NOW(6),
             NOW(6)
         ) AS new
ON DUPLICATE KEY UPDATE
                     pickup_datetime = new.pickup_datetime,
                     design_id       = new.design_id,
                     updated_at      = NOW(6);


-- draft items (옵션 연결)
INSERT INTO order_draft_items (
    id, draft_id, custom_option_id, position_x, position_y
) VALUES
      (1, 1, 2, NULL, NULL),  -- 초코 시트
      (2, 1, 3, NULL, NULL),  -- 생크림
      (3, 1, 5, NULL, NULL)   -- 딸기 토핑
    AS new
ON DUPLICATE KEY UPDATE
                     custom_option_id = new.custom_option_id;

UPDATE shops
SET pickup_time_guide = '0'
WHERE pickup_time_guide IS NULL OR pickup_time_guide = '';

UPDATE shops
SET day_order_guide = '0'
WHERE day_order_guide IS NULL OR day_order_guide = '';


ALTER TABLE shops
    MODIFY COLUMN pickup_time_guide INT NOT NULL DEFAULT 0 COMMENT '픽업 소요 시간(일 단위)',
    MODIFY COLUMN day_order_guide TINYINT(1) NOT NULL DEFAULT 0 COMMENT '당일 예약 가능 여부 (0:불가, 1:가능)';
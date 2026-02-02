CREATE TABLE IF NOT EXISTS daily_shop_order_counters (
                                                         id BIGINT AUTO_INCREMENT PRIMARY KEY,
                                                         shop_id BIGINT NOT NULL,
                                                         date DATE NOT NULL,
                                                         count INT NOT NULL DEFAULT 0,
                                                         created_at DATETIME(6) DEFAULT CURRENT_TIMESTAMP(6),
    updated_at DATETIME(6) DEFAULT CURRENT_TIMESTAMP(6) ON UPDATE CURRENT_TIMESTAMP(6),

    CONSTRAINT uk_daily_shop_order_counter_shop_date UNIQUE (shop_id, date)
    ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

ALTER TABLE orders
    MODIFY COLUMN additional_request VARCHAR(255) NULL COMMENT '디자인 관련 요청사항 (From Draft)';

ALTER TABLE orders
    ADD COLUMN order_additional_request VARCHAR(255) NULL COMMENT '주문/픽업 관련 요청사항 (From OrderSheet)',
    ADD COLUMN order_code VARCHAR(20) NOT NULL DEFAULT 'TEMP' COMMENT '주문 코드',
    ADD COLUMN customer_name VARCHAR(50) NOT NULL DEFAULT 'TEMP' COMMENT '픽업자 이름',
    ADD COLUMN customer_phone VARCHAR(20) NOT NULL DEFAULT 'TEMP' COMMENT '픽업자 연락처';

ALTER TABLE orders
    ALTER COLUMN order_code DROP DEFAULT,
ALTER COLUMN customer_name DROP DEFAULT,
    ALTER COLUMN customer_phone DROP DEFAULT;

ALTER TABLE orders
    ADD CONSTRAINT uk_orders_order_code UNIQUE (order_code);
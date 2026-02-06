-- V16__create_order_histories_table.sql
SET NAMES utf8mb4 COLLATE utf8mb4_unicode_ci;

CREATE TABLE IF NOT EXISTS order_histories (
                                               `id`            BIGINT NOT NULL AUTO_INCREMENT,
                                               `order_id`      BIGINT NOT NULL COMMENT 'orders 테이블 참조 ID',
                                               `status`        VARCHAR(50) NOT NULL COMMENT '주문 상태 Enum 값',
    `created_at`    DATETIME(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6),
    `updated_at`    DATETIME(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6) ON UPDATE CURRENT_TIMESTAMP(6),

    PRIMARY KEY (`id`)
    ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

DROP PROCEDURE IF EXISTS AddForeignKeyToOrderHistories;

DELIMITER $$

CREATE PROCEDURE AddForeignKeyToOrderHistories()
BEGIN
    IF NOT EXISTS (
        SELECT * FROM information_schema.TABLE_CONSTRAINTS
        WHERE CONSTRAINT_SCHEMA = DATABASE()
          AND TABLE_NAME = 'order_histories'
          AND CONSTRAINT_NAME = 'fk_order_histories_order'
    ) THEN
ALTER TABLE order_histories
    ADD CONSTRAINT fk_order_histories_order
        FOREIGN KEY (order_id) REFERENCES orders (id) ON DELETE CASCADE;
END IF;
END $$

DELIMITER ;
CALL AddForeignKeyToOrderHistories();
DROP PROCEDURE AddForeignKeyToOrderHistories;
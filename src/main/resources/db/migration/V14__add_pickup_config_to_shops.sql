-- V14__add_pickup_config_to_shops.sql
-- 픽업 캘린더 설정을 위한 shops 테이블 컬럼 추가
-- (slot_interval_minutes, max_orders_per_slot)

DROP PROCEDURE IF EXISTS AddPickupConfigToShops;

DELIMITER $$

CREATE PROCEDURE AddPickupConfigToShops()
BEGIN
    -- 1. slot_interval_minutes 컬럼 추가 (기본값 30분)
    IF NOT EXISTS (
        SELECT *
        FROM information_schema.COLUMNS
        WHERE TABLE_SCHEMA = DATABASE()
          AND TABLE_NAME = 'shops'
          AND COLUMN_NAME = 'slot_interval_minutes'
    ) THEN
ALTER TABLE shops
    ADD COLUMN slot_interval_minutes INT NOT NULL DEFAULT 30 COMMENT '픽업 시간 간격 (분 단위)';
END IF;

    -- 2. max_orders_per_slot 컬럼 추가 (기본값 2건)
    IF NOT EXISTS (
        SELECT *
        FROM information_schema.COLUMNS
        WHERE TABLE_SCHEMA = DATABASE()
          AND TABLE_NAME = 'shops'
          AND COLUMN_NAME = 'max_orders_per_slot'
    ) THEN
ALTER TABLE shops
    ADD COLUMN max_orders_per_slot INT NOT NULL DEFAULT 2 COMMENT '슬롯당 최대 주문 가능 수';
END IF;
END $$

DELIMITER ;

-- 프로시저 실행
CALL AddPickupConfigToShops();

-- 프로시저 삭제 (Clean up)
DROP PROCEDURE AddPickupConfigToShops;
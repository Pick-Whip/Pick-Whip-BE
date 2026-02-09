DROP PROCEDURE IF EXISTS AddAddressColumnToShops;

DELIMITER $$

CREATE PROCEDURE AddAddressColumnToShops()
BEGIN
    IF NOT EXISTS (
        SELECT *
        FROM information_schema.COLUMNS
        WHERE TABLE_SCHEMA = DATABASE()
          AND TABLE_NAME = 'shops'
          AND COLUMN_NAME = 'address'
    ) THEN
ALTER TABLE shops
    ADD COLUMN address VARCHAR(255) NOT NULL DEFAULT '' COMMENT '가게 도로명/지번 주소';
END IF;
END $$

DELIMITER ;
CALL AddAddressColumnToShops();
DROP PROCEDURE AddAddressColumnToShops;
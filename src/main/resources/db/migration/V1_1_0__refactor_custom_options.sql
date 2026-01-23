-- V1_1_0__refactor_custom_options.sql
-- 리팩토링: custom_id -> custom_option_id 명칭 변경 및 불필요 컬럼 삭제

SET FOREIGN_KEY_CHECKS = 0;

-- 1. [custom_options] 색상 코드 NULL 허용으로 변경
ALTER TABLE `custom_options`
    MODIFY COLUMN `color_rgb_code` varchar(7) NULL;

-- 2. [design_options] custom_id -> custom_option_id 변경
-- custom_id -> custom_options(id) FK가 존재하면 찾아서 DROP (FK 이름 하드코딩 제거)
SET @fk_design_options := (
  SELECT CONSTRAINT_NAME
  FROM information_schema.KEY_COLUMN_USAGE
  WHERE TABLE_SCHEMA = DATABASE()
    AND TABLE_NAME = 'design_options'
    AND COLUMN_NAME = 'custom_id'
    AND REFERENCED_TABLE_NAME = 'custom_options'
  LIMIT 1
);

SET @sql := IF(@fk_design_options IS NULL,
               'SELECT 1',
               CONCAT('ALTER TABLE `design_options` DROP FOREIGN KEY `', @fk_design_options, '`'));

PREPARE stmt FROM @sql;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;

ALTER TABLE `design_options`
    CHANGE COLUMN `custom_id` `custom_option_id` bigint NOT NULL;

-- FK를 사람이 읽을 수 있는 고정 이름으로 생성
ALTER TABLE `design_options`
    ADD CONSTRAINT `fk_design_options_custom_option_id`
        FOREIGN KEY (`custom_option_id`) REFERENCES `custom_options` (`id`);

-- 3. [order_draft_items] 중복 컬럼 custom_id 삭제
-- custom_id -> custom_options(id) FK가 존재하면 찾아서 DROP
SET @fk_order_draft_items := (
  SELECT CONSTRAINT_NAME
  FROM information_schema.KEY_COLUMN_USAGE
  WHERE TABLE_SCHEMA = DATABASE()
    AND TABLE_NAME = 'order_draft_items'
    AND COLUMN_NAME = 'custom_id'
    AND REFERENCED_TABLE_NAME = 'custom_options'
  LIMIT 1
);

SET @sql := IF(@fk_order_draft_items IS NULL,
               'SELECT 1',
               CONCAT('ALTER TABLE `order_draft_items` DROP FOREIGN KEY `', @fk_order_draft_items, '`'));

PREPARE stmt FROM @sql;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;

ALTER TABLE `order_draft_items`
DROP COLUMN `custom_id`;

-- 4. [order_items] custom_id -> custom_option_id 변경 및 색상 NULL 허용
-- custom_id -> custom_options(id) FK가 존재하면 찾아서 DROP
SET @fk_order_items := (
  SELECT CONSTRAINT_NAME
  FROM information_schema.KEY_COLUMN_USAGE
  WHERE TABLE_SCHEMA = DATABASE()
    AND TABLE_NAME = 'order_items'
    AND COLUMN_NAME = 'custom_id'
    AND REFERENCED_TABLE_NAME = 'custom_options'
  LIMIT 1
);

SET @sql := IF(@fk_order_items IS NULL,
               'SELECT 1',
               CONCAT('ALTER TABLE `order_items` DROP FOREIGN KEY `', @fk_order_items, '`'));

PREPARE stmt FROM @sql;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;

ALTER TABLE `order_items`
    CHANGE COLUMN `custom_id` `custom_option_id` bigint NOT NULL;

ALTER TABLE `order_items`
    MODIFY COLUMN `color_rgb_code` varchar(7) NULL;

-- FK를 사람이 읽을 수 있는 고정 이름으로 생성
ALTER TABLE `order_items`
    ADD CONSTRAINT `fk_order_items_custom_option_id`
        FOREIGN KEY (`custom_option_id`) REFERENCES `custom_options` (`id`);

-- =========================
-- report 테이블: status 컬럼 안전 추가/수정
-- =========================

-- status 컬럼 없으면 추가
SET @col_exists := (
  SELECT COUNT(*)
  FROM INFORMATION_SCHEMA.COLUMNS
  WHERE TABLE_SCHEMA = DATABASE()
    AND TABLE_NAME = 'report'
    AND COLUMN_NAME = 'status'
);

SET @sql := IF(
  @col_exists = 0,
  "ALTER TABLE `report` ADD COLUMN `status` ENUM('PENDING','COMPLETED') NOT NULL DEFAULT 'PENDING'",
  "SELECT 1"
);

PREPARE stmt FROM @sql;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;

-- answer 컬럼 없으면 추가
SET @col_exists := (
  SELECT COUNT(*)
  FROM INFORMATION_SCHEMA.COLUMNS
  WHERE TABLE_SCHEMA = DATABASE()
    AND TABLE_NAME = 'report'
    AND COLUMN_NAME = 'answer'
);

SET @sql := IF(
  @col_exists = 0,
  "ALTER TABLE `report` ADD COLUMN `answer` TEXT NULL",
  "SELECT 1"
);

PREPARE stmt FROM @sql;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;

-- processed_at 컬럼 없으면 추가
SET @col_exists := (
  SELECT COUNT(*)
  FROM INFORMATION_SCHEMA.COLUMNS
  WHERE TABLE_SCHEMA = DATABASE()
    AND TABLE_NAME = 'report'
    AND COLUMN_NAME = 'processed_at'
);

SET @sql := IF(
  @col_exists = 0,
  "ALTER TABLE `report` ADD COLUMN `processed_at` DATETIME(6) NULL",
  "SELECT 1"
);

PREPARE stmt FROM @sql;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;

-- status 컬럼이 존재하면 타입/기본값 보정 (이미 있으면 MODIFY)
SET @col_exists := (
  SELECT COUNT(*)
  FROM INFORMATION_SCHEMA.COLUMNS
  WHERE TABLE_SCHEMA = DATABASE()
    AND TABLE_NAME = 'report'
    AND COLUMN_NAME = 'status'
);

SET @sql := IF(
  @col_exists = 1,
  "ALTER TABLE `report` MODIFY COLUMN `status` ENUM('PENDING','COMPLETED') NOT NULL DEFAULT 'PENDING'",
  "SELECT 1"
);

PREPARE stmt FROM @sql;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;

SET FOREIGN_KEY_CHECKS = 1;
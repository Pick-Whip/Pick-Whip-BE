-- V1_1_0__refactor_custom_options.sql
-- 리팩토링: custom_id -> custom_option_id 명칭 변경 및 불필요 컬럼 삭제

SET FOREIGN_KEY_CHECKS = 0;

-- 1. [custom_options] 색상 코드 NULL 허용으로 변경
ALTER TABLE `custom_options`
    MODIFY COLUMN `color_rgb_code` varchar(7) NULL;

-- 2. [design_options] custom_id -> custom_option_id 변경
ALTER TABLE `design_options` DROP FOREIGN KEY `FKfrd11hdres0ogmmgh2hinkcwl`;

ALTER TABLE `design_options`
    CHANGE COLUMN `custom_id` `custom_option_id` bigint NOT NULL;

ALTER TABLE `design_options`
    ADD CONSTRAINT `FK9a1hobbc3scxp4xa9ffvq3c3k`
        FOREIGN KEY (`custom_option_id`) REFERENCES `custom_options` (`id`);

-- 3. [order_draft_items] 중복 컬럼 custom_id 삭제
ALTER TABLE `order_draft_items` DROP FOREIGN KEY `FKe5uxjtwqc3flvx7tx0j6s46pu`;
ALTER TABLE `order_draft_items` DROP COLUMN `custom_id`;

-- 4. [order_items] custom_id -> custom_option_id 변경 및 색상 NULL 허용
ALTER TABLE `order_items` DROP FOREIGN KEY `FKgg6yuxouh7oxf5sd1702loke6`;

ALTER TABLE `order_items`
    CHANGE COLUMN `custom_id` `custom_option_id` bigint NOT NULL;

ALTER TABLE `order_items`
    MODIFY COLUMN `color_rgb_code` varchar(7) NULL;

ALTER TABLE `order_items`
    ADD CONSTRAINT `FKot639rciihur6tlkrm25cf6s0`
        FOREIGN KEY (`custom_option_id`) REFERENCES `custom_options` (`id`);

ALTER TABLE `report` ADD COLUMN `answer` text NULL;

SET FOREIGN_KEY_CHECKS = 1;
-- V2__add_popular_cake_ranking.sql
-- 홈 인기 케이크 Top5 집계 테이블
-- 최근 14일 결제 DONE 기준 Top5 결과를 저장 (스케줄러가 매일 12:00(KST)에 갱신)

SET NAMES utf8mb4 COLLATE utf8mb4_unicode_ci;

--테이블 생성 (없을 때만)
CREATE TABLE IF NOT EXISTS popular_cake_rankings (
                                                     `rank` INT NOT NULL COMMENT '순위(1~5)',
                                                     `design_id` BIGINT NOT NULL COMMENT 'design_gallery.id',
                                                     `shop_id` BIGINT NOT NULL COMMENT 'shops.shop_id',
                                                     `order_count` BIGINT NOT NULL COMMENT '최근 14일 결제 DONE 주문 건수',
                                                     `window_start` DATETIME(6) NULL COMMENT '집계 시작 시각',
    `window_end` DATETIME(6) NULL COMMENT '집계 종료 시각',
    `calculated_at` DATETIME(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6) COMMENT '집계 수행 시각',
    PRIMARY KEY (`rank`)
    ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- 컬럼 멱등 추가
SET @c := (SELECT COUNT(*) FROM information_schema.columns
           WHERE table_schema = DATABASE() AND table_name = 'popular_cake_rankings' AND column_name = 'design_id');
SET @sql := IF(@c = 0, 'ALTER TABLE popular_cake_rankings ADD COLUMN design_id BIGINT NOT NULL COMMENT ''design_gallery.id''', 'SELECT 1');
PREPARE s FROM @sql; EXECUTE s; DEALLOCATE PREPARE s;

-- shop_id
SET @c := (SELECT COUNT(*) FROM information_schema.columns
           WHERE table_schema = DATABASE() AND table_name = 'popular_cake_rankings' AND column_name = 'shop_id');
SET @sql := IF(@c = 0, 'ALTER TABLE popular_cake_rankings ADD COLUMN shop_id BIGINT NOT NULL COMMENT ''shops.shop_id''', 'SELECT 1');
PREPARE s FROM @sql; EXECUTE s; DEALLOCATE PREPARE s;

-- order_count
SET @c := (SELECT COUNT(*) FROM information_schema.columns
           WHERE table_schema = DATABASE() AND table_name = 'popular_cake_rankings' AND column_name = 'order_count');
SET @sql := IF(@c = 0, 'ALTER TABLE popular_cake_rankings ADD COLUMN order_count BIGINT NOT NULL COMMENT ''최근 14일 결제 DONE 주문 건수''', 'SELECT 1');
PREPARE s FROM @sql; EXECUTE s; DEALLOCATE PREPARE s;

-- window_start
SET @c := (SELECT COUNT(*) FROM information_schema.columns
           WHERE table_schema = DATABASE() AND table_name = 'popular_cake_rankings' AND column_name = 'window_start');
SET @sql := IF(@c = 0, 'ALTER TABLE popular_cake_rankings ADD COLUMN window_start DATETIME(6) NULL COMMENT ''집계 시작 시각''', 'SELECT 1');
PREPARE s FROM @sql; EXECUTE s; DEALLOCATE PREPARE s;

-- window_end
SET @c := (SELECT COUNT(*) FROM information_schema.columns
           WHERE table_schema = DATABASE() AND table_name = 'popular_cake_rankings' AND column_name = 'window_end');
SET @sql := IF(@c = 0, 'ALTER TABLE popular_cake_rankings ADD COLUMN window_end DATETIME(6) NULL COMMENT ''집계 종료 시각''', 'SELECT 1');
PREPARE s FROM @sql; EXECUTE s; DEALLOCATE PREPARE s;

-- calculated_at
SET @c := (SELECT COUNT(*) FROM information_schema.columns
           WHERE table_schema = DATABASE() AND table_name = 'popular_cake_rankings' AND column_name = 'calculated_at');
SET @sql := IF(@c = 0, 'ALTER TABLE popular_cake_rankings ADD COLUMN calculated_at DATETIME(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6) COMMENT ''집계 수행 시각''', 'SELECT 1');
PREPARE s FROM @sql; EXECUTE s; DEALLOCATE PREPARE s;

-- 인덱스 멱등 추가
SET @idx := (
    SELECT COUNT(*)
    FROM information_schema.statistics
    WHERE table_schema = DATABASE()
      AND table_name = 'popular_cake_rankings'
      AND index_name = 'idx_popular_cake_rankings_design_id'
);
SET @sql := IF(@idx = 0,
    'CREATE INDEX idx_popular_cake_rankings_design_id ON popular_cake_rankings (design_id)',
    'SELECT 1'
);
PREPARE s FROM @sql; EXECUTE s; DEALLOCATE PREPARE s;

SET @idx := (
    SELECT COUNT(*)
    FROM information_schema.statistics
    WHERE table_schema = DATABASE()
      AND table_name = 'popular_cake_rankings'
      AND index_name = 'idx_popular_cake_rankings_shop_id'
);
SET @sql := IF(@idx = 0,
    'CREATE INDEX idx_popular_cake_rankings_shop_id ON popular_cake_rankings (shop_id)',
    'SELECT 1'
);
PREPARE s FROM @sql; EXECUTE s; DEALLOCATE PREPARE s;

-- FK 멱등 추가
-- FK -> design_gallery(id)
SET @fk := (
    SELECT COUNT(*)
    FROM information_schema.referential_constraints
    WHERE constraint_schema = DATABASE()
      AND table_name = 'popular_cake_rankings'
      AND constraint_name = 'fk_popular_cake_rankings_design'
);
SET @sql := IF(@fk = 0,
    'ALTER TABLE popular_cake_rankings
        ADD CONSTRAINT fk_popular_cake_rankings_design
        FOREIGN KEY (design_id) REFERENCES design_gallery(id)
        ON DELETE CASCADE',
    'SELECT 1'
);
PREPARE s FROM @sql; EXECUTE s; DEALLOCATE PREPARE s;

-- FK -> shops(shop_id)
SET @fk := (
    SELECT COUNT(*)
    FROM information_schema.referential_constraints
    WHERE constraint_schema = DATABASE()
      AND table_name = 'popular_cake_rankings'
      AND constraint_name = 'fk_popular_cake_rankings_shop'
);
SET @sql := IF(@fk = 0,
    'ALTER TABLE popular_cake_rankings
        ADD CONSTRAINT fk_popular_cake_rankings_shop
        FOREIGN KEY (shop_id) REFERENCES shops(shop_id)
        ON DELETE CASCADE',
    'SELECT 1'
);
PREPARE s FROM @sql; EXECUTE s; DEALLOCATE PREPARE s;
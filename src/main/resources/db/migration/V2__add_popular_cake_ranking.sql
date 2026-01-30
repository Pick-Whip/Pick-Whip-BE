-- V2__add_popular_cake_ranking.sql
-- 홈 인기 케이크 Top5 집계 테이블
-- Flyway는 버전별로 한 번만 실행되므로 멱등성 검사 로직을 제거하고 단순화함

SET NAMES utf8mb4 COLLATE utf8mb4_unicode_ci;

CREATE TABLE popular_cake_rankings (
                                       `ranking`       INT NOT NULL COMMENT '순위(1~5)',  -- 수정됨: rank -> ranking
                                       `design_id`     BIGINT NOT NULL COMMENT 'design_gallery.id',
                                       `shop_id`       BIGINT NOT NULL COMMENT 'shops.shop_id',
                                       `order_count`   BIGINT NOT NULL COMMENT '최근 14일 결제 DONE 주문 건수',
                                       `window_start`  DATETIME(6) NULL COMMENT '집계 시작 시각',
                                       `window_end`    DATETIME(6) NULL COMMENT '집계 종료 시각',
                                       `calculated_at` DATETIME(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6) COMMENT '집계 수행 시각',
                                       PRIMARY KEY (`ranking`),
                                       CONSTRAINT fk_popular_cake_rankings_design FOREIGN KEY (design_id) REFERENCES design_gallery (id) ON DELETE CASCADE,
                                       CONSTRAINT fk_popular_cake_rankings_shop FOREIGN KEY (shop_id) REFERENCES shops (shop_id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- 인덱스 생성
CREATE INDEX idx_popular_cake_rankings_design_id ON popular_cake_rankings (design_id);
CREATE INDEX idx_popular_cake_rankings_shop_id ON popular_cake_rankings (shop_id);
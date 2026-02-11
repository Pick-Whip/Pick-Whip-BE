-- V20__create_popular_design_rankings.sql
-- 인기 디자인 도안 Top4 집계 테이블 생성
-- V2(popular_cake_rankings)와는 다르게 Surrogate Key(id)를 PK로 사용하며,
-- 결제 완료(PAID)된 주문 건수 기준의 디자인 랭킹을 저장합니다.

SET NAMES utf8mb4 COLLATE utf8mb4_unicode_ci;

CREATE TABLE popular_design_rankings (
                                         `id`            BIGINT NOT NULL AUTO_INCREMENT,
                                         `ranking`       INT NOT NULL COMMENT '순위(1~4)',
                                         `design_id`     BIGINT NOT NULL COMMENT 'design_gallery.id 참조',
                                         `shop_id`       BIGINT NOT NULL COMMENT 'shops.shop_id 참조',
                                         `order_count`   BIGINT NOT NULL COMMENT '집계된 주문 수 (최근 14일, 결제완료 기준)',
                                         `calculated_at` DATETIME(6) NOT NULL COMMENT '집계 수행 시각',
                                         PRIMARY KEY (`id`),
    -- [리뷰 반영] 순위 중복 방지를 위한 유니크 제약 조건
                                         UNIQUE KEY `uk_popular_design_rankings_ranking` (`ranking`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Foreign Keys 설정 (Cascade Delete 적용)
ALTER TABLE popular_design_rankings
    ADD CONSTRAINT fk_popular_design_rankings_design
        FOREIGN KEY (design_id) REFERENCES design_gallery (id) ON DELETE CASCADE;

ALTER TABLE popular_design_rankings
    ADD CONSTRAINT fk_popular_design_rankings_shop
        FOREIGN KEY (shop_id) REFERENCES shops (shop_id) ON DELETE CASCADE;

-- [성능 최적화] FK 인덱스 추가 (Deadlock 방지 및 조회 성능 향상)
CREATE INDEX idx_popular_design_rankings_design_id ON popular_design_rankings (design_id);
CREATE INDEX idx_popular_design_rankings_shop_id ON popular_design_rankings (shop_id);
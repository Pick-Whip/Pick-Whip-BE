-- V20__create_popular_design_rankings.sql
-- 인기 디자인 도안 Top4 집계 테이블 생성
-- V2(popular_cake_rankings)와는 다르게 Surrogate Key(id)를 PK로 사용하며,
-- 결제 완료(DONE)된 주문 건수 기준의 디자인 랭킹을 저장합니다.

SET NAMES utf8mb4 COLLATE utf8mb4_unicode_ci;

CREATE TABLE popular_design_rankings (
                                         `id`            BIGINT NOT NULL AUTO_INCREMENT,
                                         `ranking`       INT NOT NULL COMMENT '순위(1~4)',
                                         `design_id`     BIGINT NOT NULL COMMENT 'design_gallery.id 참조',
                                         `shop_id`       BIGINT NOT NULL COMMENT 'shops.shop_id 참조',
                                         `order_count`   BIGINT NOT NULL COMMENT '집계된 주문 수 (최근 14일, 결제완료 기준)',
                                         `calculated_at` DATETIME(6) NOT NULL COMMENT '집계 수행 시각',
                                         PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Foreign Keys 설정 (Cascade Delete 적용: 원본 디자인/가게 삭제 시 랭킹에서도 삭제)
-- 제약조건 이름은 기존 규칙(fk_테이블명_참조테이블명)을 따름
ALTER TABLE popular_design_rankings
    ADD CONSTRAINT fk_popular_design_rankings_design
        FOREIGN KEY (design_id) REFERENCES design_gallery (id) ON DELETE CASCADE;

ALTER TABLE popular_design_rankings
    ADD CONSTRAINT fk_popular_design_rankings_shop
        FOREIGN KEY (shop_id) REFERENCES shops (shop_id) ON DELETE CASCADE;

-- 성능 향상을 위한 인덱스 (순위 조회용)
CREATE INDEX idx_popular_design_rankings_ranking ON popular_design_rankings (ranking);
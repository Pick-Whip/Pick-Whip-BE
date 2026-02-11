-- shops 테이블에 district 컬럼 추가 (길이 20)
ALTER TABLE shops ADD COLUMN district VARCHAR(20) COMMENT '행정구역(구)';

-- 검색 성능 향상을 위한 인덱스 추가
CREATE INDEX idx_shop_district ON shops(district);
-- 1) FK가 사용할 일반 인덱스 먼저 추가
CREATE INDEX idx_review_order_id ON review (order_id);

-- 2) 기존 유니크 인덱스 드롭
ALTER TABLE review DROP INDEX uc_review_order;

-- 3) 삭제되지 않은 리뷰에만 order_id가 살아있는 generated column 추가
ALTER TABLE review
    ADD COLUMN active_order_id BIGINT
        AS (CASE WHEN deleted_at IS NULL THEN order_id ELSE NULL END) STORED;

-- 4) 활성 리뷰에 대해서만 유니크 강제
ALTER TABLE review
    ADD UNIQUE KEY uc_review_active_order (active_order_id);

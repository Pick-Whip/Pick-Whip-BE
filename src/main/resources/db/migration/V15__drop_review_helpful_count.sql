-- V2__drop_review_helpful_count.sql
-- Review 테이블에서 사용하지 않는 helpful_count 컬럼 삭제

ALTER TABLE review DROP COLUMN helpful_count;
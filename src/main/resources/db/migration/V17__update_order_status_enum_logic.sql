-- V17__Update_order_status_enum_logic.sql
-- 주문 상태(Status) Enum 변경에 따른 스키마 및 데이터 마이그레이션
-- IMPOSSIBLE -> CANCELED_BY_SHOP / PAYMENT_FAILED 분리

-- -----------------------------------------------------------------------------
-- 1. [Orders 테이블] ENUM 확장 (기존 데이터 보존을 위해 IMPOSSIBLE 유지 + 새 값 추가)
-- -----------------------------------------------------------------------------
ALTER TABLE orders
    MODIFY COLUMN status ENUM(
    'CONFIRM_WAIT',
    'PROD_CONFIRM',
    'MAKING',
    'PICKUP_WAIT',
    'COMPLETED',
    'IMPOSSIBLE',      -- 데이터 이동용 임시 유지
    'PAYMENT_WAIT',    -- [New]
    'CANCELED_BY_SHOP',-- [New]
    'PAYMENT_FAILED'   -- [New]
    ) COLLATE utf8mb4_unicode_ci NOT NULL;


-- -----------------------------------------------------------------------------
-- 2. [Orders 테이블] 데이터 마이그레이션
-- -----------------------------------------------------------------------------
-- Case A: 거절 사유가 있으면 -> 'CANCELED_BY_SHOP' (사장님 거절)
UPDATE orders
SET status = 'CANCELED_BY_SHOP'
WHERE status = 'IMPOSSIBLE' AND rejection_reason IS NOT NULL;

-- Case B: 거절 사유가 없으면 -> 'PAYMENT_FAILED' (결제 실패 등)
UPDATE orders
SET status = 'PAYMENT_FAILED'
WHERE status = 'IMPOSSIBLE' AND rejection_reason IS NULL;


-- -----------------------------------------------------------------------------
-- 3. [OrderHistories 테이블] 데이터 마이그레이션 (현재 VARCHAR이므로 바로 UPDATE 가능)
-- -----------------------------------------------------------------------------
-- 히스토리는 거절 사유 컬럼이 없으므로 일괄적으로 'CANCELED_BY_SHOP'으로 변경하여 타임라인 에러 방지
UPDATE order_histories
SET status = 'CANCELED_BY_SHOP'
WHERE status = 'IMPOSSIBLE';


-- -----------------------------------------------------------------------------
-- 4. [Orders 테이블] ENUM 축소 (IMPOSSIBLE 제거 및 순서 정리)
-- -----------------------------------------------------------------------------
ALTER TABLE orders
    MODIFY COLUMN status ENUM(
    'CONFIRM_WAIT',
    'PAYMENT_WAIT',      -- 2단계
    'CANCELED_BY_SHOP',  -- 2단계 (예외)
    'PAYMENT_FAILED',    -- 2단계 (예외)
    'PROD_CONFIRM',      -- 3단계
    'MAKING',            -- 4단계
    'PICKUP_WAIT',       -- 5단계
    'COMPLETED'          -- 완료
    ) COLLATE utf8mb4_unicode_ci NOT NULL;


-- -----------------------------------------------------------------------------
-- 5. [OrderHistories 테이블] 컬럼 타입 변경 (VARCHAR -> ENUM)
-- -----------------------------------------------------------------------------
-- orders 테이블과 정합성을 맞추기 위해 VARCHAR를 ENUM으로 변경합니다.
-- 위에서 데이터를 이미 다 바꿨으므로 안전하게 변환됩니다.
ALTER TABLE order_histories
    MODIFY COLUMN status ENUM(
    'CONFIRM_WAIT',
    'PAYMENT_WAIT',
    'CANCELED_BY_SHOP',
    'PAYMENT_FAILED',
    'PROD_CONFIRM',
    'MAKING',
    'PICKUP_WAIT',
    'COMPLETED'
    ) COLLATE utf8mb4_unicode_ci NOT NULL;
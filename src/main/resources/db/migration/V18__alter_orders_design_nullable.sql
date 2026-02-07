-- V18__alter_orders_design_nullable.sql

ALTER TABLE orders MODIFY COLUMN design_id BIGINT NULL;
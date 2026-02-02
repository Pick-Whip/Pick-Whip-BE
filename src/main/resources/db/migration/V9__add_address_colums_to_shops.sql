ALTER TABLE shops
    ADD COLUMN address_name VARCHAR(255),       -- 지번 주소
    ADD COLUMN road_address_name VARCHAR(255),  -- 도로명 주소
    ADD COLUMN region1_depth_name VARCHAR(50),  -- 시/도
    ADD COLUMN region2_depth_name VARCHAR(50),  -- 구/군
    ADD COLUMN region3_depth_name VARCHAR(50);  -- 동/면/리
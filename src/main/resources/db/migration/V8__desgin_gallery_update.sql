ALTER TABLE design_gallery
    ADD COLUMN lettering_text VARCHAR(30),
    ADD COLUMN lettering_line_count VARCHAR(255),
    ADD COLUMN lettering_alignment VARCHAR(255),
    ADD COLUMN shop_cake_size_id BIGINT;


ALTER TABLE design_gallery
    MODIFY COLUMN shop_cake_size_id BIGINT NOT NULL,
    ADD CONSTRAINT fk_design_gallery_shop_cake_size
    FOREIGN KEY (shop_cake_size_id) REFERENCES shop_cake_sizes (size_id);

ALTER TABLE design_options
    ADD COLUMN position_x DOUBLE,
    ADD COLUMN position_y DOUBLE;
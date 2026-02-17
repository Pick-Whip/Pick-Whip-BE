ALTER TABLE design_gallery
    ADD COLUMN lettering_color VARCHAR(30) COMMENT '레터링 색상 코드';

ALTER TABLE orders_drafts
    ADD COLUMN lettering_color VARCHAR(30) COMMENT '레터링 색상 코드';

CREATE TABLE IF NOT EXISTS avail_option (
                                            avail_option_id BIGINT AUTO_INCREMENT PRIMARY KEY,
                                            design_id BIGINT NOT NULL,
                                            custom_option_id BIGINT NOT NULL,

                                            CONSTRAINT fk_avail_option_design
                                            FOREIGN KEY (design_id) REFERENCES design_gallery (id) ON DELETE CASCADE,

    CONSTRAINT fk_avail_option_custom
    FOREIGN KEY (custom_option_id) REFERENCES custom_options (id) ON DELETE CASCADE
    ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
CREATE TABLE design_gallery_keywords (
                                         design_gallery_id BIGINT NOT NULL,  -- 부모(DesignGallery)의 ID
                                         keyword VARCHAR(255)                -- 실제 키워드 내용
) ENGINE=InnoDB;

ALTER TABLE design_gallery_keywords
    ADD CONSTRAINT fk_design_gallery_keywords_design_gallery
        FOREIGN KEY (design_gallery_id) REFERENCES design_gallery (id);
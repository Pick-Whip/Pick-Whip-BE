/* 1. 테이블 생성 */
CREATE TABLE favorite_designs (
                                  id BIGINT NOT NULL AUTO_INCREMENT,
                                  user_id BIGINT NOT NULL,
                                  design_id BIGINT NOT NULL,
                                  created_at DATETIME(6),
                                  PRIMARY KEY (id)
) ENGINE=InnoDB;

ALTER TABLE favorite_designs
    ADD CONSTRAINT uk_favorite_design_user UNIQUE (user_id, design_id);

ALTER TABLE favorite_designs
    ADD CONSTRAINT fk_favorite_designs_user
        FOREIGN KEY (user_id) REFERENCES users (user_id);

ALTER TABLE favorite_designs
    ADD CONSTRAINT fk_favorite_designs_design_gallery
        FOREIGN KEY (design_id) REFERENCES design_gallery (id);

CREATE TABLE design_gallery_purposes (
                                         design_gallery_id BIGINT NOT NULL,
                                         purpose VARCHAR(50) NOT NULL,

                                         CONSTRAINT fk_design_gallery_purposes_gallery
                                             FOREIGN KEY (design_gallery_id)
                                                 REFERENCES design_gallery (id)
                                                 ON DELETE CASCADE,

                                         CONSTRAINT pk_design_gallery_purposes
                                             PRIMARY KEY (design_gallery_id, purpose)
);

ALTER TABLE design_gallery
    ADD COLUMN lettering_text VARCHAR(30),
ADD COLUMN lettering_line_count VARCHAR(255),
ADD COLUMN lettering_alignment VARCHAR(255);

ALTER TABLE design_options
    ADD COLUMN position_x double,
    ADD COLUMN position_y double;
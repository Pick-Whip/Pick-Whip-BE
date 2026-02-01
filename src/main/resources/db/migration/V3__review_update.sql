ALTER TABLE review
    ADD agreement BIT(1) NULL;

ALTER TABLE review
    ADD design_id BIGINT NULL;

ALTER TABLE review
    ADD order_id BIGINT NOT NULL;

ALTER TABLE review
    ADD shop_id BIGINT NOT NULL;

ALTER TABLE review
    ADD user_id BIGINT NOT NULL;

ALTER TABLE review_keyword
    ADD code VARCHAR(50) NOT NULL;

ALTER TABLE review_keyword
    ADD label VARCHAR(15) NOT NULL;

ALTER TABLE review_keyword
    ADD CONSTRAINT uc_review_keyword_code UNIQUE (code);

ALTER TABLE review
    ADD CONSTRAINT uc_review_order UNIQUE (order_id);

ALTER TABLE review
    ADD CONSTRAINT FK_REVIEW_ON_DESIGN FOREIGN KEY (design_id) REFERENCES design_gallery (id);

ALTER TABLE review
    ADD CONSTRAINT FK_REVIEW_ON_ORDER FOREIGN KEY (order_id) REFERENCES orders (id);

ALTER TABLE review
    ADD CONSTRAINT FK_REVIEW_ON_SHOP FOREIGN KEY (shop_id) REFERENCES shops (shop_id);

ALTER TABLE review
    ADD CONSTRAINT FK_REVIEW_ON_USER FOREIGN KEY (user_id) REFERENCES users (user_id);

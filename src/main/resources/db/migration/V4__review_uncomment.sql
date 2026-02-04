ALTER TABLE review_reply
    ADD seller_id BIGINT NULL;

ALTER TABLE review_reply
    ADD CONSTRAINT FK_REVIEW_REPLY_ON_SELLER FOREIGN KEY (seller_id) REFERENCES users (user_id);

ALTER TABLE review_selected_keyword
    ADD deleted_at datetime NULL;

ALTER TABLE review_like
    ADD user_id BIGINT NULL;

ALTER TABLE review_like
    MODIFY user_id BIGINT NOT NULL;

ALTER TABLE review_like
    ADD CONSTRAINT uk_review_like_review_user UNIQUE (review_id, user_id);

ALTER TABLE review_like
    ADD CONSTRAINT FK_REVIEW_LIKE_ON_USER FOREIGN KEY (user_id) REFERENCES users (user_id);

ALTER TABLE design_gallery
    ADD shop_id BIGINT NULL;

ALTER TABLE design_gallery
    MODIFY shop_id BIGINT NOT NULL;

ALTER TABLE design_gallery
    ADD CONSTRAINT FK_DESIGN_GALLERY_ON_SHOP FOREIGN KEY (shop_id) REFERENCES shops (shop_id);

ALTER TABLE review_keyword
DROP COLUMN keyword;

ALTER TABLE review_selected_keyword
DROP COLUMN deleted_at;

ALTER TABLE review_keyword DROP INDEX uk_review_keyword_category_keyword;

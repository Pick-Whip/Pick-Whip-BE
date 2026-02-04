ALTER TABLE notification
    ADD kind VARCHAR(255) NOT NULL;

ALTER TABLE notification
    ADD target_id BIGINT NULL;

ALTER TABLE notification
    ADD user_id BIGINT NOT NULL;

CREATE INDEX idx_notification_user_id_id ON notification (user_id, id);

CREATE INDEX idx_notification_user_id_type_id ON notification (user_id, type, id);

ALTER TABLE notification
    ADD CONSTRAINT FK_NOTIFICATION_ON_USER FOREIGN KEY (user_id) REFERENCES users (user_id);
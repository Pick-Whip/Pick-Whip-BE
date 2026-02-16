CREATE TABLE refresh_token (
                               id BIGINT AUTO_INCREMENT PRIMARY KEY,
                               token VARCHAR(255) NOT NULL UNIQUE,
                               user_id BIGINT NOT NULL,
                               expiry_date DATETIME(6) NOT NULL,
                               created_at DATETIME(6) NOT NULL,
                               updated_at DATETIME(6) NOT NULL,
                               CONSTRAINT fk_refresh_token_user FOREIGN KEY (user_id) REFERENCES users(user_id) ON DELETE CASCADE
);

-- 검색 성능 향상을 위해 인덱스 추가
CREATE INDEX idx_refresh_token_value ON refresh_token(token);
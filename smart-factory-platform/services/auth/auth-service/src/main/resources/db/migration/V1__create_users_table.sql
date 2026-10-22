CREATE TABLE users (
                       id BIGINT NOT NULL AUTO_INCREMENT,
                       username VARCHAR(100) NOT NULL,
                       password VARCHAR(255) NOT NULL,
                       role VARCHAR(30) NOT NULL,
                       enabled BOOLEAN NOT NULL DEFAULT TRUE,
                       created_at TIMESTAMP NOT NULL,
                       updated_at TIMESTAMP NOT NULL,

                       PRIMARY KEY (id),

                       CONSTRAINT uk_users_username UNIQUE (username)
);
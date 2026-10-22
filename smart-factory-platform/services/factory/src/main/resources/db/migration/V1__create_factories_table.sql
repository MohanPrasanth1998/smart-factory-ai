CREATE TABLE factories (
                           id BIGINT NOT NULL AUTO_INCREMENT,
                           name VARCHAR(100) NOT NULL,
                           location VARCHAR(255),
                           status VARCHAR(30) NOT NULL,
                           created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
                           updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,

                           PRIMARY KEY (id)
);
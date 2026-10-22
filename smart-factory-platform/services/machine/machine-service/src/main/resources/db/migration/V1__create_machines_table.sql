CREATE TABLE machines (
                          id BIGINT NOT NULL AUTO_INCREMENT,
                          machine_code VARCHAR(20) NOT NULL,
                          name VARCHAR(100) NOT NULL,
                          type VARCHAR(100) NOT NULL,
                          factory_code VARCHAR(20) NOT NULL,
                          status VARCHAR(30) NOT NULL,
                          created_at TIMESTAMP NOT NULL,
                          updated_at TIMESTAMP NOT NULL,

                          PRIMARY KEY (id),

                          CONSTRAINT uk_machines_machine_code
                              UNIQUE (machine_code)
);
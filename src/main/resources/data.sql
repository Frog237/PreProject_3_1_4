CREATE TABLE IF NOT EXISTS roles (
                                     id BIGINT AUTO_INCREMENT PRIMARY KEY,
                                     name VARCHAR(50) NOT NULL UNIQUE
);

INSERT INTO roles (name)
VALUES ('ROLE_USER'), ('ROLE_ADMIN')
ON DUPLICATE KEY UPDATE name = VALUES(name);
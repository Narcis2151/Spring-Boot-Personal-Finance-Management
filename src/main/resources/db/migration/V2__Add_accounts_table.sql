DROP TABLE user;

CREATE TABLE user
(
    id           BIGINT AUTO_INCREMENT PRIMARY KEY,
    email        VARCHAR(255) NOT NULL UNIQUE,
    full_name    VARCHAR(100) NOT NULL,
    password     VARCHAR(255) NOT NULL
);

CREATE TABLE account
(
    id       BIGINT AUTO_INCREMENT PRIMARY KEY,
    name     VARCHAR(100)   NOT NULL,
    currency VARCHAR(3)     NOT NULL,
    balance  DECIMAL(10, 2) NOT NULL,
    user_id  BIGINT         NOT NULL,
    FOREIGN KEY (user_id) REFERENCES user (id)
)


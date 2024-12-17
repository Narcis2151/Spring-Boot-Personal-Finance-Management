CREATE TABLE category
(
    id           BIGINT AUTO_INCREMENT PRIMARY KEY,
    name         VARCHAR(255) NOT NULL,
    user_id      BIGINT NOT NULL,
    FOREIGN KEY (user_id) REFERENCES user (id)
);

ALTER table transaction
ADD COLUMN category_id BIGINT;

ALTER table transaction
ADD FOREIGN KEY (category_id) REFERENCES category (id);
CREATE TABLE budget
(
    id               BIGINT AUTO_INCREMENT PRIMARY KEY,
    amount_available DECIMAL(10, 2) NOT NULL,
    start_date       DATE           NOT NULL,
    end_date         DATE           NOT NULL,
    category_id      BIGINT         NOT NULL,
    user_id          BIGINT         NOT NULL,
    FOREIGN KEY (category_id) REFERENCES category (id),
    FOREIGN KEY (user_id) REFERENCES user (id)
);
CREATE TABLE transaction
(
    id           BIGINT AUTO_INCREMENT PRIMARY KEY,
    debit_credit ENUM ('DEBIT', 'CREDIT') NOT NULL,
    amount       DECIMAL(10, 2)           NOT NULL,
    party        VARCHAR(100)             NOT NULL,
    date_posted  TIMESTAMP                NOT NULL DEFAULT CURRENT_TIMESTAMP,
    user_id      BIGINT                      NOT NULL,
    account_id   BIGINT                      NOT NULL,
    FOREIGN KEY (user_id) REFERENCES user (id),
    FOREIGN KEY (account_id) REFERENCES account (id)
);
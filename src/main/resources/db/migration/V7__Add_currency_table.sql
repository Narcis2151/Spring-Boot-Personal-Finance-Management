CREATE TABLE currency
(
    name VARCHAR(255) PRIMARY KEY,
    amount_in_ron DECIMAL(10, 2) NOT NULL
);

INSERT INTO currency (name, amount_in_ron) VALUES ('RON', 1);
INSERT INTO currency (name, amount_in_ron) VALUES ('EUR', 4.87);
INSERT INTO currency (name, amount_in_ron) VALUES ('USD', 4.11);

ALTER TABLE account
MODIFY COLUMN currency VARCHAR(3);

ALTER TABLE account
ADD CONSTRAINT fk_account_currency FOREIGN KEY (currency) REFERENCES currency (name);
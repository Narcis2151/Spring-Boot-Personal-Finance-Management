CREATE TABLE users
(
    id        SERIAL PRIMARY KEY,
    email     VARCHAR(255) NOT NULL UNIQUE,
    full_name VARCHAR(100) NOT NULL,
    password  VARCHAR(255) NOT NULL
);

CREATE TABLE currency
(
    name          VARCHAR(3) PRIMARY KEY,
    amount_in_ron DECIMAL(10, 2) NOT NULL
);

CREATE TABLE account
(
    id       SERIAL PRIMARY KEY,
    name     VARCHAR(100)   NOT NULL,
    currency VARCHAR(3)     NOT NULL,
    balance  DECIMAL(10, 2) NOT NULL,
    user_id  BIGINT         NOT NULL,
    FOREIGN KEY (user_id) REFERENCES users (id),
    FOREIGN KEY (currency) REFERENCES currency (name)
);

CREATE TABLE category
(
    id      SERIAL PRIMARY KEY,
    name    VARCHAR(255) NOT NULL,
    user_id BIGINT       NOT NULL,
    FOREIGN KEY (user_id) REFERENCES users (id)
);

CREATE TYPE debit_credit_enum AS ENUM ('DEBIT', 'CREDIT');
CREATE TABLE transaction
(
    id           SERIAL PRIMARY KEY,
    debit_credit debit_credit_enum NOT NULL,
    amount       DECIMAL(10, 2)    NOT NULL,
    party        VARCHAR(100)      NOT NULL,
    date_posted  TIMESTAMP         NOT NULL DEFAULT CURRENT_TIMESTAMP,
    user_id      BIGINT            NOT NULL,
    account_id   BIGINT            NOT NULL,
    category_id  BIGINT,
    FOREIGN KEY (user_id) REFERENCES users (id),
    FOREIGN KEY (account_id) REFERENCES account (id),
    FOREIGN KEY (category_id) REFERENCES category (id)
);

CREATE TABLE budget
(
    id               SERIAL PRIMARY KEY,
    amount_available DECIMAL(10, 2) NOT NULL,
    start_date       DATE           NOT NULL,
    end_date         DATE           NOT NULL,
    category_id      BIGINT         NOT NULL,
    user_id          BIGINT         NOT NULL,
    FOREIGN KEY (category_id) REFERENCES category (id),
    FOREIGN KEY (user_id) REFERENCES users (id)
);
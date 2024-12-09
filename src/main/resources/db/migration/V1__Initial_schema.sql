CREATE TABLE user
(
    id           INT AUTO_INCREMENT PRIMARY KEY,
    email        VARCHAR(255) NOT NULL UNIQUE,
    full_name    VARCHAR(100) NOT NULL,
    password     VARCHAR(255) NOT NULL
);


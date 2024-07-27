CREATE TABLE candidates
  (
      id              BIGINT AUTO_INCREMENT PRIMARY KEY,
      name            VARCHAR(100)   NOT NULL,
      email           VARCHAR(100)   NOT NULL UNIQUE,
      gender          VARCHAR(10)    NOT NULL,
      salary_expected DECIMAL(10, 2) NOT NULL,
      created_at      TIMESTAMP DEFAULT CURRENT_TIMESTAMP
  );

CREATE TABLE users
(
    id              BIGINT AUTO_INCREMENT PRIMARY KEY,
    username            VARCHAR(100)   NOT NULL,
    password           VARCHAR(100)   NOT NULL

);
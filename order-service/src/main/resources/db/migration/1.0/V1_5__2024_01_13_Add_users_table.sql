--date: 2024-01-13
--author: vstebletsov

CREATE TABLE IF NOT EXISTS users (
   id BIGSERIAL PRIMARY KEY,
   username VARCHAR(100) UNIQUE NOT NULL,
   password VARCHAR(100) NOT NULL,
   role VARCHAR(100),
   last_login  TIMESTAMP NOT NULL,
   active BOOLEAN NOT NULL
)
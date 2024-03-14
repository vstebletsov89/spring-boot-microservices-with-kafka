--date: 2024-03-14
--author: vstebletsov

CREATE TABLE IF NOT EXISTS orders (
     id BIGSERIAL PRIMARY KEY,
     user_id BIGINT REFERENCES users(id) ON DELETE CASCADE,
     state VARCHAR (100) NOT NULL,
     created_at TIMESTAMP NOT NULL
)
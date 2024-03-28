--date: 2024-03-16
--author: vstebletsov

CREATE TABLE IF NOT EXISTS orders (
     id BIGSERIAL PRIMARY KEY,
     user_id BIGINT REFERENCES users(id) ON DELETE CASCADE,
     order_number VARCHAR(36) NOT NULL,
     state VARCHAR (100) NOT NULL,
     created_at TIMESTAMP NOT NULL
)
--date: 2024-03-19
--author: vstebletsov

CREATE TABLE IF NOT EXISTS payments (
     id BIGSERIAL PRIMARY KEY,
     user_id BIGINT,
     order_number VARCHAR(36) NOT NULL,
     type VARCHAR (100) NOT NULL,
     amount DECIMAL(10, 2),
     created_at TIMESTAMP NOT NULL,
     updated_at TIMESTAMP NOT NULL
)
--date: 2024-03-16
--author: vstebletsov

CREATE TABLE IF NOT EXISTS orders (
     id BIGSERIAL PRIMARY KEY,
     customer_number VARCHAR(36) NOT NULL,
     state VARCHAR (100) NOT NULL,
     created_at TIMESTAMP NOT NULL
)
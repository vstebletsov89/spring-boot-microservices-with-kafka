--date: 2024-03-14
--author: vstebletsov

CREATE TABLE IF NOT EXISTS products (
     id BIGSERIAL PRIMARY KEY,
     name VARCHAR(255),
     description VARCHAR(500),
     quantity INTEGER,
     price DECIMAL(10, 2)
)
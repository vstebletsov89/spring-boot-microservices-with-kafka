--date: 2024-03-14
--author: vstebletsov

CREATE TABLE IF NOT EXISTS items (
     id BIGSERIAL PRIMARY KEY,
     name VARCHAR(255),
     category VARCHAR(100),
     quantity INTEGER,
     price DECIMAL(10, 2)
)
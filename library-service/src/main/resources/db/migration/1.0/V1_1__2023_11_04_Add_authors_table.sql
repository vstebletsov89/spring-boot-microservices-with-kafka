--date: 2023-04-11
--author: vstebletsov

CREATE TABLE IF NOT EXISTS authors (
     id BIGSERIAL PRIMARY KEY,
     full_name VARCHAR(255)
)
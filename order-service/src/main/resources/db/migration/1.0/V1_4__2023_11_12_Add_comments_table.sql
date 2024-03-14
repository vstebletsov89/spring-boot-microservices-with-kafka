--date: 2023-12-11
--author: vstebletsov

CREATE TABLE IF NOT EXISTS comments (
   id BIGSERIAL PRIMARY KEY,
   text VARCHAR(500),
   book_id BIGINT REFERENCES books(id) ON DELETE CASCADE
)
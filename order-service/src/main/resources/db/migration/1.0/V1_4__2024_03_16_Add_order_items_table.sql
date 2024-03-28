--date: 2024-03-16
--author: vstebletsov

CREATE TABLE IF NOT EXISTS order_items (
     id BIGSERIAL PRIMARY KEY,
     order_id BIGINT REFERENCES orders(id) ON DELETE CASCADE,
     item_id BIGINT REFERENCES items(id) ON DELETE CASCADE
)
INSERT INTO users(username, password, role, last_login, active)
VALUES ('user', '$2a$10$2QijsT.ISCurZV5vP8oHk.6L9Rg2VprJfF0cfM.J.cRfPILsVO5ZG', 'USER', CURRENT_TIMESTAMP, TRUE),
('admin', '$2a$10$2QijsT.ISCurZV5vP8oHk.6L9Rg2VprJfF0cfM.J.cRfPILsVO5ZG', 'ADMIN', CURRENT_TIMESTAMP, TRUE),
('guest', '$2a$10$2QijsT.ISCurZV5vP8oHk.6L9Rg2VprJfF0cfM.J.cRfPILsVO5ZG', 'GUEST', CURRENT_TIMESTAMP, TRUE);


-- 初始化测试数据
-- 注意：密码 "123456" 的哈希值是 -123456
INSERT INTO users (username, email, full_name, password, created_at, updated_at, role) VALUES 
('admin', 'admin@example.com', 'Administrator', '-123456', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'ADMIN'),
('user1', 'user1@example.com', 'User One', '-123456', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'CONSUMER'),
('user2', 'user2@example.com', 'User Two', '-123456', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'MERCHANT');

-- 检查和修复用户数据脚本
-- 确保用户数据存在且格式正确

-- 1. 检查当前用户数据
SELECT 'Current users in database:' as status;
SELECT id, username, email, role, created_at FROM users ORDER BY id;

-- 2. 检查是否有小写角色数据
SELECT 'Checking for lowercase roles:' as status;
SELECT COUNT(*) as lowercase_count FROM users WHERE role IN ('admin', 'merchant', 'consumer');

-- 3. 如果发现小写角色，修复它们
UPDATE users SET role = 'ADMIN' WHERE role = 'admin';
UPDATE users SET role = 'MERCHANT' WHERE role = 'merchant';
UPDATE users SET role = 'CONSUMER' WHERE role = 'consumer';

-- 4. 如果用户表为空，插入测试数据
INSERT IGNORE INTO users (username, email, full_name, password, created_at, updated_at, role) VALUES 
('admin', 'admin@foodstreet.com', '系统管理员', '-123456', NOW(), NOW(), 'ADMIN'),
('merchant1', 'merchant1@foodstreet.com', '商户1', '-123456', NOW(), NOW(), 'MERCHANT'),
('consumer1', 'consumer1@foodstreet.com', '消费者1', '-123456', NOW(), NOW(), 'CONSUMER');

-- 5. 显示修复后的用户数据
SELECT 'Users after fix:' as status;
SELECT id, username, email, role, created_at FROM users ORDER BY id;

-- 6. 验证用户数据
SELECT 'Verification:' as status;
SELECT 
    COUNT(*) as total_users,
    SUM(CASE WHEN role = 'ADMIN' THEN 1 ELSE 0 END) as admin_count,
    SUM(CASE WHEN role = 'MERCHANT' THEN 1 ELSE 0 END) as merchant_count,
    SUM(CASE WHEN role = 'CONSUMER' THEN 1 ELSE 0 END) as consumer_count
FROM users;

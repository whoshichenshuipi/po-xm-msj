-- 修复用户角色数据
-- 将小写的角色名称更新为大写

-- 更新用户角色为正确的大写格式
UPDATE users SET role = 'ADMIN' WHERE role = 'admin';
UPDATE users SET role = 'MERCHANT' WHERE role = 'merchant';
UPDATE users SET role = 'CONSUMER' WHERE role = 'consumer';

-- 显示修复后的用户数据
SELECT id, username, email, role FROM users ORDER BY id;

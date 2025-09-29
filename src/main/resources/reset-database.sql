-- 重置数据库脚本
-- 清理所有数据并重新插入正确的测试数据

-- 清理现有数据（按依赖关系逆序删除）
DELETE FROM system_log;
DELETE FROM notification;
DELETE FROM cultural_content;
DELETE FROM product;
DELETE FROM merchant_qualification;
DELETE FROM review;
DELETE FROM checkin;
DELETE FROM reservation;
DELETE FROM activity_verify;
DELETE FROM activity_signup;
DELETE FROM activity;
DELETE FROM revenue_record;
DELETE FROM traffic_stat;
DELETE FROM hygiene_check;
DELETE FROM venue;
DELETE FROM merchant;
DELETE FROM users;

-- 重置自增ID
ALTER TABLE users AUTO_INCREMENT = 1;
ALTER TABLE merchant AUTO_INCREMENT = 1;
ALTER TABLE venue AUTO_INCREMENT = 1;
ALTER TABLE product AUTO_INCREMENT = 1;
ALTER TABLE activity AUTO_INCREMENT = 1;
ALTER TABLE reservation AUTO_INCREMENT = 1;
ALTER TABLE review AUTO_INCREMENT = 1;
ALTER TABLE checkin AUTO_INCREMENT = 1;
ALTER TABLE cultural_content AUTO_INCREMENT = 1;
ALTER TABLE notification AUTO_INCREMENT = 1;
ALTER TABLE system_log AUTO_INCREMENT = 1;

-- 插入正确的用户数据（使用大写的角色名称）
INSERT INTO users (username, email, full_name, password, created_at, updated_at, role) VALUES 
-- 管理员
('admin', 'admin@foodstreet.com', '系统管理员', '-123456', '2024-01-01 08:00:00', '2024-01-01 08:00:00', 'ADMIN'),
('manager1', 'manager1@foodstreet.com', '运营经理', '-123456', '2024-01-01 08:30:00', '2024-01-01 08:30:00', 'ADMIN'),

-- 商户用户
('merchant1', 'zhanglaoshi@example.com', '张老师傅', '-123456', '2024-01-01 09:00:00', '2024-01-01 09:00:00', 'MERCHANT'),
('merchant2', 'wangfujia@example.com', '王福家', '-123456', '2024-01-01 09:30:00', '2024-01-01 09:30:00', 'MERCHANT'),
('merchant3', 'limingtang@example.com', '李明堂', '-123456', '2024-01-01 10:00:00', '2024-01-01 10:00:00', 'MERCHANT'),
('merchant4', 'chenshifu@example.com', '陈师傅', '-123456', '2024-01-01 10:30:00', '2024-01-01 10:30:00', 'MERCHANT'),
('merchant5', 'yanglaobao@example.com', '杨老包', '-123456', '2024-01-01 11:00:00', '2024-01-01 11:00:00', 'MERCHANT'),

-- 消费者用户
('consumer1', 'xiaoming@example.com', '小明', '-123456', '2024-01-01 12:00:00', '2024-01-01 12:00:00', 'CONSUMER'),
('consumer2', 'xiaohong@example.com', '小红', '-123456', '2024-01-01 12:30:00', '2024-01-01 12:30:00', 'CONSUMER'),
('consumer3', 'xiaolei@example.com', '小雷', '-123456', '2024-01-01 13:00:00', '2024-01-01 13:00:00', 'CONSUMER'),
('consumer4', 'xiaohua@example.com', '小花', '-123456', '2024-01-01 13:30:00', '2024-01-01 13:30:00', 'CONSUMER'),
('consumer5', 'xiaolin@example.com', '小林', '-123456', '2024-01-01 14:00:00', '2024-01-01 14:00:00', 'CONSUMER'),
('consumer6', 'xiaofang@example.com', '小芳', '-123456', '2024-01-01 14:30:00', '2024-01-01 14:30:00', 'CONSUMER'),
('consumer7', 'xiaocao@example.com', '小草', '-123456', '2024-01-01 15:00:00', '2024-01-01 15:00:00', 'CONSUMER'),
('consumer8', 'xiaojun@example.com', '小军', '-123456', '2024-01-01 15:30:00', '2024-01-01 15:30:00', 'CONSUMER');

-- 验证数据
SELECT 'Users inserted successfully' as status;
SELECT id, username, email, role FROM users ORDER BY id;

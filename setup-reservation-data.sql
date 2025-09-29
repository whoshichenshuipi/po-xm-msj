USE food_street;

-- 1. 检查并创建必要的数据

-- 检查 users 表数据
SELECT 'Users table data:' as info;
SELECT id, username, full_name, role FROM users LIMIT 5;

-- 检查 merchant 表数据
SELECT 'Merchant table data:' as info;
SELECT id, name, approved FROM merchant LIMIT 5;

-- 如果 merchant 表没有数据，插入测试数据
INSERT IGNORE INTO merchant (id, name, contact_phone, address, culture_positioning, description, approved, created_at, updated_at) VALUES
(1, '老北京炸酱面', '010-12345678', '美食街1号', '非遗传承', '传承百年的炸酱面制作工艺', 1, NOW(), NOW()),
(2, '四川担担面', '010-12345679', '美食街2号', '地方特色', '正宗四川担担面，麻辣鲜香', 1, NOW(), NOW()),
(3, '广东茶点', '010-12345680', '美食街3号', '文化传承', '精致粤式茶点', 1, NOW(), NOW()),
(4, '山西刀削面', '010-12345681', '美食街4号', '地方特色', '正宗山西刀削面', 1, NOW(), NOW()),
(5, '传统糕点', '010-12345682', '美食街5号', '传统工艺', '传统手工糕点', 1, NOW(), NOW());

-- 检查 food_detail 表数据
SELECT 'Food detail table data:' as info;
SELECT id, name, merchant_id, price FROM food_detail LIMIT 5;

-- 插入 food_detail 测试数据（如果不存在）
INSERT IGNORE INTO food_detail (
  id, name, description, image_url, video_url, price, rating, category, tags, 
  merchant_id, culture_story, ingredients, cooking_method, nutrition_info, 
  origin_story, cultural_significance, preparation_time, difficulty_level, 
  serving_size, is_featured, view_count, like_count, status, created_at, updated_at
) VALUES
(1, '传统炒饭', '粒粒分明的炒饭，香气四溢', '/src/assets/文化街.jpg', '/src/assets/炒饭.mp4', 
 28.00, 4.8, '主食类', '传统,香脆,营养', 1, 
 '炒饭作为中华传统美食', '优质大米、新鲜蔬菜、鸡蛋', '1. 将米饭晾凉\n2. 热锅下油', 
 '富含碳水化合物、蛋白质', '炒饭起源于古代', '炒饭体现了中华民族的传统美德', 
 15, 'EASY', 2, 1, 156, 23, 'ACTIVE', NOW(), NOW()),

(2, '秘制炸鸡', '外酥内嫩的炸鸡', '/src/assets/文化街.jpg', '/src/assets/炸鸡.mp4', 
 35.00, 4.5, '小食类', '酥脆,香辣,特色', 2, 
 '炸鸡工艺传承自宫廷秘方', '新鲜鸡肉、秘制腌料', '1. 腌制\n2. 裹粉', 
 '高蛋白、低脂肪', '炸鸡工艺源于古代宫廷', '炸鸡代表了中华烹饪文化的创新', 
 25, 'MEDIUM', 1, 1, 89, 15, 'ACTIVE', NOW(), NOW()),

(3, '精选牛肉', '优质牛肉，精心烹制', '/src/assets/文化街.jpg', '/src/assets/牛肉.mp4', 
 68.00, 4.9, '主食类', '优质,鲜嫩,营养', 3, 
 '牛肉选用上等部位', '上等牛肉、配菜', '1. 焯水\n2. 炖煮', 
 '富含蛋白质、铁质', '牛肉烹饪技艺传承千年', '牛肉体现了对食材品质的追求', 
 60, 'HARD', 3, 1, 234, 45, 'ACTIVE', NOW(), NOW()),

(4, '山西刀削面', '正宗山西刀削面', '/src/assets/文化街.jpg', '', 
 22.00, 4.6, '主食类', '地方特色,传统工艺', 4, 
 '刀削面是山西传统面食', '高筋面粉、配菜', '1. 和面\n2. 削面', 
 '富含碳水化合物、蛋白质', '刀削面起源于山西', '刀削面是中华面食文化的重要组成部分', 
 30, 'MEDIUM', 2, 0, 67, 12, 'ACTIVE', NOW(), NOW()),

(5, '传统糕点', '传统手工糕点', '/src/assets/文化街.jpg', '', 
 18.00, 4.4, '甜品类', '传统工艺,手工制作', 5, 
 '传统糕点制作工艺传承千年', '面粉、白糖、配料', '1. 和面\n2. 烘焙', 
 '富含碳水化合物、蛋白质', '传统糕点起源于古代宫廷', '传统糕点承载着节庆文化', 
 45, 'HARD', 4, 0, 45, 8, 'ACTIVE', NOW(), NOW());

-- 2. 检查外键关联

-- 检查 reservation 表的外键约束
SELECT 
  CONSTRAINT_NAME,
  TABLE_NAME,
  REFERENCED_TABLE_NAME,
  REFERENCED_COLUMN_NAME
FROM 
  information_schema.KEY_COLUMN_USAGE
WHERE 
  TABLE_SCHEMA = 'food_street'
  AND TABLE_NAME = 'reservation'
  AND REFERENCED_TABLE_NAME IS NOT NULL;

-- 3. 验证数据完整性

-- 检查 merchants
SELECT 'Merchant verification:' as info;
SELECT id, name, approved, created_at FROM merchant WHERE id IN (1, 2, 3, 4, 5);

-- 检查 food details
SELECT 'Food detail verification:' as info;
SELECT id, name, merchant_id, price, status FROM food_detail WHERE id IN (1, 2, 3, 4, 5);

-- 检查 users
SELECT 'User verification:' as info;
SELECT id, username, full_name, role FROM users;

-- 4. 创建测试预订（可选）

-- 注意：创建测试预订时，user_id 应该是有效的用户ID
-- 查看可用的用户
SELECT 'Available users for testing:' as info;
SELECT id, username, full_name, role FROM users;

-- 创建测试预订
INSERT IGNORE INTO reservation (user_id, merchant_id, product_id, reserve_time, people, status, remark, created_at, updated_at)
SELECT 
  1 as user_id,                    -- 使用第一个用户
  merchant_id,
  id as product_id,
  DATE_ADD(NOW(), INTERVAL 1 DAY) as reserve_time,
  1 as people,
  'PENDING' as status,
  CONCAT('测试预订 - ', name) as remark,
  NOW(),
  NOW()
FROM food_detail 
WHERE id IN (1, 2)
LIMIT 2;

-- 5. 查看预订记录

SELECT 'Reservation records:' as info;
SELECT 
  r.id,
  r.user_id,
  u.username,
  u.full_name,
  r.merchant_id,
  m.name as merchant_name,
  r.product_id,
  fd.name as food_name,
  r.reserve_time,
  r.people,
  r.status,
  r.remark,
  r.created_at,
  r.updated_at
FROM reservation r
LEFT JOIN users u ON r.user_id = u.id
LEFT JOIN merchant m ON r.merchant_id = m.id
LEFT JOIN food_detail fd ON r.product_id = fd.id
ORDER BY r.created_at DESC
LIMIT 10;


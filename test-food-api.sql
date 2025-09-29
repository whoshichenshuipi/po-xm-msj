-- 测试食品详情API的SQL脚本
USE food_street;

-- 1. 检查 food_detail 表是否存在
SHOW TABLES LIKE 'food_detail';

-- 2. 查看表中的数据
SELECT COUNT(*) as total FROM food_detail;
SELECT * FROM food_detail LIMIT 5;

-- 3. 如果没有数据，插入测试数据
INSERT IGNORE INTO food_detail (
    name, description, image_url, video_url, price, rating, category, tags, 
    merchant_id, culture_story, ingredients, cooking_method, nutrition_info, 
    origin_story, cultural_significance, preparation_time, difficulty_level, 
    serving_size, is_featured, view_count, like_count, status, created_at, updated_at
) VALUES
('传统炒饭', '粒粒分明的炒饭，香气四溢', '/src/assets/文化街.jpg', '/src/assets/炒饭.mp4', 28.00, 4.8, '主食类', '传统,香脆,营养', 1, 
 '炒饭作为中华传统美食', '优质大米、新鲜蔬菜、鸡蛋', '1. 将米饭晾凉\n2. 热锅下油', 
 '富含碳水化合物、蛋白质、维生素', '炒饭起源于古代', '炒饭体现了中华民族的传统美德', 
 15, 'EASY', 2, 1, 156, 23, 'ACTIVE', NOW(), NOW());

-- 4. 再次查看数据
SELECT * FROM food_detail WHERE id = 1;

-- 5. 检查后端表
SHOW TABLES;


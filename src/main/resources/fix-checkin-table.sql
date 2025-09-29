-- 检查和修复checkin表结构的SQL脚本
-- 用于解决"Unknown column 'type' in 'field list'"错误

-- 1. 检查当前表结构
DESCRIBE checkin;

-- 2. 如果表不存在，创建表
CREATE TABLE IF NOT EXISTS checkin (
  id BIGINT AUTO_INCREMENT PRIMARY KEY,
  user_id BIGINT NOT NULL,
  merchant_id BIGINT,
  type VARCHAR(50) NOT NULL COMMENT '打卡类型：FOOD_EXPERIENCE, CULTURAL_LEARNING, ACTIITY_PARTICIPATION, VENUE_VISIT, OTHER',
  title VARCHAR(200) NOT NULL COMMENT '打卡标题',
  content VARCHAR(1000) COMMENT '打卡内容',
  location VARCHAR(200) COMMENT '打卡地点',
  location_tag VARCHAR(200) COMMENT '位置标签(兼容字段)',
  checkin_time DATETIME NOT NULL COMMENT '打卡时间',
  score INT COMMENT '体验评分 1-5',
  images VARCHAR(1000) COMMENT '相关图片URL，逗号分隔',
  tags VARCHAR(500) COMMENT '标签，逗号分隔',
  created_at DATETIME,
  updated_at DATETIME
);

-- 3. 如果表存在但缺少字段，添加缺失的字段
-- 检查并添加type字段
SET @sql = (SELECT IF(
  (SELECT COUNT(*) FROM INFORMATION_SCHEMA.COLUMNS 
   WHERE TABLE_SCHEMA = DATABASE() 
   AND TABLE_NAME = 'checkin' 
   AND COLUMN_NAME = 'type') = 0,
  'ALTER TABLE checkin ADD COLUMN type VARCHAR(50) NOT NULL COMMENT ''打卡类型：FOOD_EXPERIENCE, CULTURAL_LEARNING, ACTIVITY_PARTICIPATION, VENUE_VISIT, OTHER''',
  'SELECT ''type字段已存在'' as message'
));
PREPARE stmt FROM @sql;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;

-- 检查并添加title字段
SET @sql = (SELECT IF(
  (SELECT COUNT(*) FROM INFORMATION_SCHEMA.COLUMNS 
   WHERE TABLE_SCHEMA = DATABASE() 
   AND TABLE_NAME = 'checkin' 
   AND COLUMN_NAME = 'title') = 0,
  'ALTER TABLE checkin ADD COLUMN title VARCHAR(200) NOT NULL COMMENT ''打卡标题''',
  'SELECT ''title字段已存在'' as message'
));
PREPARE stmt FROM @sql;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;

-- 检查并添加content字段
SET @sql = (SELECT IF(
  (SELECT COUNT(*) FROM INFORMATION_SCHEMA.COLUMNS 
   WHERE TABLE_SCHEMA = DATABASE() 
   AND TABLE_NAME = 'checkin' 
   AND COLUMN_NAME = 'content') = 0,
  'ALTER TABLE checkin ADD COLUMN content VARCHAR(1000) COMMENT ''打卡内容''',
  'SELECT ''content字段已存在'' as message'
));
PREPARE stmt FROM @sql;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;

-- 检查并添加location字段
SET @sql = (SELECT IF(
  (SELECT COUNT(*) FROM INFORMATION_SCHEMA.COLUMNS 
   WHERE TABLE_SCHEMA = DATABASE() 
   AND TABLE_NAME = 'checkin' 
   AND COLUMN_NAME = 'location') = 0,
  'ALTER TABLE checkin ADD COLUMN location VARCHAR(200) COMMENT ''打卡地点''',
  'SELECT ''location字段已存在'' as message'
));
PREPARE stmt FROM @sql;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;

-- 检查并添加location_tag字段
SET @sql = (SELECT IF(
  (SELECT COUNT(*) FROM INFORMATION_SCHEMA.COLUMNS 
   WHERE TABLE_SCHEMA = DATABASE() 
   AND TABLE_NAME = 'checkin' 
   AND COLUMN_NAME = 'location_tag') = 0,
  'ALTER TABLE checkin ADD COLUMN location_tag VARCHAR(200) COMMENT ''位置标签(兼容字段)''',
  'SELECT ''location_tag字段已存在'' as message'
));
PREPARE stmt FROM @sql;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;

-- 检查并添加checkin_time字段
SET @sql = (SELECT IF(
  (SELECT COUNT(*) FROM INFORMATION_SCHEMA.COLUMNS 
   WHERE TABLE_SCHEMA = DATABASE() 
   AND TABLE_NAME = 'checkin' 
   AND COLUMN_NAME = 'checkin_time') = 0,
  'ALTER TABLE checkin ADD COLUMN checkin_time DATETIME NOT NULL COMMENT ''打卡时间''',
  'SELECT ''checkin_time字段已存在'' as message'
));
PREPARE stmt FROM @sql;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;

-- 检查并添加score字段
SET @sql = (SELECT IF(
  (SELECT COUNT(*) FROM INFORMATION_SCHEMA.COLUMNS 
   WHERE TABLE_SCHEMA = DATABASE() 
   AND TABLE_NAME = 'checkin' 
   AND COLUMN_NAME = 'score') = 0,
  'ALTER TABLE checkin ADD COLUMN score INT COMMENT ''体验评分 1-5''',
  'SELECT ''score字段已存在'' as message'
));
PREPARE stmt FROM @sql;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;

-- 检查并添加images字段
SET @sql = (SELECT IF(
  (SELECT COUNT(*) FROM INFORMATION_SCHEMA.COLUMNS 
   WHERE TABLE_SCHEMA = DATABASE() 
   AND TABLE_NAME = 'checkin' 
   AND COLUMN_NAME = 'images') = 0,
  'ALTER TABLE checkin ADD COLUMN images VARCHAR(1000) COMMENT ''相关图片URL，逗号分隔''',
  'SELECT ''images字段已存在'' as message'
));
PREPARE stmt FROM @sql;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;

-- 检查并添加tags字段
SET @sql = (SELECT IF(
  (SELECT COUNT(*) FROM INFORMATION_SCHEMA.COLUMNS 
   WHERE TABLE_SCHEMA = DATABASE() 
   AND TABLE_NAME = 'checkin' 
   AND COLUMN_NAME = 'tags') = 0,
  'ALTER TABLE checkin ADD COLUMN tags VARCHAR(500) COMMENT ''标签，逗号分隔''',
  'SELECT ''tags字段已存在'' as message'
));
PREPARE stmt FROM @sql;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;

-- 检查并添加created_at字段
SET @sql = (SELECT IF(
  (SELECT COUNT(*) FROM INFORMATION_SCHEMA.COLUMNS 
   WHERE TABLE_SCHEMA = DATABASE() 
   AND TABLE_NAME = 'checkin' 
   AND COLUMN_NAME = 'created_at') = 0,
  'ALTER TABLE checkin ADD COLUMN created_at DATETIME',
  'SELECT ''created_at字段已存在'' as message'
));
PREPARE stmt FROM @sql;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;

-- 检查并添加updated_at字段
SET @sql = (SELECT IF(
  (SELECT COUNT(*) FROM INFORMATION_SCHEMA.COLUMNS 
   WHERE TABLE_SCHEMA = DATABASE() 
   AND TABLE_NAME = 'checkin' 
   AND COLUMN_NAME = 'updated_at') = 0,
  'ALTER TABLE checkin ADD COLUMN updated_at DATETIME',
  'SELECT ''updated_at字段已存在'' as message'
));
PREPARE stmt FROM @sql;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;

-- 4. 最终检查表结构
DESCRIBE checkin;

-- 5. 插入测试数据（如果表为空）
INSERT IGNORE INTO checkin (user_id, merchant_id, type, title, content, location, checkin_time, score, images, tags, created_at, updated_at) VALUES
(2, 1, 'FOOD_EXPERIENCE', '老北京炸酱面体验', '今天在老字号面馆体验了正宗的老北京炸酱面，师傅现场展示了传统制作工艺，面条劲道，酱料香浓，真正体验了京城美食文化的魅力。', '北京路美食街1号', '2024-01-15 12:30:00', 5, 'https://example.com/images/zhajiangmian1.jpg,https://example.com/images/zhajiangmian2.jpg', '老北京,炸酱面,传统工艺', NOW(), NOW()),
(3, 2, 'FOOD_EXPERIENCE', '手工包子制作体验', '参与了传统手工包子的制作过程，从和面、调馅到包制，每一步都体现了匠心精神。包子皮薄馅大，咬一口汤汁四溢，感受到了传统面点的精髓。', '天津包子铺', '2024-01-14 15:20:00', 4, 'https://example.com/images/baozi1.jpg', '手工包子,传统面点,匠心', NOW(), NOW()),
(2, null, 'CULTURAL_LEARNING', '书法艺术体验课', '参加了书法艺术体验课，学习了楷书的基本笔画和结构。老师耐心指导，让我感受到了中华书法的博大精深，一撇一捺间都蕴含着深厚的文化底蕴。', '文化艺术中心', '2024-01-13 14:00:00', 5, 'https://example.com/images/shufa1.jpg', '书法,传统文化,艺术体验', NOW(), NOW()),
(3, null, 'CULTURAL_LEARNING', '茶艺文化讲座', '聆听了茶艺文化讲座，了解了茶的起源、分类和冲泡技巧。通过实际操作，体验了从选茶、温杯到冲泡的完整流程，深深被中国茶文化的优雅和深邃所吸引。', '茶文化体验馆', '2024-01-12 16:45:00', 4, '', '茶艺,茶文化,传统文化', NOW(), NOW()),
(2, 3, 'ACTIVITY_PARTICIPATION', '新春民俗文化节', '参加了新春民俗文化节，观看了舞龙舞狮表演，体验了传统游戏和手工艺制作。活动丰富多彩，让我深入了解了春节的传统习俗和文化内涵。', '民俗文化广场', '2024-01-11 10:30:00', 5, 'https://example.com/images/chunji1.jpg,https://example.com/images/chunji2.jpg', '新春,民俗,文化节,传统游戏', NOW(), NOW());

-- 6. 检查数据
SELECT COUNT(*) as total_records FROM checkin;
SELECT id, user_id, type, title FROM checkin LIMIT 5;

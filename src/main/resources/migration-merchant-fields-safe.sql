-- 安全的商户表字段迁移脚本
-- 这个脚本会在添加字段前检查字段是否已存在，避免重复添加导致的错误

-- 添加 cultural_level 字段
SELECT COUNT(*) INTO @col_exists
FROM information_schema.columns 
WHERE table_name = 'merchant' 
AND column_name = 'cultural_level'
AND table_schema = DATABASE();

SET @sql = IF(@col_exists = 0,
    'ALTER TABLE merchant ADD COLUMN cultural_level VARCHAR(50) COMMENT ''文化资质级别：national(国家级)、provincial(省级)、municipal(市级)''',
    'SELECT ''字段 cultural_level 已存在，跳过'' AS message');
PREPARE stmt FROM @sql;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;

-- 添加 rating 字段
SELECT COUNT(*) INTO @col_exists
FROM information_schema.columns 
WHERE table_name = 'merchant' 
AND column_name = 'rating'
AND table_schema = DATABASE();

SET @sql = IF(@col_exists = 0,
    'ALTER TABLE merchant ADD COLUMN rating VARCHAR(10) COMMENT ''文化评级：A/B/C''',
    'SELECT ''字段 rating 已存在，跳过'' AS message');
PREPARE stmt FROM @sql;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;

-- 添加 heritage_cert 字段
SELECT COUNT(*) INTO @col_exists
FROM information_schema.columns 
WHERE table_name = 'merchant' 
AND column_name = 'heritage_cert'
AND table_schema = DATABASE();

SET @sql = IF(@col_exists = 0,
    'ALTER TABLE merchant ADD COLUMN heritage_cert TINYINT(1) DEFAULT 0 COMMENT ''老字号认证''',
    'SELECT ''字段 heritage_cert 已存在，跳过'' AS message');
PREPARE stmt FROM @sql;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;

-- 添加 intangible_heritage 字段
SELECT COUNT(*) INTO @col_exists
FROM information_schema.columns 
WHERE table_name = 'merchant' 
AND column_name = 'intangible_heritage'
AND table_schema = DATABASE();

SET @sql = IF(@col_exists = 0,
    'ALTER TABLE merchant ADD COLUMN intangible_heritage TINYINT(1) DEFAULT 0 COMMENT ''非遗认证''',
    'SELECT ''字段 intangible_heritage 已存在，跳过'' AS message');
PREPARE stmt FROM @sql;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;


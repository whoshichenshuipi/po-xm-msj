-- 为 merchant 表添加文化资质相关字段
-- 注意：如果字段已存在，执行此脚本会报错，这是正常的
-- 可以忽略错误或使用 SHOW COLUMNS FROM merchant 先检查字段是否存在

ALTER TABLE merchant 
ADD COLUMN cultural_level VARCHAR(50) COMMENT '文化资质级别：national(国家级)、provincial(省级)、municipal(市级)',
ADD COLUMN rating VARCHAR(10) COMMENT '文化评级：A/B/C',
ADD COLUMN heritage_cert TINYINT(1) DEFAULT 0 COMMENT '老字号认证',
ADD COLUMN intangible_heritage TINYINT(1) DEFAULT 0 COMMENT '非遗认证';


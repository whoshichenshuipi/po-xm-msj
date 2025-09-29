-- 简单的商户表字段迁移脚本
-- 依次添加字段，如果字段已存在会报错，可以忽略

-- 添加 cultural_level 字段
ALTER TABLE merchant 
ADD COLUMN cultural_level VARCHAR(50) COMMENT '文化资质级别：national(国家级)、provincial(省级)、municipal(市级)';

-- 添加 rating 字段  
ALTER TABLE merchant 
ADD COLUMN rating VARCHAR(10) COMMENT '文化评级：A/B/C';

-- 添加 heritage_cert 字段
ALTER TABLE merchant 
ADD COLUMN heritage_cert TINYINT(1) DEFAULT 0 COMMENT '老字号认证';

-- 添加 intangible_heritage 字段
ALTER TABLE merchant 
ADD COLUMN intangible_heritage TINYINT(1) DEFAULT 0 COMMENT '非遗认证';


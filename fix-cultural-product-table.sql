-- 修复 cultural_product 表，添加 features 字段

-- 检查字段是否存在，如果不存在则添加
ALTER TABLE cultural_product 
ADD COLUMN features TEXT COMMENT '产品特色' 
AFTER cultural_story;

-- 如果需要重建表（注意：会删除现有数据）
-- DROP TABLE IF EXISTS cultural_product;
-- 然后执行 create-cultural-product-table.sql

-- 验证字段
DESC cultural_product;


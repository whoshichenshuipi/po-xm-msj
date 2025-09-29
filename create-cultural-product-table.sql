-- 文化产品表建表SQL
-- 执行此SQL创建 cultural_product 表

CREATE TABLE IF NOT EXISTS cultural_product (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    product_name VARCHAR(200) NOT NULL COMMENT '产品名称',
    merchant_id BIGINT NOT NULL COMMENT '所属商户ID',
    merchant_name VARCHAR(100) COMMENT '所属商户名称',
    cultural_tags TEXT COMMENT '文化标签(JSON格式，数组)',
    cultural_story TEXT COMMENT '文化故事',
    features TEXT COMMENT '产品特色',
    images TEXT COMMENT '产品图片(JSON格式，数组)',
    status VARCHAR(20) NOT NULL DEFAULT 'PENDING' COMMENT '审核状态：PENDING(待审核), APPROVED(已通过), REJECTED(已驳回)',
    approved_by BIGINT COMMENT '审核人ID',
    approved_at TIMESTAMP NULL COMMENT '审核时间',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    INDEX idx_merchant_id (merchant_id),
    INDEX idx_status (status),
    INDEX idx_approved_by (approved_by),
    INDEX idx_created_at (created_at)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='文化产品表';


-- 商户文化评级表
CREATE TABLE IF NOT EXISTS merchant_cultural_rating (
  id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '评级ID',
  merchant_id BIGINT NOT NULL COMMENT '商户ID',
  merchant_name VARCHAR(200) COMMENT '商户名称',
  cultural_level VARCHAR(50) COMMENT '文化资质等级：NATIONAL(国家级), PROVINCIAL(省级), MUNICIPAL(市级)',
  cultural_level_score INT COMMENT '文化资质等级分数(0-100)',
  content_richness INT COMMENT '内容展示丰富度(0-100)',
  interaction_rate INT COMMENT '互动参与度(0-100)',
  consumer_rating DECIMAL(3,2) COMMENT '消费者评价(1-5)',
  overall_rating VARCHAR(10) COMMENT '综合评级：A(优秀), B(良好), C(一般)',
  benefits TEXT COMMENT '激励政策(JSON格式)',
  rental_discount INT DEFAULT 0 COMMENT '租金优惠比例(0-100)',
  priority_push TINYINT(1) DEFAULT 0 COMMENT '是否优先推送',
  rated_by BIGINT COMMENT '评级人ID',
  rated_at DATETIME COMMENT '评级时间',
  created_at DATETIME COMMENT '创建时间',
  updated_at DATETIME COMMENT '更新时间',
  INDEX idx_merchant_id (merchant_id),
  INDEX idx_overall_rating (overall_rating),
  INDEX idx_cultural_level (cultural_level),
  INDEX idx_rated_at (rated_at)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='商户文化评级表';

-- 插入测试数据
INSERT INTO merchant_cultural_rating (
  merchant_id,
  merchant_name,
  cultural_level,
  cultural_level_score,
  content_richness,
  interaction_rate,
  consumer_rating,
  overall_rating,
  benefits,
  rental_discount,
  priority_push,
  rated_by,
  rated_at,
  created_at,
  updated_at
) VALUES
(1, '老字号烧饼店', 'NATIONAL', 95, 95, 88, 4.5, 'A', '{"rentalDiscount":"20%","priorityPush":"优先推送"}', 20, 1, 1, NOW(), NOW(), NOW()),
(2, '传统糕点店', 'PROVINCIAL', 85, 85, 82, 4.2, 'B', '{"rentalDiscount":"10%","priorityPush":"普通推送"}', 10, 0, 1, NOW(), NOW(), NOW()),
(3, '文化茶楼', 'MUNICIPAL', 75, 80, 76, 3.8, 'B', '{"rentalDiscount":"10%","priorityPush":"普通推送"}', 10, 0, 1, NOW(), NOW(), NOW()),
(4, '非遗手工艺坊', 'NATIONAL', 98, 96, 92, 4.8, 'A', '{"rentalDiscount":"20%","priorityPush":"优先推送"}', 20, 1, 1, NOW(), NOW(), NOW()),
(5, '传统面食馆', 'PROVINCIAL', 78, 78, 74, 3.9, 'C', '{"rentalDiscount":"0%","priorityPush":"普通推送"}', 0, 0, 1, NOW(), NOW(), NOW());


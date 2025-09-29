-- 培训计划表
CREATE TABLE IF NOT EXISTS training_plan (
  id BIGINT AUTO_INCREMENT PRIMARY KEY,
  title VARCHAR(200) NOT NULL COMMENT '培训主题',
  type VARCHAR(50) NOT NULL COMMENT '培训类型：SKILL_TRAINING(技艺培训)、CONTENT_CREATION(文化内容)、PLANNING(策划培训)',
  description VARCHAR(1000) COMMENT '培训描述',
  content TEXT COMMENT '培训内容',
  trainer VARCHAR(100) NOT NULL COMMENT '培训讲师',
  training_date DATE NOT NULL COMMENT '培训日期',
  location VARCHAR(300) NOT NULL COMMENT '培训地点',
  capacity INT NOT NULL DEFAULT 30 COMMENT '培训容量',
  registered_count INT NOT NULL DEFAULT 0 COMMENT '已报名人数',
  created_by BIGINT COMMENT '创建人ID',
  status VARCHAR(32) DEFAULT 'DRAFT' COMMENT '状态：DRAFT(草稿)、PUBLISHED(已发布)、FINISHED(已结束)',
  created_at DATETIME COMMENT '创建时间',
  updated_at DATETIME COMMENT '更新时间',
  INDEX idx_type (type),
  INDEX idx_status (status),
  INDEX idx_training_date (training_date)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='培训计划表';

-- 培训证书表
CREATE TABLE IF NOT EXISTS training_certificate (
  id BIGINT AUTO_INCREMENT PRIMARY KEY,
  training_id BIGINT NOT NULL COMMENT '培训计划ID',
  training_title VARCHAR(200) COMMENT '培训主题',
  merchant_id BIGINT COMMENT '商户ID',
  merchant_name VARCHAR(200) COMMENT '商户名称',
  participant VARCHAR(100) COMMENT '参与者姓名',
  certificate_url VARCHAR(500) COMMENT '证书文件URL',
  certificate_no VARCHAR(100) COMMENT '证书编号',
  issue_date DATE COMMENT '颁发日期',
  status VARCHAR(32) DEFAULT 'DRAFT' COMMENT '状态：DRAFT(草稿)、ISSUED(已颁发)',
  created_at DATETIME COMMENT '创建时间',
  updated_at DATETIME COMMENT '更新时间',
  INDEX idx_training_id (training_id),
  INDEX idx_merchant_id (merchant_id),
  INDEX idx_status (status),
  INDEX idx_certificate_no (certificate_no)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='培训证书表';

-- 插入测试数据（可选）
INSERT INTO training_plan (title, type, description, content, trainer, training_date, location, capacity, registered_count, status, created_at, updated_at) VALUES
('传统技艺提升培训', 'SKILL_TRAINING', '提升传统工艺制作技艺，学习传统技法精髓', '本次培训将涵盖传统技艺的各个方面，包括材料选择、工艺技法、质量控制等内容。通过理论学习与实践操作相结合，帮助学员掌握传统技艺的核心要领。', '张师傅', '2024-03-01', '文化中心', 30, 0, 'PUBLISHED', NOW(), NOW()),
('文化故事挖掘', 'CONTENT_CREATION', '学习如何挖掘和讲述文化故事，提升产品文化附加值', '培训内容包括文化故事的挖掘方法、故事讲述技巧、产品与文化故事的结合方式等。', '李老师', '2024-03-15', '线上培训', 50, 0, 'PUBLISHED', NOW(), NOW()),
('文化产品策划', 'PLANNING', '学习文化产品的策划与营销技巧', '涵盖文化产品的市场分析、策划流程、营销策略等内容。', '王讲师', '2024-04-01', '文化中心', 40, 0, 'DRAFT', NOW(), NOW());


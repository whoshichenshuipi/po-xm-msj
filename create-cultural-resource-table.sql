-- 创建文化资源对接表
DROP TABLE IF EXISTS cultural_resource;

CREATE TABLE cultural_resource (
  id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '资源ID',
  title VARCHAR(200) NOT NULL COMMENT '资源标题',
  category VARCHAR(50) NOT NULL COMMENT '资源类别：POLICY(政策文件), COOPERATION(合作机会), TRAINING(培训信息)',
  description VARCHAR(500) COMMENT '资源描述',
  content TEXT COMMENT '资源内容',
  target_merchants TEXT COMMENT '目标商户(JSON格式，数组)',
  publisher_id BIGINT COMMENT '发布人ID',
  status VARCHAR(20) DEFAULT 'DRAFT' COMMENT '状态：DRAFT(草稿), PUBLISHED(已发布)',
  publish_date DATETIME COMMENT '发布日期',
  created_at DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  INDEX idx_category (category),
  INDEX idx_status (status),
  INDEX idx_publish_date (publish_date)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='文化资源对接表';

-- 插入测试数据
INSERT INTO cultural_resource (title, category, description, content, target_merchants, publisher_id, status, publish_date) VALUES
('非遗保护政策解读', 'POLICY', '最新非遗保护政策详细解读，帮助商户了解政策支持', 
'为深入推进非物质文化遗产保护工作，加强非遗资源的传承与发展，特制定以下政策措施：
1. 建立非遗保护专项基金
2. 提供场地和基础设施支持
3. 优先给予相关政策扶持
请符合条件的商户积极申报。', 
'["老字号", "非遗传承"]', 
1, 'PUBLISHED', '2024-01-15 10:00:00'),
('文化创意设计合作', 'COOPERATION', '与知名文化创意设计机构建立合作关系', 
'为提高文化街商户的创意设计水平，我们与多家知名文化创意设计机构达成合作协议：
1. 提供免费设计咨询服务
2. 组织创意设计培训
3. 协助打造文化IP和品牌形象
欢迎各商户报名参与。', 
'["所有商户"]', 
1, 'PUBLISHED', '2024-01-10 14:30:00'),
('文化运营能力提升培训', 'TRAINING', '帮助商户提升文化运营能力，增强文化内涵', 
'本次培训将涵盖以下内容：
1. 文化品牌建设策略
2. 文化内容创作技巧
3. 文化营销推广方法
4. 数字文化传播新趋势
培训为期3天，报名从速。', 
'["老字号", "非遗传承", "文化创意"]', 
1, 'PUBLISHED', '2024-01-20 09:00:00');


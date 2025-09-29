-- 商户表
CREATE TABLE IF NOT EXISTS merchant (
  id BIGINT AUTO_INCREMENT PRIMARY KEY,
  name VARCHAR(200) NOT NULL,
  contact_phone VARCHAR(50),
  address VARCHAR(300),
  culture_positioning VARCHAR(200), -- 文化定位
  description VARCHAR(1000),
  created_at DATETIME,
  updated_at DATETIME,
  approved TINYINT(1) DEFAULT 0
);

-- 场地表
CREATE TABLE IF NOT EXISTS venue (
  id BIGINT AUTO_INCREMENT PRIMARY KEY,
  name VARCHAR(200) NOT NULL,
  type VARCHAR(100), -- 店铺/活动区/展示区
  status VARCHAR(50) DEFAULT 'IDLE', -- IDLE/IN_USE/MAINTAIN
  capacity INT,
  description VARCHAR(1000),
  created_at DATETIME,
  updated_at DATETIME
);

-- 卫生检查表
CREATE TABLE IF NOT EXISTS hygiene_check (
  id BIGINT AUTO_INCREMENT PRIMARY KEY,
  merchant_id BIGINT NOT NULL,
  check_date DATE NOT NULL,
  result VARCHAR(32) NOT NULL, -- PASS/FAIL
  notes VARCHAR(1000),
  next_check_date DATE,
  created_at DATETIME,
  updated_at DATETIME
);

-- 客流统计表（可按场地或整街统计）
CREATE TABLE IF NOT EXISTS traffic_stat (
  id BIGINT AUTO_INCREMENT PRIMARY KEY,
  stat_date DATE NOT NULL,
  venue_id BIGINT,
  visitor_count INT NOT NULL,
  created_at DATETIME
);

-- 营收记录表（按商户）
CREATE TABLE IF NOT EXISTS revenue_record (
  id BIGINT AUTO_INCREMENT PRIMARY KEY,
  merchant_id BIGINT NOT NULL,
  record_date DATE NOT NULL,
  revenue DECIMAL(12,2) NOT NULL,
  created_at DATETIME
);
-- 活动表
CREATE TABLE IF NOT EXISTS activity (
  id BIGINT AUTO_INCREMENT PRIMARY KEY,
  title VARCHAR(200) NOT NULL,
  description TEXT,
  merchant_id BIGINT,
  start_time DATETIME NOT NULL,
  end_time DATETIME NOT NULL,
  location VARCHAR(300),
  status VARCHAR(32) DEFAULT 'DRAFT', -- DRAFT/PUBLISHED/ENDED
  created_at DATETIME,
  updated_at DATETIME
);

-- 活动报名表
CREATE TABLE IF NOT EXISTS activity_signup (
  id BIGINT AUTO_INCREMENT PRIMARY KEY,
  activity_id BIGINT NOT NULL,
  user_id BIGINT NOT NULL,
  merchant_id BIGINT,
  status VARCHAR(32) DEFAULT 'PENDING', -- PENDING/CONFIRMED/CANCELLED
  created_at DATETIME,
  updated_at DATETIME
);

-- 活动核销表
CREATE TABLE IF NOT EXISTS activity_verify (
  id BIGINT AUTO_INCREMENT PRIMARY KEY,
  activity_id BIGINT NOT NULL,
  signup_id BIGINT NOT NULL,
  verifier_id BIGINT,
  verify_time DATETIME NOT NULL,
  status VARCHAR(32) DEFAULT 'VERIFIED', -- VERIFIED/REJECTED
  remark VARCHAR(500),
  created_at DATETIME,
  updated_at DATETIME
);
-- 预订表（餐位/体验活动）
CREATE TABLE IF NOT EXISTS reservation (
  id BIGINT AUTO_INCREMENT PRIMARY KEY,
  user_id BIGINT NOT NULL,
  merchant_id BIGINT NOT NULL,
  product_id BIGINT,
  reserve_time DATETIME NOT NULL,
  people INT DEFAULT 1,
  status VARCHAR(32) DEFAULT 'PENDING', -- PENDING/CONFIRMED/CANCELLED/DONE
  remark VARCHAR(500),
  created_at DATETIME,
  updated_at DATETIME
);

-- 文化打卡表
CREATE TABLE IF NOT EXISTS checkin (
  id BIGINT AUTO_INCREMENT PRIMARY KEY,
  user_id BIGINT NOT NULL,
  merchant_id BIGINT,
  type VARCHAR(50) NOT NULL COMMENT '打卡类型：FOOD_EXPERIENCE, CULTURAL_LEARNING, ACTIVITY_PARTICIPATION, VENUE_VISIT, OTHER',
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

-- 评价表（文化体验/商户/产品）
CREATE TABLE IF NOT EXISTS review (
  id BIGINT AUTO_INCREMENT PRIMARY KEY,
  user_id BIGINT NOT NULL,
  merchant_id BIGINT,
  product_id BIGINT,
  rating INT NOT NULL, -- 1-5
  content VARCHAR(2000),
  created_at DATETIME,
  updated_at DATETIME
);
-- 用户表（与 data.sql 初始化数据一致）
CREATE TABLE IF NOT EXISTS users (
  id BIGINT AUTO_INCREMENT PRIMARY KEY,
  username VARCHAR(50) NOT NULL UNIQUE,
  email VARCHAR(255) NOT NULL UNIQUE,
  full_name VARCHAR(100) NOT NULL,
  password VARCHAR(100) NOT NULL,
  created_at DATETIME,
  updated_at DATETIME,
  role VARCHAR(32) NOT NULL
);

-- 商户文化资质表
CREATE TABLE IF NOT EXISTS merchant_qualification (
  id BIGINT AUTO_INCREMENT PRIMARY KEY,
  merchant_id BIGINT NOT NULL,
  title VARCHAR(200) NOT NULL,
  level VARCHAR(100), -- 资质级别：国家级、省级、市级
  cert_no VARCHAR(100),
  valid_until DATE,
  status VARCHAR(32) DEFAULT 'VALID', -- VALID/EXPIRED/PENDING
  created_at DATETIME,
  updated_at DATETIME
);

-- 商品/文化产品表
CREATE TABLE IF NOT EXISTS product (
  id BIGINT AUTO_INCREMENT PRIMARY KEY,
  merchant_id BIGINT NOT NULL,
  name VARCHAR(200) NOT NULL,
  price DECIMAL(10,2) NOT NULL,
  culture_tags VARCHAR(300), -- 文化标签
  culture_story TEXT,
  on_sale TINYINT(1) DEFAULT 1,
  created_at DATETIME,
  updated_at DATETIME
);

-- 文化内容推荐记录表
CREATE TABLE IF NOT EXISTS cultural_recommendation (
  id BIGINT AUTO_INCREMENT PRIMARY KEY,
  content_id BIGINT NOT NULL,
  user_id BIGINT NOT NULL,
  score DECIMAL(3,2) DEFAULT 0.00,
  reason VARCHAR(50),
  clicked TINYINT(1) DEFAULT 0,
  liked TINYINT(1) DEFAULT 0,
  created_at DATETIME,
  updated_at DATETIME
);

-- 文化内容标签表
CREATE TABLE IF NOT EXISTS cultural_tag (
  id BIGINT AUTO_INCREMENT PRIMARY KEY,
  name VARCHAR(50) NOT NULL UNIQUE,
  description VARCHAR(200),
  color VARCHAR(7) DEFAULT '#409EFF',
  usage_count INT DEFAULT 0,
  enabled TINYINT(1) DEFAULT 1,
  sort_order INT DEFAULT 0,
  created_at DATETIME,
  updated_at DATETIME
);

-- 文化内容分类表
CREATE TABLE IF NOT EXISTS cultural_category (
  id BIGINT AUTO_INCREMENT PRIMARY KEY,
  name VARCHAR(100) NOT NULL UNIQUE,
  description VARCHAR(500),
  icon VARCHAR(200),
  sort_order INT DEFAULT 0,
  enabled TINYINT(1) DEFAULT 1,
  content_count INT DEFAULT 0,
  created_at DATETIME,
  updated_at DATETIME
);

-- 文化内容表（文化模块）
CREATE TABLE IF NOT EXISTS cultural_content (
  id BIGINT AUTO_INCREMENT PRIMARY KEY,
  title VARCHAR(200) NOT NULL,
  summary VARCHAR(2000),
  content LONGTEXT,
  type VARCHAR(32) NOT NULL,
  tags VARCHAR(500),
  merchant_id BIGINT,
  category_id BIGINT COMMENT '关联分类ID',
  approved TINYINT(1) DEFAULT 0 NOT NULL COMMENT '审核状态：0-待审核，1-已通过，2-已拒绝',
  approval_status VARCHAR(32) DEFAULT 'PENDING' COMMENT '审核状态：PENDING-待审核，APPROVED-已通过，REJECTED-已拒绝',
  approver_id BIGINT COMMENT '审核人ID',
  approval_time DATETIME COMMENT '审核时间',
  approval_comment VARCHAR(1000) COMMENT '审核意见',
  submitter_id BIGINT COMMENT '提交人ID',
  view_count INT DEFAULT 0 COMMENT '浏览次数',
  like_count INT DEFAULT 0 COMMENT '点赞次数',
  created_at DATETIME,
  updated_at DATETIME
);

-- 消息通知表
CREATE TABLE IF NOT EXISTS notification (
  id BIGINT AUTO_INCREMENT PRIMARY KEY,
  title VARCHAR(200) NOT NULL,
  content TEXT,
  type VARCHAR(50) DEFAULT 'SYSTEM', -- SYSTEM/MERCHANT/CONSUMER
  target_type VARCHAR(50), -- USER/MERCHANT/ALL
  target_id BIGINT,
  status VARCHAR(32) DEFAULT 'UNREAD', -- UNREAD/READ
  created_at DATETIME,
  updated_at DATETIME
);

-- 系统日志表
CREATE TABLE IF NOT EXISTS system_log (
  id BIGINT AUTO_INCREMENT PRIMARY KEY,
  user_id BIGINT,
  action VARCHAR(200) NOT NULL,
  module VARCHAR(100),
  ip_address VARCHAR(50),
  user_agent VARCHAR(500),
  result VARCHAR(32) DEFAULT 'SUCCESS', -- SUCCESS/FAIL
  created_at DATETIME
);


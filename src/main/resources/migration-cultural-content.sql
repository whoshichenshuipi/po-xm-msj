-- 文化内容表结构升级脚本
-- 添加审核相关字段和统计字段

-- 添加新的审核状态字段
ALTER TABLE cultural_content 
ADD COLUMN approval_status VARCHAR(32) DEFAULT 'PENDING' COMMENT '审核状态：PENDING-待审核，APPROVED-已通过，REJECTED-已拒绝';

-- 添加审核人ID字段
ALTER TABLE cultural_content 
ADD COLUMN approver_id BIGINT COMMENT '审核人ID';

-- 添加审核时间字段
ALTER TABLE cultural_content 
ADD COLUMN approval_time DATETIME COMMENT '审核时间';

-- 添加审核意见字段
ALTER TABLE cultural_content 
ADD COLUMN approval_comment VARCHAR(1000) COMMENT '审核意见';

-- 添加提交人ID字段
ALTER TABLE cultural_content 
ADD COLUMN submitter_id BIGINT COMMENT '提交人ID';

-- 添加浏览次数字段
ALTER TABLE cultural_content 
ADD COLUMN view_count INT DEFAULT 0 COMMENT '浏览次数';

-- 添加点赞次数字段
ALTER TABLE cultural_content 
ADD COLUMN like_count INT DEFAULT 0 COMMENT '点赞次数';

-- 更新现有数据的审核状态
UPDATE cultural_content 
SET approval_status = CASE 
    WHEN approved = 1 THEN 'APPROVED'
    WHEN approved = 0 THEN 'PENDING'
    ELSE 'PENDING'
END;

-- 添加索引以提高查询性能
CREATE INDEX idx_cultural_content_approval_status ON cultural_content(approval_status);
CREATE INDEX idx_cultural_content_submitter ON cultural_content(submitter_id);
CREATE INDEX idx_cultural_content_approver ON cultural_content(approver_id);
CREATE INDEX idx_cultural_content_type_status ON cultural_content(type, approval_status);

-- 验证表结构
DESCRIBE cultural_content;

-- 路线详情表
CREATE TABLE IF NOT EXISTS route_details (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    route_id BIGINT NOT NULL COMMENT '路线ID',
    route_name VARCHAR(100) NOT NULL COMMENT '路线名称',
    description TEXT COMMENT '路线描述',
    route_type VARCHAR(50) NOT NULL COMMENT '路线类型',
    total_distance INT NOT NULL COMMENT '总距离（米）',
    estimated_duration INT NOT NULL COMMENT '预计时长（分钟）',
    difficulty_level INT NOT NULL DEFAULT 3 COMMENT '路线难度等级（1-5）',
    rating DECIMAL(3,2) DEFAULT 0.00 COMMENT '路线评分（1-5）',
    status VARCHAR(20) NOT NULL DEFAULT 'ACTIVE' COMMENT '路线状态',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    created_by BIGINT COMMENT '创建者ID',
    INDEX idx_route_id (route_id),
    INDEX idx_route_type (route_type),
    INDEX idx_status (status),
    INDEX idx_rating (rating)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='路线详情表';

-- 路线节点表
CREATE TABLE IF NOT EXISTS route_nodes (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    route_id BIGINT NOT NULL COMMENT '路线ID',
    node_order INT NOT NULL COMMENT '节点顺序',
    node_name VARCHAR(100) NOT NULL COMMENT '节点名称',
    description TEXT COMMENT '节点描述',
    node_type VARCHAR(50) NOT NULL COMMENT '节点类型',
    merchant_id BIGINT COMMENT '关联的商户ID',
    attraction_id BIGINT COMMENT '关联的景点ID',
    longitude DECIMAL(10,7) COMMENT '经度',
    latitude DECIMAL(10,7) COMMENT '纬度',
    stay_duration INT DEFAULT 0 COMMENT '停留时间（分钟）',
    status VARCHAR(20) NOT NULL DEFAULT 'ACTIVE' COMMENT '节点状态',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    INDEX idx_route_id (route_id),
    INDEX idx_node_order (route_id, node_order),
    INDEX idx_node_type (node_type),
    INDEX idx_merchant_id (merchant_id),
    INDEX idx_attraction_id (attraction_id),
    INDEX idx_status (status)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='路线节点表';

-- 插入示例路线数据
INSERT INTO route_details (route_id, route_name, description, route_type, total_distance, estimated_duration, difficulty_level, rating, status) VALUES
(1, '非遗美食之旅', '探索传统非遗美食，感受文化传承的独特魅力', '传统美食', 1200, 120, 3, 4.8, 'ACTIVE'),
(2, '老字号寻味之旅', '走访百年老字号，品味历史味道', '传统美食', 800, 90, 2, 4.6, 'ACTIVE'),
(3, '民俗节庆美食之旅', '体验节庆美食文化，感受民俗魅力', '文化体验', 1500, 150, 4, 4.7, 'ACTIVE'),
(4, '地域特色美食之旅', '品尝各地特色美食，感受地域文化', '地方特色', 1000, 108, 3, 4.5, 'ACTIVE');

-- 插入示例路线节点数据
INSERT INTO route_nodes (route_id, node_order, node_name, description, node_type, merchant_id, longitude, latitude, stay_duration, status) VALUES
-- 非遗美食之旅节点
(1, 1, '老字号茶楼', '百年传承的茶楼，品味传统茶文化', 'MERCHANT', 1, 116.397428, 39.90923, 30, 'ACTIVE'),
(1, 2, '传统糕点店', '手工制作的传统糕点，传承古法工艺', 'MERCHANT', 2, 116.398428, 39.91023, 20, 'ACTIVE'),
(1, 3, '文化广场', '展示非遗文化的中心广场', 'ATTRACTION', 1, 116.399428, 39.91123, 15, 'ACTIVE'),
(1, 4, '传统面食店', '手工拉面，传承千年技艺', 'MERCHANT', 3, 116.400428, 39.91223, 25, 'ACTIVE'),
(1, 5, '休息区', '游客休息区域', 'REST_AREA', NULL, 116.401428, 39.91323, 10, 'ACTIVE'),

-- 老字号寻味之旅节点
(2, 1, '百年老店', '历史悠久的传统老店', 'MERCHANT', 4, 116.402428, 39.91423, 35, 'ACTIVE'),
(2, 2, '传统酱菜店', '手工制作的特色酱菜', 'MERCHANT', 5, 116.403428, 39.91523, 20, 'ACTIVE'),
(2, 3, '历史博物馆', '了解老字号历史', 'ATTRACTION', 2, 116.404428, 39.91623, 30, 'ACTIVE'),
(2, 4, '传统糖果店', '手工制作的传统糖果', 'MERCHANT', 6, 116.405428, 39.91723, 15, 'ACTIVE'),

-- 民俗节庆美食之旅节点
(3, 1, '节庆广场', '举办各种节庆活动的广场', 'ATTRACTION', 3, 116.406428, 39.91823, 20, 'ACTIVE'),
(3, 2, '传统小吃摊', '节庆特色小吃', 'MERCHANT', 7, 116.407428, 39.91923, 25, 'ACTIVE'),
(3, 3, '民俗表演区', '观看传统民俗表演', 'ATTRACTION', 4, 116.408428, 39.92023, 40, 'ACTIVE'),
(3, 4, '特色餐厅', '节庆特色菜品', 'MERCHANT', 8, 116.409428, 39.92123, 45, 'ACTIVE'),
(3, 5, '手工艺品店', '传统手工艺品制作', 'MERCHANT', 9, 116.410428, 39.92223, 20, 'ACTIVE'),

-- 地域特色美食之旅节点
(4, 1, '川菜馆', '正宗川菜，麻辣鲜香', 'MERCHANT', 10, 116.411428, 39.92323, 30, 'ACTIVE'),
(4, 2, '粤菜馆', '精致粤菜，清淡鲜美', 'MERCHANT', 11, 116.412428, 39.92423, 35, 'ACTIVE'),
(4, 3, '鲁菜馆', '传统鲁菜，口味浓郁', 'MERCHANT', 12, 116.413428, 39.92523, 30, 'ACTIVE'),
(4, 4, '苏菜馆', '江南苏菜，清淡雅致', 'MERCHANT', 13, 116.414428, 39.92623, 35, 'ACTIVE'),
(4, 5, '文化展示区', '各地饮食文化展示', 'ATTRACTION', 5, 116.415428, 39.92723, 25, 'ACTIVE');

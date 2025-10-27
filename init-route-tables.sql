USE food_street;

-- 创建路线详情表
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

-- 创建路线节点表
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
INSERT IGNORE INTO route_details (route_id, route_name, description, route_type, total_distance, estimated_duration, difficulty_level, rating, status) VALUES
(1, '非遗美食之旅', '探索传统非遗美食，感受文化传承的独特魅力', '传统美食', 1200, 120, 3, 4.8, 'ACTIVE'),
(2, '老字号寻味之旅', '走访百年老字号，品味历史味道', '传统美食', 800, 90, 2, 4.6, 'ACTIVE'),
(3, '民俗节庆美食之旅', '体验节庆美食文化，感受民俗魅力', '文化体验', 1500, 150, 4, 4.7, 'ACTIVE'),
(4, '地域特色美食之旅', '品尝各地特色美食，感受地域文化', '地方特色', 1000, 108, 3, 4.5, 'ACTIVE');

-- 插入示例路线节点数据
INSERT IGNORE INTO route_nodes (route_id, node_order, node_name, description, node_type, merchant_id, longitude, latitude, stay_duration, status) VALUES
(1, 1, '老字号茶楼', '百年传承的茶楼，品味传统茶文化', 'MERCHANT', 1, 116.397428, 39.90923, 30, 'ACTIVE'),
(1, 2, '传统糕点店', '手工制作的传统糕点，传承古法工艺', 'MERCHANT', 2, 116.398428, 39.91023, 20, 'ACTIVE'),
(1, 3, '文化广场', '展示非遗文化的中心广场', 'ATTRACTION', 1, 116.399428, 39.91123, 15, 'ACTIVE'),
(1, 4, '传统面食店', '手工拉面，传承千年技艺', 'MERCHANT', 3, 116.400428, 39.91223, 25, 'ACTIVE'),
(1, 5, '休息区', '游客休息区域', 'REST_AREA', NULL, 116.401428, 39.91323, 10, 'ACTIVE'),
(2, 1, '百年老店', '历史悠久的传统老店', 'MERCHANT', 4, 116.402428, 39.91423, 35, 'ACTIVE'),
(2, 2, '传统酱菜店', '手工制作的特色酱菜', 'MERCHANT', 5, 116.403428, 39.91523, 20, 'ACTIVE'),
(2, 3, '历史博物馆', '了解老字号历史', 'ATTRACTION', 2, 116.404428, 39.91623, 30, 'ACTIVE'),
(2, 4, '传统糖果店', '手工制作的传统糖果', 'MERCHANT', 6, 116.405428, 39.91723, 15, 'ACTIVE'),
(3, 1, '节庆广场', '举办各种节庆活动的广场', 'ATTRACTION', 3, 116.406428, 39.91823, 20, 'ACTIVE'),
(3, 2, '传统小吃摊', '节庆特色小吃', 'MERCHANT', 7, 116.407428, 39.91923, 25, 'ACTIVE'),
(3, 3, '民俗表演区', '观看传统民俗表演', 'ATTRACTION', 4, 116.408428, 39.92023, 40, 'ACTIVE'),
(3, 4, '特色餐厅', '节庆特色菜品', 'MERCHANT', 8, 116.409428, 39.92123, 45, 'ACTIVE'),
(3, 5, '手工艺品店', '传统手工艺品制作', 'MERCHANT', 9, 116.410428, 39.92223, 20, 'ACTIVE'),
(4, 1, '川菜馆', '正宗川菜，麻辣鲜香', 'MERCHANT', 10, 116.411428, 39.92323, 30, 'ACTIVE'),
(4, 2, '粤菜馆', '精致粤菜，清淡鲜美', 'MERCHANT', 11, 116.412428, 39.92423, 35, 'ACTIVE'),
(4, 3, '鲁菜馆', '传统鲁菜，口味浓郁', 'MERCHANT', 12, 116.413428, 39.92523, 30, 'ACTIVE'),
(4, 4, '苏菜馆', '江南苏菜，清淡雅致', 'MERCHANT', 13, 116.414428, 39.92623, 35, 'ACTIVE'),
(4, 5, '文化展示区', '各地饮食文化展示', 'ATTRACTION', 5, 116.415428, 39.92723, 25, 'ACTIVE');

-- 插入食品详情测试数据
INSERT IGNORE INTO food_detail (name, description, image_url, video_url, price, rating, category, tags, merchant_id, culture_story, ingredients, cooking_method, nutrition_info, origin_story, cultural_significance, preparation_time, difficulty_level, serving_size, is_featured, view_count, like_count, status, created_at, updated_at) VALUES
('传统炒饭', '粒粒分明的炒饭，香气四溢，口感丰富，传承百年制作工艺', '/src/assets/文化街.jpg', '/src/assets/炒饭.mp4', 28.00, 4.8, '主食类', '传统,香脆,营养', 1, '炒饭作为中华传统美食，承载着深厚的历史文化内涵。每一粒米饭都经过精心挑选，传承着祖辈的智慧。', '优质大米、新鲜蔬菜、鸡蛋、葱花、生抽、老抽、盐、胡椒粉', '1. 将米饭晾凉，确保粒粒分明\n2. 热锅下油，爆香葱花\n3. 加入鸡蛋炒散\n4. 下入米饭快速翻炒\n5. 调味后炒至粒粒分明', '富含碳水化合物、蛋白质、维生素，营养均衡，易于消化', '炒饭起源于古代，最初是为了不浪费剩饭而发明的烹饪方法，后来逐渐发展成一道经典美食。', '炒饭体现了中华民族勤俭节约的传统美德，也展现了中华烹饪文化的博大精深。', 15, 'EASY', 2, 1, 156, 23, 'ACTIVE', NOW(), NOW()),
('秘制炸鸡', '外酥内嫩的炸鸡，秘制调料，回味无穷，采用传统工艺制作', '/src/assets/文化街.jpg', '/src/assets/炸鸡.mp4', 35.00, 4.5, '小食类', '酥脆,香辣,特色', 2, '炸鸡工艺传承自宫廷秘方，经过数代师傅的改良，形成了独特的制作技艺。', '新鲜鸡肉、秘制腌料、面粉、鸡蛋、面包糠、辣椒粉、孜然粉', '1. 鸡肉用秘制腌料腌制2小时\n2. 依次裹上面粉、蛋液、面包糠\n3. 油温180度炸制8分钟\n4. 捞出沥油，撒上调料', '高蛋白、低脂肪，富含维生素B族，适量食用有益健康', '炸鸡工艺源于古代宫廷，经过民间传承和改良，成为深受喜爱的传统美食。', '炸鸡代表了中华烹饪文化的创新精神，体现了传统与现代的完美结合。', 25, 'MEDIUM', 1, 1, 89, 15, 'ACTIVE', NOW(), NOW()),
('精选牛肉', '优质牛肉，精心烹制，口感鲜嫩，选用上等食材', '/src/assets/文化街.jpg', '/src/assets/牛肉.mp4', 68.00, 4.9, '主食类', '优质,鲜嫩,营养', 3, '牛肉选用上等部位，经过精心处理，体现了对食材的尊重和对美食的追求。', '上等牛肉、洋葱、胡萝卜、土豆、料酒、生抽、老抽、冰糖、八角、桂皮', '1. 牛肉切块，冷水下锅焯水\n2. 热锅下油，炒糖色\n3. 下入牛肉翻炒上色\n4. 加入调料和配菜\n5. 小火炖煮1小时', '富含优质蛋白质、铁质、维生素B12，营养丰富，滋补强身', '牛肉烹饪技艺传承千年，体现了中华饮食文化的深厚底蕴。', '牛肉代表了中华饮食文化中对食材品质的追求，体现了"食不厌精"的饮食理念。', 60, 'HARD', 3, 1, 234, 45, 'ACTIVE', NOW(), NOW()),
('山西刀削面', '正宗山西刀削面，劲道爽滑，传承百年制作工艺', '/src/assets/文化街.jpg', '', 22.00, 4.6, '主食类', '地方特色,传统工艺,手工制作', 4, '刀削面是山西传统面食，制作工艺独特，体现了山西人民的智慧和勤劳。', '高筋面粉、水、盐、猪肉、豆角、土豆、西红柿、葱姜蒜、生抽、老抽', '1. 和面至光滑，醒面30分钟\n2. 用特制刀具削面入锅\n3. 煮至面条浮起\n4. 炒制配菜\n5. 面条捞出浇上配菜', '富含碳水化合物、蛋白质，营养均衡，易于消化吸收', '刀削面起源于山西，是当地人民智慧的结晶，体现了山西面食文化的精髓。', '刀削面代表了山西面食文化的最高水平，是中华面食文化的重要组成部分。', 30, 'MEDIUM', 2, 0, 67, 12, 'ACTIVE', NOW(), NOW()),
('传统糕点', '传统手工糕点，香甜可口，传承古法制作工艺', '/src/assets/文化街.jpg', '', 18.00, 4.4, '甜品类', '传统工艺,手工制作,香甜', 5, '传统糕点制作工艺传承千年，每一道工序都体现了匠人精神和对品质的追求。', '面粉、白糖、鸡蛋、猪油、芝麻、花生、核桃、红枣', '1. 和面至光滑，醒面1小时\n2. 制作馅料\n3. 包制成型\n4. 刷蛋液撒芝麻\n5. 烤箱180度烤制20分钟', '富含碳水化合物、蛋白质、维生素E，适量食用有益健康', '传统糕点起源于古代宫廷，经过民间传承，成为节庆必备的美食。', '传统糕点承载着节庆文化，体现了中华民族对美好生活的向往。', 45, 'HARD', 4, 0, 45, 8, 'ACTIVE', NOW(), NOW());


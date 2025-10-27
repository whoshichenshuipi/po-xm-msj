-- 检查并创建food_detail表（如果不存在）
CREATE TABLE IF NOT EXISTS food_detail (
  id BIGINT AUTO_INCREMENT PRIMARY KEY,
  name VARCHAR(200) NOT NULL COMMENT '食品名称',
  description TEXT COMMENT '食品描述',
  image_url VARCHAR(500) COMMENT '食品图片URL',
  video_url VARCHAR(500) COMMENT '制作视频URL',
  price DECIMAL(10,2) NOT NULL COMMENT '价格',
  rating DECIMAL(3,2) DEFAULT 0.00 COMMENT '评分',
  category VARCHAR(100) NOT NULL COMMENT '食品分类',
  tags VARCHAR(500) COMMENT '标签，逗号分隔',
  merchant_id BIGINT COMMENT '关联商户ID',
  culture_story TEXT COMMENT '文化故事',
  ingredients TEXT COMMENT '食材配料',
  cooking_method TEXT COMMENT '制作方法',
  nutrition_info TEXT COMMENT '营养信息',
  origin_story TEXT COMMENT '起源故事',
  cultural_significance TEXT COMMENT '文化意义',
  preparation_time INT COMMENT '制作时间(分钟)',
  difficulty_level VARCHAR(50) COMMENT '制作难度：EASY/MEDIUM/HARD',
  serving_size INT COMMENT '建议份量',
  is_featured TINYINT(1) DEFAULT 0 COMMENT '是否推荐',
  view_count INT DEFAULT 0 COMMENT '浏览次数',
  like_count INT DEFAULT 0 COMMENT '点赞次数',
  status VARCHAR(32) DEFAULT 'ACTIVE' COMMENT '状态：ACTIVE/INACTIVE',
  created_at DATETIME,
  updated_at DATETIME
);

-- 清空现有数据
DELETE FROM food_detail;

-- 插入测试数据
INSERT INTO food_detail (name, description, image_url, video_url, price, rating, category, tags, merchant_id, culture_story, ingredients, cooking_method, nutrition_info, origin_story, cultural_significance, preparation_time, difficulty_level, serving_size, is_featured, view_count, like_count, status, created_at, updated_at) VALUES
('传统炒饭', '粒粒分明的炒饭，香气四溢，口感丰富，传承百年制作工艺', '/src/assets/文化街.jpg', '/src/assets/炒饭.mp4', 28.00, 4.8, '主食类', '传统,香脆,营养', 1, '炒饭作为中华传统美食，承载着深厚的历史文化内涵。每一粒米饭都经过精心挑选，传承着祖辈的智慧。', '优质大米、新鲜蔬菜、鸡蛋、葱花、生抽、老抽、盐、胡椒粉', '1. 将米饭晾凉，确保粒粒分明\n2. 热锅下油，爆香葱花\n3. 加入鸡蛋炒散\n4. 下入米饭快速翻炒\n5. 调味后炒至粒粒分明', '富含碳水化合物、蛋白质、维生素，营养均衡，易于消化', '炒饭起源于古代，最初是为了不浪费剩饭而发明的烹饪方法，后来逐渐发展成一道经典美食。', '炒饭体现了中华民族勤俭节约的传统美德，也展现了中华烹饪文化的博大精深。', 15, 'EASY', 2, 1, 156, 23, 'ACTIVE', NOW(), NOW()),

('秘制炸鸡', '外酥内嫩的炸鸡，秘制调料，回味无穷，采用传统工艺制作', '/src/assets/文化街.jpg', '/src/assets/炸鸡.mp4', 35.00, 4.5, '小食类', '酥脆,香辣,特色', 2, '炸鸡工艺传承自宫廷秘方，经过数代师傅的改良，形成了独特的制作技艺。', '新鲜鸡肉、秘制腌料、面粉、鸡蛋、面包糠、辣椒粉、孜然粉', '1. 鸡肉用秘制腌料腌制2小时\n2. 依次裹上面粉、蛋液、面包糠\n3. 油温180度炸制8分钟\n4. 捞出沥油，撒上调料', '高蛋白、低脂肪，富含维生素B族，适量食用有益健康', '炸鸡工艺源于古代宫廷，经过民间传承和改良，成为深受喜爱的传统美食。', '炸鸡代表了中华烹饪文化的创新精神，体现了传统与现代的完美结合。', 25, 'MEDIUM', 1, 1, 89, 15, 'ACTIVE', NOW(), NOW()),

('精选牛肉', '优质牛肉，精心烹制，口感鲜嫩，选用上等食材', '/src/assets/文化街.jpg', '/src/assets/牛肉.mp4', 68.00, 4.9, '主食类', '优质,鲜嫩,营养', 3, '牛肉选用上等部位，经过精心处理，体现了对食材的尊重和对美食的追求。', '上等牛肉、洋葱、胡萝卜、土豆、料酒、生抽、老抽、冰糖、八角、桂皮', '1. 牛肉切块，冷水下锅焯水\n2. 热锅下油，炒糖色\n3. 下入牛肉翻炒上色\n4. 加入调料和配菜\n5. 小火炖煮1小时', '富含优质蛋白质、铁质、维生素B12，营养丰富，滋补强身', '牛肉烹饪技艺传承千年，体现了中华饮食文化的深厚底蕴。', '牛肉代表了中华饮食文化中对食材品质的追求，体现了"食不厌精"的饮食理念。', 60, 'HARD', 3, 1, 234, 45, 'ACTIVE', NOW(), NOW()),

('山西刀削面', '正宗山西刀削面，劲道爽滑，传承百年制作工艺', '/src/assets/文化街.jpg', '', 22.00, 4.6, '主食类', '地方特色,传统工艺,手工制作', 4, '刀削面是山西传统面食，制作工艺独特，体现了山西人民的智慧和勤劳。', '高筋面粉、水、盐、猪肉、豆角、土豆、西红柿、葱姜蒜、生抽、老抽', '1. 和面至光滑，醒面30分钟\n2. 用特制刀具削面入锅\n3. 煮至面条浮起\n4. 炒制配菜\n5. 面条捞出浇上配菜', '富含碳水化合物、蛋白质，营养均衡，易于消化吸收', '刀削面起源于山西，是当地人民智慧的结晶，体现了山西面食文化的精髓。', '刀削面代表了山西面食文化的最高水平，是中华面食文化的重要组成部分。', 30, 'MEDIUM', 2, 0, 67, 12, 'ACTIVE', NOW(), NOW()),

('传统糕点', '传统手工糕点，香甜可口，传承古法制作工艺', '/src/assets/文化街.jpg', '', 18.00, 4.4, '甜品类', '传统工艺,手工制作,香甜', 5, '传统糕点制作工艺传承千年，每一道工序都体现了匠人精神和对品质的追求。', '面粉、白糖、鸡蛋、猪油、芝麻、花生、核桃、红枣', '1. 和面至光滑，醒面1小时\n2. 制作馅料\n3. 包制成型\n4. 刷蛋液撒芝麻\n5. 烤箱180度烤制20分钟', '富含碳水化合物、蛋白质、维生素E，适量食用有益健康', '传统糕点起源于古代宫廷，经过民间传承，成为节庆必备的美食。', '传统糕点承载着节庆文化，体现了中华民族对美好生活的向往。', 45, 'HARD', 4, 0, 45, 8, 'ACTIVE', NOW(), NOW());

-- 显示插入结果
SELECT COUNT(*) as total_foods FROM food_detail;
SELECT id, name, category, price, rating FROM food_detail ORDER BY id;

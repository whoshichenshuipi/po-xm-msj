-- 文化产品测试数据
-- 为 cultural_product 表插入测试数据

INSERT INTO cultural_product (
    product_name, 
    merchant_id, 
    merchant_name, 
    cultural_tags, 
    cultural_story, 
    features, 
    images, 
    status, 
    created_at, 
    updated_at
) VALUES

-- 已通过审核的产品
('手工月饼', 1, '老字号糕点店', '["非遗技艺", "传统工艺", "手工制作"]', 
'传承百年手工月饼制作工艺，每一块月饼都经过传统手工打造，选用优质原料，严格按照古法制作，口感醇厚，回味悠长。每一道工序都体现着匠人精神，是对传统美食文化的传承与创新。',
'酥软香甜，传统手工制作，口感层次丰富，选用优质原料',
'["/upload/products/mooncake1.jpg", "/upload/products/mooncake2.jpg"]', 
'APPROVED', 
NOW(), NOW()),

('传统茶艺表演', 3, '茶文化馆', '["茶文化", "传统技艺", "文化传承"]', 
'展示传统茶艺表演，包括茶道、茶艺、茶文化讲解，让游客深入了解中国茶文化的博大精深。每一场表演都是一场文化的盛宴，通过精湛的茶艺技巧，展现中国传统文化的魅力。',
'传统茶道，文化讲解，技艺精湛，互动体验',
'["/upload/products/tea1.jpg"]', 
'APPROVED', 
NOW(), NOW()),

('百年老字号包子', 1, '老字号糕点店', '["老字号", "百年传承", "地方特色"]', 
'传承百年的包子制作工艺，选用新鲜食材，手工和面，传统工艺，每一个包子都包涵着深厚的文化底蕴。包子皮薄馅大，汤汁丰富，口感鲜美，是传统美食文化的杰出代表。',
'皮薄馅大，汤汁丰富，传统工艺，口感鲜美',
'["/upload/products/baozi1.jpg", "/upload/products/baozi2.jpg", "/upload/products/baozi3.jpg"]', 
'APPROVED', 
NOW(), NOW()),

-- 待审核的产品
('民俗节庆套餐', 2, '民俗小吃店', '["民俗文化", "节庆美食", "地方特色"]', 
'结合本地民俗节庆特色，推出传统节庆限定套餐，包含多种传统小吃，让游客体验地道的民俗文化。每道小吃都蕴含着浓厚的地方特色和传统节庆的文化内涵。',
'节庆限定，民俗特色，文化氛围浓厚，多种传统小吃',
'["/upload/products/festival1.jpg", "/upload/products/festival2.jpg"]', 
'PENDING', 
NOW(), NOW()),

('传统剪纸艺术品', 1, '老字号糕点店', '["非遗技艺", "传统艺术", "手工制作"]', 
'精选优质纸张，由非遗传承人手工剪裁，每一幅作品都蕴含着深厚的文化底蕴和美好寓意。剪纸图案精美细致，传统与现代相结合，是对中国传统艺术的完美诠释。',
'手工剪裁，文化底蕴深厚，寓意美好，精湛技艺',
'["/upload/products/papercut1.jpg"]', 
'PENDING', 
NOW(), NOW()),

('传统面点套餐', 4, '传统面食馆', '["传统工艺", "手工制作", "文化传承"]', 
'采用传统手工和面技艺，制作各种传统面点，包括拉面、包子、饺子等。每一道面点都经过精心制作，口感劲道，味道纯正，让您品尝到最传统的中国美食。',
'口感劲道，味道纯正，传统技艺，手工制作',
'["/upload/products/noodles1.jpg", "/upload/products/noodles2.jpg"]', 
'PENDING', 
NOW(), NOW()),

-- 已驳回的产品
('创新融合料理', 2, '民俗小吃店', '["创新", "融合", "现代美食"]', 
'融合传统与现代的创新料理，结合传统工艺和现代创意，打造独特的味觉体验。',
'创新工艺，独特口感，现代融合',
'["/upload/products/fusion1.jpg"]', 
'REJECTED', 
NOW(), NOW()),

('特色小食拼盘', 5, '美食小摊', '["地方特色", "小吃文化"]', 
'精选多种地方特色小食，组合成丰富多样的拼盘，让您一次品尝多种传统美食。',
'丰富多样，地方特色',
'["/upload/products/snacks1.jpg"]', 
'REJECTED', 
NOW(), NOW());

-- 查询插入的数据
SELECT * FROM cultural_product ORDER BY created_at DESC;


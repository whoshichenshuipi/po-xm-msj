package com.example.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.entity.Checkin;
import com.example.mapper.CheckinMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import java.util.List;
import java.util.ArrayList;

@Service
@Transactional
@Slf4j
public class CheckinService {

    @Autowired
    private CheckinMapper mapper;

    /**
     * 创建打卡记录
     */
    public Checkin create(Checkin checkin) {
        checkin.setId(null);
        if (checkin.getCreatedAt() == null) {
            checkin.setCreatedAt(LocalDateTime.now());
        }
        if (checkin.getUpdatedAt() == null) {
            checkin.setUpdatedAt(LocalDateTime.now());
        }
        mapper.insert(checkin);
        return checkin;
    }

    /**
     * 更新打卡记录
     */
    public Checkin update(Checkin checkin) {
        checkin.setUpdatedAt(LocalDateTime.now());
        mapper.updateById(checkin);
        return checkin;
    }

    /**
     * 根据ID获取打卡记录
     */
    public Checkin getById(Long id) {
        return mapper.selectById(id);
    }

    /**
     * 删除打卡记录
     */
    public void delete(Long id) {
        mapper.deleteById(id);
    }

    /**
     * 分页查询打卡记录
     */
    public Page<Checkin> page(Long userId, Long merchantId, String type, String keyword, 
                             String locationTag, String startDate, String endDate, 
                             int pageNo, int pageSize) {
        try {
            log.info("=== 开始分页查询打卡记录 ===");
            log.info("查询参数 - pageNo: {}, pageSize: {}", pageNo, pageSize);
            log.info("过滤条件 - userId: {}, merchantId: {}, type: {}, keyword: {}, locationTag: {}, startDate: {}, endDate: {}", 
                    userId, merchantId, type, keyword, locationTag, startDate, endDate);
            
            // 1. 首先检查数据库中是否有任何数据
            QueryWrapper<Checkin> countWrapper = new QueryWrapper<>();
            Long totalCount = mapper.selectCount(countWrapper);
            log.info("数据库中总打卡记录数: {}", totalCount);
            
            if (totalCount == 0) {
                log.warn("⚠️ 数据库中没有任何打卡记录！");
                log.warn("可能的原因：");
                log.warn("1. 数据库表为空");
                log.warn("2. SQL初始化未执行");
                log.warn("3. 数据插入失败");
                
                // 尝试检查表是否存在
                try {
                    List<Checkin> testList = mapper.selectList(new QueryWrapper<>());
                    log.info("表存在，但无数据，记录数: {}", testList.size());
                } catch (Exception e) {
                    log.error("表可能不存在或连接问题: {}", e.getMessage());
                }
                
                Page<Checkin> emptyPage = new Page<>(pageNo, pageSize);
                emptyPage.setTotal(0);
                return emptyPage;
            }
            
            // 2. 构建查询条件
            QueryWrapper<Checkin> qw = new QueryWrapper<>();
            boolean hasConditions = false;
            
            // 用户ID过滤
            if (userId != null && userId > 0) {
                qw.eq("user_id", userId);
                log.debug("✅ 添加用户ID过滤条件: {}", userId);
                hasConditions = true;
            }
            
            // 商户ID过滤
            if (merchantId != null && merchantId > 0) {
                qw.eq("merchant_id", merchantId);
                log.debug("✅ 添加商户ID过滤条件: {}", merchantId);
                hasConditions = true;
            }
            
            // 打卡类型过滤
            if (StringUtils.hasText(type)) {
                qw.eq("type", type);
                log.debug("✅ 添加类型过滤条件: {}", type);
                hasConditions = true;
            }
            
            // 关键词搜索（标题或内容）
            if (StringUtils.hasText(keyword)) {
                qw.and(wrapper -> wrapper.like("title", keyword).or().like("content", keyword));
                log.debug("✅ 添加关键词搜索条件: {}", keyword);
                hasConditions = true;
            }
            
            // 位置标签过滤
            if (StringUtils.hasText(locationTag)) {
                qw.like("location_tag", locationTag);
                log.debug("✅ 添加位置标签过滤条件: {}", locationTag);
                hasConditions = true;
            }
            
            // 日期范围过滤
            if (StringUtils.hasText(startDate)) {
                try {
                    LocalDateTime start = LocalDateTime.parse(startDate + " 00:00:00", DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
                    qw.ge("checkin_time", start);
                    log.debug("✅ 添加开始日期过滤条件: {}", startDate);
                    hasConditions = true;
                } catch (Exception e) {
                    log.warn("❌ 开始日期格式错误: {}, 忽略此条件", startDate);
                }
            }
            
            if (StringUtils.hasText(endDate)) {
                try {
                    LocalDateTime end = LocalDateTime.parse(endDate + " 23:59:59", DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
                    qw.le("checkin_time", end);
                    log.debug("✅ 添加结束日期过滤条件: {}", endDate);
                    hasConditions = true;
                } catch (Exception e) {
                    log.warn("❌ 结束日期格式错误: {}, 忽略此条件", endDate);
                }
            }
            
            // 按创建时间倒序排列
            qw.orderByDesc("created_at");
            
            log.info("查询条件构建完成，是否有过滤条件: {}", hasConditions);
            
            // 3. 执行分页查询
            Page<Checkin> page = new Page<>(pageNo, pageSize);
            log.info("开始执行分页查询...");
            
            Page<Checkin> result = mapper.selectPage(page, qw);
            
            log.info("=== 分页查询完成 ===");
            log.info("查询结果 - 总记录数: {}, 当前页记录数: {}, 总页数: {}, 当前页: {}", 
                    result.getTotal(), result.getRecords().size(), result.getPages(), result.getCurrent());
            
            // 4. 详细结果分析
            if (result.getTotal() == 0) {
                log.warn("⚠️ 查询结果为空！");
                log.warn("可能的原因：");
                log.warn("1. 查询条件过于严格");
                log.warn("2. 数据不匹配查询条件");
                log.warn("3. 数据库字段映射问题");
                
                // 尝试无条件查询验证
                QueryWrapper<Checkin> testQw = new QueryWrapper<>();
                testQw.orderByDesc("created_at");
                List<Checkin> testResult = mapper.selectList(testQw);
                log.info("无条件查询结果: {} 条记录", testResult.size());
                
                if (testResult.size() > 0) {
                    log.info("前3条记录示例:");
                    for (int i = 0; i < Math.min(3, testResult.size()); i++) {
                        Checkin checkin = testResult.get(i);
                        log.info("  {}. ID: {}, 用户: {}, 类型: {}, 标题: {}", 
                                i + 1, checkin.getId(), checkin.getUserId(), checkin.getType(), checkin.getTitle());
                    }
                }
            } else {
                log.info("✅ 查询成功！");
                if (result.getRecords().size() > 0) {
                    log.info("当前页记录示例:");
                    for (int i = 0; i < Math.min(3, result.getRecords().size()); i++) {
                        Checkin checkin = result.getRecords().get(i);
                        log.info("  {}. ID: {}, 用户: {}, 类型: {}, 标题: {}", 
                                i + 1, checkin.getId(), checkin.getUserId(), checkin.getType(), checkin.getTitle());
                    }
                }
            }
            
            return result;
            
        } catch (Exception e) {
            log.error("❌ 分页查询打卡记录失败", e);
            log.error("异常详情: {}", e.getMessage());
            
            // 返回空的分页结果
            Page<Checkin> emptyPage = new Page<>(pageNo, pageSize);
            emptyPage.setTotal(0);
            return emptyPage;
        }
    }

    /**
     * 初始化测试数据
     */
    public Map<String, Object> initTestData() {
        Map<String, Object> result = new HashMap<>();
        try {
            log.info("开始初始化测试数据...");
            
            // 检查是否已有数据
            Long existingCount = mapper.selectCount(new QueryWrapper<>());
            if (existingCount > 0) {
                log.info("数据库中已存在 {} 条记录，跳过初始化", existingCount);
                result.put("status", "skipped");
                result.put("message", "数据已存在，无需初始化");
                result.put("existingCount", existingCount);
                return result;
            }
            
            // 创建测试数据
            List<Checkin> testData = createTestData();
            int successCount = 0;
            
            for (Checkin checkin : testData) {
                try {
                    mapper.insert(checkin);
                    successCount++;
                    log.debug("插入测试数据成功: {}", checkin.getTitle());
                } catch (Exception e) {
                    log.error("插入测试数据失败: {}, 错误: {}", checkin.getTitle(), e.getMessage());
                }
            }
            
            log.info("测试数据初始化完成，成功插入 {} 条记录", successCount);
            
            result.put("status", "success");
            result.put("message", "测试数据初始化成功");
            result.put("insertedCount", successCount);
            result.put("totalCount", testData.size());
            
            return result;
            
        } catch (Exception e) {
            log.error("初始化测试数据失败", e);
            result.put("status", "error");
            result.put("message", "初始化失败: " + e.getMessage());
            result.put("insertedCount", 0);
            return result;
        }
    }
    
    /**
     * 创建测试数据
     */
    private List<Checkin> createTestData() {
        List<Checkin> testData = new ArrayList<>();
        LocalDateTime now = LocalDateTime.now();
        
        // 美食体验打卡
        Checkin checkin1 = new Checkin();
        checkin1.setUserId(2L);
        checkin1.setMerchantId(1L);
        checkin1.setType("FOOD_EXPERIENCE");
        checkin1.setTitle("老北京炸酱面体验");
        checkin1.setContent("今天在老字号面馆体验了正宗的老北京炸酱面，师傅现场展示了传统制作工艺，面条劲道，酱料香浓，真正体验了京城美食文化的魅力。");
        checkin1.setLocation("北京路美食街1号");
        checkin1.setCheckinTime(LocalDateTime.of(2024, 1, 15, 12, 30));
        checkin1.setScore(5);
        checkin1.setImages("https://example.com/images/zhajiangmian1.jpg,https://example.com/images/zhajiangmian2.jpg");
        checkin1.setTags("老北京,炸酱面,传统工艺");
        checkin1.setCreatedAt(now);
        checkin1.setUpdatedAt(now);
        testData.add(checkin1);
        
        Checkin checkin2 = new Checkin();
        checkin2.setUserId(3L);
        checkin2.setMerchantId(2L);
        checkin2.setType("FOOD_EXPERIENCE");
        checkin2.setTitle("手工包子制作体验");
        checkin2.setContent("参与了传统手工包子的制作过程，从和面、调馅到包制，每一步都体现了匠心精神。包子皮薄馅大，咬一口汤汁四溢，感受到了传统面点的精髓。");
        checkin2.setLocation("天津包子铺");
        checkin2.setCheckinTime(LocalDateTime.of(2024, 1, 14, 15, 20));
        checkin2.setScore(4);
        checkin2.setImages("https://example.com/images/baozi1.jpg");
        checkin2.setTags("手工包子,传统面点,匠心");
        checkin2.setCreatedAt(now);
        checkin2.setUpdatedAt(now);
        testData.add(checkin2);
        
        // 文化学习打卡
        Checkin checkin3 = new Checkin();
        checkin3.setUserId(2L);
        checkin3.setType("CULTURAL_LEARNING");
        checkin3.setTitle("书法艺术体验课");
        checkin3.setContent("参加了书法艺术体验课，学习了楷书的基本笔画和结构。老师耐心指导，让我感受到了中华书法的博大精深，一撇一捺间都蕴含着深厚的文化底蕴。");
        checkin3.setLocation("文化艺术中心");
        checkin3.setCheckinTime(LocalDateTime.of(2024, 1, 13, 14, 0));
        checkin3.setScore(5);
        checkin3.setImages("https://example.com/images/shufa1.jpg");
        checkin3.setTags("书法,传统文化,艺术体验");
        checkin3.setCreatedAt(now);
        checkin3.setUpdatedAt(now);
        testData.add(checkin3);
        
        Checkin checkin4 = new Checkin();
        checkin4.setUserId(3L);
        checkin4.setType("CULTURAL_LEARNING");
        checkin4.setTitle("茶艺文化讲座");
        checkin4.setContent("聆听了茶艺文化讲座，了解了茶的起源、分类和冲泡技巧。通过实际操作，体验了从选茶、温杯到冲泡的完整流程，深深被中国茶文化的优雅和深邃所吸引。");
        checkin4.setLocation("茶文化体验馆");
        checkin4.setCheckinTime(LocalDateTime.of(2024, 1, 12, 16, 45));
        checkin4.setScore(4);
        checkin4.setTags("茶艺,茶文化,传统文化");
        checkin4.setCreatedAt(now);
        checkin4.setUpdatedAt(now);
        testData.add(checkin4);
        
        // 活动参与打卡
        Checkin checkin5 = new Checkin();
        checkin5.setUserId(2L);
        checkin5.setMerchantId(3L);
        checkin5.setType("ACTIVITY_PARTICIPATION");
        checkin5.setTitle("新春民俗文化节");
        checkin5.setContent("参加了新春民俗文化节，观看了舞龙舞狮表演，体验了传统游戏和手工艺制作。活动丰富多彩，让我深入了解了春节的传统习俗和文化内涵。");
        checkin5.setLocation("民俗文化广场");
        checkin5.setCheckinTime(LocalDateTime.of(2024, 1, 11, 10, 30));
        checkin5.setScore(5);
        checkin5.setImages("https://example.com/images/chunji1.jpg,https://example.com/images/chunji2.jpg");
        checkin5.setTags("新春,民俗,文化节,传统游戏");
        checkin5.setCreatedAt(now);
        checkin5.setUpdatedAt(now);
        testData.add(checkin5);
        
        return testData;
    }
    public Map<String, Object> testDatabaseConnection() {
        Map<String, Object> result = new HashMap<>();
        try {
            log.info("开始测试数据库连接...");
            
            // 测试基本查询
            Long totalCount = mapper.selectCount(new QueryWrapper<>());
            result.put("totalCount", totalCount);
            result.put("status", "success");
            result.put("message", "数据库连接正常");
            
            // 如果有数据，获取前几条记录
            if (totalCount > 0) {
                QueryWrapper<Checkin> qw = new QueryWrapper<>();
                qw.last("LIMIT 3");
                List<Checkin> sampleData = mapper.selectList(qw);
                result.put("sampleData", sampleData);
                log.info("获取到样本数据: {}", sampleData.size());
            }
            
            log.info("数据库测试完成 - 总记录数: {}", totalCount);
            return result;
            
        } catch (Exception e) {
            log.error("数据库测试失败", e);
            result.put("status", "error");
            result.put("message", "数据库连接失败: " + e.getMessage());
            result.put("totalCount", 0);
            
            // 检查是否是字段缺失问题
            if (e.getMessage().contains("Unknown column")) {
                result.put("errorType", "missing_column");
                result.put("suggestion", "请执行 fix-checkin-table.sql 脚本来修复表结构");
            }
            
            return result;
        }
    }
    public Map<String, Object> getUserStats(Long userId) {
        Map<String, Object> stats = new HashMap<>();
        
        try {
            log.info("开始获取用户打卡统计 - userId: {}", userId);
            
            // 总打卡数
            QueryWrapper<Checkin> totalWrapper = new QueryWrapper<>();
            totalWrapper.eq("user_id", userId);
            Long totalCount = mapper.selectCount(totalWrapper);
            stats.put("totalCount", totalCount);
            log.debug("用户总打卡数: {}", totalCount);
            
            // 按类型统计
            Map<String, Long> typeStats = new HashMap<>();
            String[] types = {"FOOD_EXPERIENCE", "CULTURAL_LEARNING", "ACTIVITY_PARTICIPATION", "VENUE_VISIT", "OTHER"};
            for (String type : types) {
                QueryWrapper<Checkin> typeWrapper = new QueryWrapper<>();
                typeWrapper.eq("user_id", userId).eq("type", type);
                Long count = mapper.selectCount(typeWrapper);
                typeStats.put(type, count);
            }
            stats.put("typeStats", typeStats);
            log.debug("用户类型统计: {}", typeStats);
            
            // 平均评分 - 使用Java 8兼容的方式
            QueryWrapper<Checkin> scoreWrapper = new QueryWrapper<>();
            scoreWrapper.eq("user_id", userId).isNotNull("score");
            List<Checkin> checkinsWithScore = mapper.selectList(scoreWrapper);
            if (!checkinsWithScore.isEmpty()) {
                int totalScore = 0;
                int validScoreCount = 0;
                for (Checkin checkin : checkinsWithScore) {
                    if (checkin.getScore() != null) {
                        totalScore += checkin.getScore();
                        validScoreCount++;
                    }
                }
                if (validScoreCount > 0) {
                    double avgScore = (double) totalScore / validScoreCount;
                    stats.put("avgScore", Math.round(avgScore * 100.0) / 100.0);
                } else {
                    stats.put("avgScore", 0.0);
                }
            } else {
                stats.put("avgScore", 0.0);
            }
            log.debug("用户平均评分: {}", stats.get("avgScore"));
            
            // 最近打卡时间
            QueryWrapper<Checkin> recentWrapper = new QueryWrapper<>();
            recentWrapper.eq("user_id", userId).orderByDesc("checkin_time").last("LIMIT 1");
            Checkin recentCheckin = mapper.selectOne(recentWrapper);
            stats.put("lastCheckinTime", recentCheckin != null ? recentCheckin.getCheckinTime() : null);
            log.debug("用户最近打卡时间: {}", stats.get("lastCheckinTime"));
            
            log.info("用户打卡统计获取完成");
            return stats;
            
        } catch (Exception e) {
            log.error("获取用户打卡统计失败", e);
            // 返回默认统计结果
            Map<String, Object> defaultStats = new HashMap<>();
            defaultStats.put("totalCount", 0L);
            defaultStats.put("avgScore", 0.0);
            defaultStats.put("lastCheckinTime", null);
            
            Map<String, Long> defaultTypeStats = new HashMap<>();
            String[] types = {"FOOD_EXPERIENCE", "CULTURAL_LEARNING", "ACTIVITY_PARTICIPATION", "VENUE_VISIT", "OTHER"};
            for (String type : types) {
                defaultTypeStats.put(type, 0L);
            }
            defaultStats.put("typeStats", defaultTypeStats);
            
            return defaultStats;
        }
    }
}



package com.example.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.example.annotation.RequirePermission;
import com.example.entity.MerchantCulturalRating;
import com.example.entity.UserRole;
import com.example.mapper.MerchantCulturalRatingMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 商户文化评级控制器
 * 
 * @author example
 * @since 1.0.0
 */
@RestController
@RequestMapping("/merchant-ratings")
@CrossOrigin(origins = "*")
@Tag(name = "商户文化评级", description = "基于文化属性的商户评级体系")
public class MerchantCulturalRatingController {

    @Autowired
    private MerchantCulturalRatingMapper ratingMapper;
    
    @Autowired
    private com.example.mapper.MerchantMapper merchantMapper;

    /**
     * 获取评级列表
     */
    @GetMapping
    @RequirePermission(roles = {UserRole.ADMIN, UserRole.MERCHANT}, description = "管理员和商户可以查看评级列表")
    @Operation(summary = "获取评级列表", description = "获取商户文化评级列表")
    public ResponseEntity<List<MerchantCulturalRating>> list() {
        List<MerchantCulturalRating> list = ratingMapper.selectList(null);
        
        // 填充商户名称（如果为空）
        for (MerchantCulturalRating rating : list) {
            if (rating.getMerchantId() != null && (rating.getMerchantName() == null || rating.getMerchantName().isEmpty())) {
                com.example.entity.Merchant merchant = merchantMapper.selectById(rating.getMerchantId());
                if (merchant != null && merchant.getName() != null) {
                    rating.setMerchantName(merchant.getName());
                }
            }
        }
        
        return ResponseEntity.ok(list);
    }

    /**
     * 获取商户评级
     */
    @GetMapping("/merchant/{merchantId}")
    @RequirePermission(roles = {UserRole.ADMIN, UserRole.MERCHANT}, description = "管理员和商户可以查看商户评级")
    @Operation(summary = "获取商户评级", description = "根据商户ID获取评级信息")
    public ResponseEntity<MerchantCulturalRating> getByMerchantId(@PathVariable Long merchantId) {
        QueryWrapper<MerchantCulturalRating> wrapper = new QueryWrapper<>();
        wrapper.eq("merchant_id", merchantId);
        wrapper.orderByDesc("rated_at");
        wrapper.last("LIMIT 1");
        
        MerchantCulturalRating rating = ratingMapper.selectOne(wrapper);
        if (rating == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(rating);
    }

    /**
     * 更新商户评级
     */
    @PostMapping("/merchant/{merchantId}")
    @RequirePermission(roles = {UserRole.ADMIN}, description = "只有管理员可以更新评级")
    @Operation(summary = "更新商户评级", description = "管理员更新商户文化评级")
    public ResponseEntity<MerchantCulturalRating> updateRating(
            @PathVariable Long merchantId,
            @RequestBody MerchantCulturalRating rating,
            HttpSession session) {
        
        rating.setMerchantId(merchantId);
        
        // 从商户表获取商户名称
        com.example.entity.Merchant merchant = merchantMapper.selectById(merchantId);
        if (merchant != null && merchant.getName() != null) {
            rating.setMerchantName(merchant.getName());
        }
        
        // 计算综合评级
        String overallRating = calculateOverallRating(rating);
        rating.setOverallRating(overallRating);
        
        // 生成激励政策
        Map<String, Object> benefits = generateBenefits(overallRating);
        // 简单地将Map转换为JSON字符串
        String benefitsJson = "{\"rentalDiscount\":\"" + benefits.get("rentalDiscount") + 
                             "\",\"priorityPush\":\"" + benefits.get("priorityPush") + "\"}";
        rating.setBenefits(benefitsJson);
        
        // 根据评级设置优惠政策
        if ("A".equals(overallRating)) {
            rating.setRentalDiscount(20);
            rating.setPriorityPush(true);
        } else if ("B".equals(overallRating)) {
            rating.setRentalDiscount(10);
            rating.setPriorityPush(false);
        } else {
            rating.setRentalDiscount(0);
            rating.setPriorityPush(false);
        }
        
        Long currentUserId = (Long) session.getAttribute("userId");
        rating.setRatedBy(currentUserId);
        rating.setRatedAt(LocalDateTime.now());
        rating.setCreatedAt(LocalDateTime.now());
        rating.setUpdatedAt(LocalDateTime.now());
        
        ratingMapper.insert(rating);
        return ResponseEntity.status(HttpStatus.CREATED).body(rating);
    }

    /**
     * 获取评级统计
     */
    @GetMapping("/stats")
    @RequirePermission(roles = {UserRole.ADMIN, UserRole.MERCHANT}, description = "管理员和商户可以查看评级统计")
    @Operation(summary = "获取评级统计", description = "获取商户评级分布统计")
    public ResponseEntity<Map<String, Integer>> getStats() {
        List<MerchantCulturalRating> allRatings = ratingMapper.selectList(null);
        
        Map<String, Integer> stats = new HashMap<>();
        stats.put("A", 0);
        stats.put("B", 0);
        stats.put("C", 0);
        
        for (MerchantCulturalRating rating : allRatings) {
            if ("A".equals(rating.getOverallRating())) {
                stats.put("A", stats.get("A") + 1);
            } else if ("B".equals(rating.getOverallRating())) {
                stats.put("B", stats.get("B") + 1);
            } else {
                stats.put("C", stats.get("C") + 1);
            }
        }
        
        stats.put("total", allRatings.size());
        
        return ResponseEntity.ok(stats);
    }

    /**
     * 计算综合评级
     */
    private String calculateOverallRating(MerchantCulturalRating rating) {
        int totalScore = rating.getCulturalLevelScore() + 
                        rating.getContentRichness() + 
                        rating.getInteractionRate() + 
                        (rating.getConsumerRating().intValue() * 20);
        
        int averageScore = totalScore / 4;
        
        if (averageScore >= 85) {
            return "A";
        } else if (averageScore >= 70) {
            return "B";
        } else {
            return "C";
        }
    }

    /**
     * 生成激励政策
     */
    private Map<String, Object> generateBenefits(String overallRating) {
        Map<String, Object> benefits = new HashMap<>();
        
        if ("A".equals(overallRating)) {
            benefits.put("rentalDiscount", "20%");
            benefits.put("priorityPush", "优先推送");
        } else if ("B".equals(overallRating)) {
            benefits.put("rentalDiscount", "10%");
            benefits.put("priorityPush", "普通推送");
        } else {
            benefits.put("rentalDiscount", "0%");
            benefits.put("priorityPush", "普通推送");
        }
        
        return benefits;
    }
}


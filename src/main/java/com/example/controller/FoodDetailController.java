package com.example.controller;

import com.example.entity.FoodDetail;
import com.example.service.FoodDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 食品详情控制器
 */
@RestController
@RequestMapping("/food-details")
@CrossOrigin(origins = "*")
public class FoodDetailController {
    
    @Autowired
    private FoodDetailService foodDetailService;
    
    /**
     * 健康检查接口
     */
    @GetMapping("/health")
    public ResponseEntity<Map<String, Object>> health() {
        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("message", "FoodDetail API is running");
        response.put("timestamp", System.currentTimeMillis());
        return ResponseEntity.ok(response);
    }

    /**
     * 获取所有食品详情
     */
    @GetMapping
    public ResponseEntity<Map<String, Object>> getAllFoods() {
        try {
            // 先返回模拟数据，测试API是否工作
            List<FoodDetail> foods = new ArrayList<>();
            
            // 创建模拟数据
            FoodDetail food1 = new FoodDetail();
            food1.setId(1L);
            food1.setName("传统炒饭");
            food1.setDescription("粒粒分明的炒饭，香气四溢");
            food1.setPrice(new java.math.BigDecimal("28.00"));
            food1.setRating(new java.math.BigDecimal("4.8"));
            food1.setCategory("主食类");
            food1.setTags("传统,香脆,营养");
            food1.setStatus("ACTIVE");
            foods.add(food1);
            
            FoodDetail food2 = new FoodDetail();
            food2.setId(2L);
            food2.setName("秘制炸鸡");
            food2.setDescription("外酥内嫩的炸鸡，秘制调料");
            food2.setPrice(new java.math.BigDecimal("35.00"));
            food2.setRating(new java.math.BigDecimal("4.5"));
            food2.setCategory("小食类");
            food2.setTags("酥脆,香辣,特色");
            food2.setStatus("ACTIVE");
            foods.add(food2);
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("data", foods);
            response.put("message", "获取食品列表成功（模拟数据）");
            response.put("count", foods.size());
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", "获取食品列表失败: " + e.getMessage());
            response.put("error", e.getClass().getSimpleName());
            return ResponseEntity.status(500).body(response);
        }
    }
    
    /**
     * 根据ID获取食品详情
     */
    @GetMapping("/{id}")
    public ResponseEntity<Map<String, Object>> getFoodDetailById(@PathVariable Long id) {
        try {
            FoodDetail foodDetail = foodDetailService.getFoodDetailById(id);
            Map<String, Object> response = new HashMap<>();
            if (foodDetail != null) {
                response.put("success", true);
                response.put("data", foodDetail);
                response.put("message", "获取食品详情成功");
                return ResponseEntity.ok(response);
            } else {
                response.put("success", false);
                response.put("message", "食品不存在");
                return ResponseEntity.status(404).body(response);
            }
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", "获取食品详情失败: " + e.getMessage());
            return ResponseEntity.status(500).body(response);
        }
    }
    
    /**
     * 根据商户ID获取食品列表
     */
    @GetMapping("/merchant/{merchantId}")
    public ResponseEntity<Map<String, Object>> getFoodsByMerchantId(@PathVariable Long merchantId) {
        try {
            List<FoodDetail> foods = foodDetailService.getFoodsByMerchantId(merchantId);
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("data", foods);
            response.put("message", "获取商户食品列表成功");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", "获取商户食品列表失败: " + e.getMessage());
            return ResponseEntity.status(500).body(response);
        }
    }
    
    /**
     * 根据分类获取食品列表
     */
    @GetMapping("/category/{category}")
    public ResponseEntity<Map<String, Object>> getFoodsByCategory(@PathVariable String category) {
        try {
            List<FoodDetail> foods = foodDetailService.getFoodsByCategory(category);
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("data", foods);
            response.put("message", "获取分类食品列表成功");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", "获取分类食品列表失败: " + e.getMessage());
            return ResponseEntity.status(500).body(response);
        }
    }
    
    /**
     * 获取推荐食品
     */
    @GetMapping("/featured")
    public ResponseEntity<Map<String, Object>> getFeaturedFoods(@RequestParam(defaultValue = "10") Integer limit) {
        try {
            List<FoodDetail> foods = foodDetailService.getFeaturedFoods(limit);
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("data", foods);
            response.put("message", "获取推荐食品成功");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", "获取推荐食品失败: " + e.getMessage());
            return ResponseEntity.status(500).body(response);
        }
    }
    
    /**
     * 根据标签搜索食品
     */
    @GetMapping("/tag/{tag}")
    public ResponseEntity<Map<String, Object>> getFoodsByTag(@PathVariable String tag) {
        try {
            List<FoodDetail> foods = foodDetailService.getFoodsByTag(tag);
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("data", foods);
            response.put("message", "获取标签食品列表成功");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", "获取标签食品列表失败: " + e.getMessage());
            return ResponseEntity.status(500).body(response);
        }
    }
    
    /**
     * 搜索食品
     */
    @GetMapping("/search")
    public ResponseEntity<Map<String, Object>> searchFoods(@RequestParam String keyword) {
        try {
            List<FoodDetail> foods = foodDetailService.searchFoods(keyword);
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("data", foods);
            response.put("message", "搜索食品成功");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", "搜索食品失败: " + e.getMessage());
            return ResponseEntity.status(500).body(response);
        }
    }
    
    /**
     * 点赞食品
     */
    @PostMapping("/{id}/like")
    public ResponseEntity<Map<String, Object>> likeFood(@PathVariable Long id) {
        try {
            boolean success = foodDetailService.likeFood(id);
            Map<String, Object> response = new HashMap<>();
            if (success) {
                response.put("success", true);
                response.put("message", "点赞成功");
            } else {
                response.put("success", false);
                response.put("message", "点赞失败");
            }
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", "点赞失败: " + e.getMessage());
            return ResponseEntity.status(500).body(response);
        }
    }
    
    /**
     * 根据难度级别获取食品
     */
    @GetMapping("/difficulty/{difficultyLevel}")
    public ResponseEntity<Map<String, Object>> getFoodsByDifficulty(@PathVariable String difficultyLevel) {
        try {
            List<FoodDetail> foods = foodDetailService.getFoodsByDifficulty(difficultyLevel);
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("data", foods);
            response.put("message", "获取难度级别食品列表成功");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", "获取难度级别食品列表失败: " + e.getMessage());
            return ResponseEntity.status(500).body(response);
        }
    }
    
    /**
     * 根据价格范围获取食品
     */
    @GetMapping("/price-range")
    public ResponseEntity<Map<String, Object>> getFoodsByPriceRange(
            @RequestParam(required = false) Double minPrice,
            @RequestParam(required = false) Double maxPrice) {
        try {
            List<FoodDetail> foods = foodDetailService.getFoodsByPriceRange(minPrice, maxPrice);
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("data", foods);
            response.put("message", "获取价格范围食品列表成功");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", "获取价格范围食品列表失败: " + e.getMessage());
            return ResponseEntity.status(500).body(response);
        }
    }
}

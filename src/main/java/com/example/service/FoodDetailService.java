package com.example.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.entity.FoodDetail;
import com.example.mapper.FoodDetailMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 食品详情服务类
 */
@Service
public class FoodDetailService extends ServiceImpl<FoodDetailMapper, FoodDetail> {
    
    @Autowired
    private FoodDetailMapper foodDetailMapper;
    
    /**
     * 根据商户ID查询食品详情
     */
    public List<FoodDetail> getFoodsByMerchantId(Long merchantId) {
        return foodDetailMapper.findByMerchantId(merchantId);
    }
    
    /**
     * 根据分类查询食品详情
     */
    public List<FoodDetail> getFoodsByCategory(String category) {
        return foodDetailMapper.findByCategory(category);
    }
    
    /**
     * 查询推荐食品
     */
    public List<FoodDetail> getFeaturedFoods(Integer limit) {
        return foodDetailMapper.findFeaturedFoods(limit);
    }
    
    /**
     * 根据标签查询食品详情
     */
    public List<FoodDetail> getFoodsByTag(String tag) {
        return foodDetailMapper.findByTag(tag);
    }
    
    /**
     * 搜索食品详情
     */
    public List<FoodDetail> searchFoods(String keyword) {
        return foodDetailMapper.searchFoods(keyword);
    }
    
    /**
     * 获取食品详情并增加浏览次数
     */
    public FoodDetail getFoodDetailById(Long id) {
        FoodDetail foodDetail = this.getById(id);
        if (foodDetail != null) {
            // 增加浏览次数
            foodDetailMapper.incrementViewCount(id);
            foodDetail.setViewCount(foodDetail.getViewCount() + 1);
        }
        return foodDetail;
    }
    
    /**
     * 点赞食品
     */
    public boolean likeFood(Long id) {
        return foodDetailMapper.incrementLikeCount(id) > 0;
    }
    
    /**
     * 获取所有活跃的食品详情
     */
    public List<FoodDetail> getAllActiveFoods() {
        QueryWrapper<FoodDetail> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("status", "ACTIVE")
                   .orderByDesc("is_featured")
                   .orderByDesc("rating")
                   .orderByDesc("created_at");
        return this.list(queryWrapper);
    }
    
    /**
     * 根据难度级别查询食品
     */
    public List<FoodDetail> getFoodsByDifficulty(String difficultyLevel) {
        QueryWrapper<FoodDetail> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("difficulty_level", difficultyLevel)
                   .eq("status", "ACTIVE")
                   .orderByDesc("rating");
        return this.list(queryWrapper);
    }
    
    /**
     * 根据价格范围查询食品
     */
    public List<FoodDetail> getFoodsByPriceRange(Double minPrice, Double maxPrice) {
        QueryWrapper<FoodDetail> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("status", "ACTIVE");
        if (minPrice != null) {
            queryWrapper.ge("price", minPrice);
        }
        if (maxPrice != null) {
            queryWrapper.le("price", maxPrice);
        }
        queryWrapper.orderByDesc("rating");
        return this.list(queryWrapper);
    }
}

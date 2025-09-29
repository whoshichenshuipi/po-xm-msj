package com.example.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 食品详情实体类
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("food_detail")
public class FoodDetail {
    
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;
    
    /**
     * 食品名称
     */
    @TableField("name")
    private String name;
    
    /**
     * 食品描述
     */
    @TableField("description")
    private String description;
    
    /**
     * 食品图片URL
     */
    @TableField("image_url")
    private String imageUrl;
    
    /**
     * 制作视频URL
     */
    @TableField("video_url")
    private String videoUrl;
    
    /**
     * 价格
     */
    @TableField("price")
    private BigDecimal price;
    
    /**
     * 评分
     */
    @TableField("rating")
    private BigDecimal rating;
    
    /**
     * 食品分类
     */
    @TableField("category")
    private String category;
    
    /**
     * 标签，逗号分隔
     */
    @TableField("tags")
    private String tags;
    
    /**
     * 关联商户ID
     */
    @TableField("merchant_id")
    private Long merchantId;
    
    /**
     * 文化故事
     */
    @TableField("culture_story")
    private String cultureStory;
    
    /**
     * 食材配料
     */
    @TableField("ingredients")
    private String ingredients;
    
    /**
     * 制作方法
     */
    @TableField("cooking_method")
    private String cookingMethod;
    
    /**
     * 营养信息
     */
    @TableField("nutrition_info")
    private String nutritionInfo;
    
    /**
     * 起源故事
     */
    @TableField("origin_story")
    private String originStory;
    
    /**
     * 文化意义
     */
    @TableField("cultural_significance")
    private String culturalSignificance;
    
    /**
     * 制作时间(分钟)
     */
    @TableField("preparation_time")
    private Integer preparationTime;
    
    /**
     * 制作难度：EASY/MEDIUM/HARD
     */
    @TableField("difficulty_level")
    private String difficultyLevel;
    
    /**
     * 建议份量
     */
    @TableField("serving_size")
    private Integer servingSize;
    
    /**
     * 是否推荐
     */
    @TableField("is_featured")
    private Boolean isFeatured;
    
    /**
     * 浏览次数
     */
    @TableField("view_count")
    private Integer viewCount;
    
    /**
     * 点赞次数
     */
    @TableField("like_count")
    private Integer likeCount;
    
    /**
     * 状态：ACTIVE/INACTIVE
     */
    @TableField("status")
    private String status;
    
    /**
     * 创建时间
     */
    @TableField(value = "created_at", fill = FieldFill.INSERT)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdAt;
    
    /**
     * 更新时间
     */
    @TableField(value = "updated_at", fill = FieldFill.INSERT_UPDATE)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updatedAt;
}

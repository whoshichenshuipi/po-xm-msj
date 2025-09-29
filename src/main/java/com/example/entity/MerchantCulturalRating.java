package com.example.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 商户文化评级实体
 * 
 * @author example
 * @since 1.0.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("merchant_cultural_rating")
public class MerchantCulturalRating {

    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 商户ID
     */
    private Long merchantId;

    /**
     * 商户名称
     */
    private String merchantName;

    /**
     * 文化资质等级
     */
    private String culturalLevel; // NATIONAL, PROVINCIAL, MUNICIPAL

    /**
     * 文化资质等级分数(0-100)
     */
    private Integer culturalLevelScore;

    /**
     * 内容展示丰富度(0-100)
     */
    private Integer contentRichness;

    /**
     * 互动参与度(0-100)
     */
    private Integer interactionRate;

    /**
     * 消费者评价(1-5)
     */
    private BigDecimal consumerRating;

    /**
     * 综合评级：A(优秀), B(良好), C(一般)
     */
    private String overallRating;

    /**
     * 激励政策(JSON格式)
     */
    private String benefits;

    /**
     * 租金优惠比例(0-100)
     */
    private Integer rentalDiscount;

    /**
     * 是否优先推送
     */
    private Boolean priorityPush;

    /**
     * 评级人ID
     */
    private Long ratedBy;

    /**
     * 评级时间
     */
    private LocalDateTime ratedAt;

    /**
     * 创建时间
     */
    private LocalDateTime createdAt;

    /**
     * 更新时间
     */
    private LocalDateTime updatedAt;
}


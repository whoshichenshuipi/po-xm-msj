package com.example.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;

/**
 * 文化产品实体
 * 
 * @author example
 * @since 1.0.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("cultural_product")
public class CulturalProduct {

    @TableId(type = IdType.AUTO)
    private Long id;

    @NotBlank(message = "产品名称不能为空")
    private String productName;

    /**
     * 所属商户ID
     */
    private Long merchantId;

    /**
     * 所属商户名称
     */
    private String merchantName;

    /**
     * 文化标签(JSON格式，数组)
     */
    private String culturalTags;

    /**
     * 文化故事
     */
    private String culturalStory;

    /**
     * 产品特色
     */
    private String features;

    /**
     * 产品图片(JSON格式，数组)
     */
    private String images;

    /**
     * 审核状态：PENDING(待审核), APPROVED(已通过), REJECTED(已驳回)
     */
    private String status;

    /**
     * 审核人ID
     */
    private Long approvedBy;

    /**
     * 审核时间
     */
    private LocalDateTime approvedAt;

    /**
     * 创建时间
     */
    private LocalDateTime createdAt;

    /**
     * 更新时间
     */
    private LocalDateTime updatedAt;
}


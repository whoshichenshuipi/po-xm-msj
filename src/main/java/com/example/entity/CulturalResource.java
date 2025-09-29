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
 * 文化资源实体
 * 
 * @author example
 * @since 1.0.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("cultural_resource")
public class CulturalResource {

    @TableId(type = IdType.AUTO)
    private Long id;

    @NotBlank(message = "资源标题不能为空")
    private String title;

    /**
     * 资源类别：POLICY(政策文件), COOPERATION(合作机会), TRAINING(培训信息)
     */
    private String category;

    /**
     * 资源描述
     */
    private String description;

    /**
     * 资源内容
     */
    private String content;

    /**
     * 目标商户(JSON格式，数组)
     */
    private String targetMerchants;

    /**
     * 发布人ID
     */
    private Long publisherId;

    /**
     * 状态：DRAFT(草稿), PUBLISHED(已发布)
     */
    private String status;

    /**
     * 发布日期
     */
    private LocalDateTime publishDate;

    /**
     * 创建时间
     */
    private LocalDateTime createdAt;

    /**
     * 更新时间
     */
    private LocalDateTime updatedAt;
}


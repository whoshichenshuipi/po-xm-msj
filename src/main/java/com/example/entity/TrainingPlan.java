package com.example.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 培训计划实体
 * 
 * @author example
 * @since 1.0.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("training_plan")
public class TrainingPlan {

    @TableId(type = IdType.AUTO)
    private Long id;

    @NotBlank(message = "培训主题不能为空")
    private String title;

    /**
     * 培训类型：SKILL_TRAINING(技艺培训), CONTENT_CREATION(文化内容), PLANNING(策划培训)
     */
    private String type;

    /**
     * 培训描述
     */
    private String description;

    /**
     * 培训内容
     */
    private String content;

    /**
     * 培训讲师
     */
    private String trainer;

    /**
     * 培训日期
     */
    private LocalDate trainingDate;

    /**
     * 培训地点
     */
    private String location;

    /**
     * 培训容量
     */
    private Integer capacity;

    /**
     * 已报名人数
     */
    private Integer registeredCount;

    /**
     * 创建人ID
     */
    private Long createdBy;

    /**
     * 状态：DRAFT(草稿), PUBLISHED(已发布), FINISHED(已结束)
     */
    private String status;

    /**
     * 创建时间
     */
    private LocalDateTime createdAt;

    /**
     * 更新时间
     */
    private LocalDateTime updatedAt;
}


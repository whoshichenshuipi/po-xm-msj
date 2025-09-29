package com.example.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 路线详情实体类
 * 
 * @author example
 * @since 1.0.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("route_details")
public class RouteDetail {

    @TableId(type = IdType.AUTO)
    private Long id;
    
    /**
     * 路线ID
     */
    private Long routeId;
    
    /**
     * 路线名称
     */
    private String routeName;
    
    /**
     * 路线描述
     */
    private String description;
    
    /**
     * 路线类型（文化主题）
     */
    private String routeType;
    
    /**
     * 总距离（米）
     */
    private Integer totalDistance;
    
    /**
     * 预计时长（分钟）
     */
    private Integer estimatedDuration;
    
    /**
     * 路线难度等级（1-5）
     */
    private Integer difficultyLevel;
    
    /**
     * 路线评分（1-5）
     */
    private Double rating;
    
    /**
     * 路线状态（ACTIVE, INACTIVE, MAINTENANCE）
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
    
    /**
     * 创建者ID
     */
    private Long createdBy;
}

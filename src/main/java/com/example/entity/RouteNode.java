package com.example.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 路线节点实体类
 * 
 * @author example
 * @since 1.0.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("route_nodes")
public class RouteNode {

    @TableId(type = IdType.AUTO)
    private Long id;
    
    /**
     * 路线ID
     */
    private Long routeId;
    
    /**
     * 节点顺序
     */
    private Integer nodeOrder;
    
    /**
     * 节点名称
     */
    private String nodeName;
    
    /**
     * 节点描述
     */
    private String description;
    
    /**
     * 节点类型（MERCHANT, ATTRACTION, REST_AREA, VIEWPOINT）
     */
    private String nodeType;
    
    /**
     * 关联的商户ID（如果是商户节点）
     */
    private Long merchantId;
    
    /**
     * 关联的景点ID（如果是景点节点）
     */
    private Long attractionId;
    
    /**
     * 经度
     */
    private Double longitude;
    
    /**
     * 纬度
     */
    private Double latitude;
    
    /**
     * 停留时间（分钟）
     */
    private Integer stayDuration;
    
    /**
     * 节点状态（ACTIVE, INACTIVE, MAINTENANCE）
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

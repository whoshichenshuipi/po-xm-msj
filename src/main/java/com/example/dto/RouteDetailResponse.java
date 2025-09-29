package com.example.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 路线详情响应DTO
 * 
 * @author example
 * @since 1.0.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RouteDetailResponse {

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
     * 路线类型
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
     * 路线难度等级
     */
    private Integer difficultyLevel;
    
    /**
     * 路线评分
     */
    private Double rating;
    
    /**
     * 路线状态
     */
    private String status;
    
    /**
     * 路线节点列表
     */
    private List<RouteNodeInfo> nodes;
    
    /**
     * 路线统计信息
     */
    private RouteStatistics statistics;
    
    /**
     * 创建时间
     */
    private LocalDateTime createdAt;
    
    /**
     * 更新时间
     */
    private LocalDateTime updatedAt;
    
    /**
     * 路线节点信息
     */
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class RouteNodeInfo {
        private Long nodeId;
        private Integer nodeOrder;
        private String nodeName;
        private String description;
        private String nodeType;
        private Long merchantId;
        private Long attractionId;
        private Double longitude;
        private Double latitude;
        private Integer stayDuration;
        private String status;
        private MerchantInfo merchantInfo;
        private AttractionInfo attractionInfo;
    }
    
    /**
     * 商户信息
     */
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class MerchantInfo {
        private Long id;
        private String name;
        private String description;
        private String category;
        private String address;
        private String phone;
        private String imageUrl;
        private Double rating;
        private String status;
    }
    
    /**
     * 景点信息
     */
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class AttractionInfo {
        private Long id;
        private String name;
        private String description;
        private String category;
        private String address;
        private String imageUrl;
        private Double rating;
        private String status;
    }
    
    /**
     * 路线统计信息
     */
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class RouteStatistics {
        private Integer totalNodes;
        private Integer merchantNodes;
        private Integer attractionNodes;
        private Integer totalMerchants;
        private Integer totalAttractions;
        private Double averageRating;
        private Integer totalVisitors;
        private Integer completionRate;
    }
}

package com.example.service;

import com.example.dto.RouteDetailResponse;
import com.example.entity.RouteDetail;
import com.example.entity.RouteNode;
import com.example.mapper.RouteDetailMapper;
import com.example.mapper.RouteNodeMapper;
import com.example.mapper.MerchantMapper;
import com.example.mapper.VenueMapper;
import com.example.entity.Merchant;
import com.example.entity.Venue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 路线详情服务类
 * 
 * @author example
 * @since 1.0.0
 */
@Service
@Transactional
public class RouteDetailService {

    @Autowired
    private RouteDetailMapper routeDetailMapper;

    @Autowired
    private RouteNodeMapper routeNodeMapper;

    @Autowired
    private MerchantMapper merchantMapper;

    @Autowired
    private VenueMapper venueMapper;

    /**
     * 根据路线ID获取路线详情
     */
    public RouteDetailResponse getRouteDetailById(Long routeId) {
        // 查询路线基本信息
        RouteDetail routeDetail = routeDetailMapper.findByRouteId(routeId);
        if (routeDetail == null) {
            return null;
        }

        // 查询路线节点
        List<RouteNode> nodes = routeNodeMapper.findByRouteId(routeId);

        // 构建响应对象
        RouteDetailResponse response = new RouteDetailResponse();
        response.setRouteId(routeDetail.getRouteId());
        response.setRouteName(routeDetail.getRouteName());
        response.setDescription(routeDetail.getDescription());
        response.setRouteType(routeDetail.getRouteType());
        response.setTotalDistance(routeDetail.getTotalDistance());
        response.setEstimatedDuration(routeDetail.getEstimatedDuration());
        response.setDifficultyLevel(routeDetail.getDifficultyLevel());
        response.setRating(routeDetail.getRating());
        response.setStatus(routeDetail.getStatus());
        response.setCreatedAt(routeDetail.getCreatedAt());
        response.setUpdatedAt(routeDetail.getUpdatedAt());

        // 构建节点信息
        List<RouteDetailResponse.RouteNodeInfo> nodeInfos = nodes.stream()
                .map(this::buildNodeInfo)
                .collect(Collectors.toList());
        response.setNodes(nodeInfos);

        // 构建统计信息
        RouteDetailResponse.RouteStatistics statistics = buildStatistics(routeDetail, nodes);
        response.setStatistics(statistics);

        return response;
    }

    /**
     * 根据路线类型获取路线列表
     */
    public List<RouteDetailResponse> getRoutesByType(String routeType) {
        List<RouteDetail> routes = routeDetailMapper.findByRouteType(routeType);
        return routes.stream()
                .map(route -> getRouteDetailById(route.getRouteId()))
                .collect(Collectors.toList());
    }

    /**
     * 获取热门路线
     */
    public List<RouteDetailResponse> getPopularRoutes(Integer limit) {
        List<RouteDetail> routes = routeDetailMapper.findPopularRoutes(limit);
        return routes.stream()
                .map(route -> getRouteDetailById(route.getRouteId()))
                .collect(Collectors.toList());
    }

    /**
     * 构建节点信息
     */
    private RouteDetailResponse.RouteNodeInfo buildNodeInfo(RouteNode node) {
        RouteDetailResponse.RouteNodeInfo nodeInfo = new RouteDetailResponse.RouteNodeInfo();
        nodeInfo.setNodeId(node.getId());
        nodeInfo.setNodeOrder(node.getNodeOrder());
        nodeInfo.setNodeName(node.getNodeName());
        nodeInfo.setDescription(node.getDescription());
        nodeInfo.setNodeType(node.getNodeType());
        nodeInfo.setMerchantId(node.getMerchantId());
        nodeInfo.setAttractionId(node.getAttractionId());
        nodeInfo.setLongitude(node.getLongitude());
        nodeInfo.setLatitude(node.getLatitude());
        nodeInfo.setStayDuration(node.getStayDuration());
        nodeInfo.setStatus(node.getStatus());

        // 如果是商户节点，查询商户信息
        if (node.getMerchantId() != null) {
            Merchant merchant = merchantMapper.selectById(node.getMerchantId());
            if (merchant != null) {
                RouteDetailResponse.MerchantInfo merchantInfo = new RouteDetailResponse.MerchantInfo();
                merchantInfo.setId(merchant.getId());
                merchantInfo.setName(merchant.getName());
                merchantInfo.setDescription(merchant.getDescription());
                merchantInfo.setCategory(merchant.getCulturePositioning());
                merchantInfo.setAddress(merchant.getAddress());
                merchantInfo.setPhone(merchant.getContactPhone());
                merchantInfo.setImageUrl(null); // Merchant实体中没有imageUrl字段
                merchantInfo.setRating(0.0); // Merchant实体中没有rating字段
                merchantInfo.setStatus(merchant.getApproved() ? "APPROVED" : "PENDING");
                nodeInfo.setMerchantInfo(merchantInfo);
            }
        }

        // 如果是景点节点，查询景点信息
        if (node.getAttractionId() != null) {
            Venue venue = venueMapper.selectById(node.getAttractionId());
            if (venue != null) {
                RouteDetailResponse.AttractionInfo attractionInfo = new RouteDetailResponse.AttractionInfo();
                attractionInfo.setId(venue.getId());
                attractionInfo.setName(venue.getName());
                attractionInfo.setDescription(venue.getDescription());
                attractionInfo.setCategory(venue.getType());
                attractionInfo.setAddress(null); // Venue实体中没有address字段
                attractionInfo.setImageUrl(null); // Venue实体中没有imageUrl字段
                attractionInfo.setRating(0.0); // Venue实体中没有rating字段
                attractionInfo.setStatus(venue.getStatus());
                nodeInfo.setAttractionInfo(attractionInfo);
            }
        }

        return nodeInfo;
    }

    /**
     * 构建统计信息
     */
    private RouteDetailResponse.RouteStatistics buildStatistics(RouteDetail routeDetail, List<RouteNode> nodes) {
        RouteDetailResponse.RouteStatistics statistics = new RouteDetailResponse.RouteStatistics();
        
        statistics.setTotalNodes(nodes.size());
        statistics.setMerchantNodes((int) nodes.stream().filter(n -> "MERCHANT".equals(n.getNodeType())).count());
        statistics.setAttractionNodes((int) nodes.stream().filter(n -> "ATTRACTION".equals(n.getNodeType())).count());
        
        // 统计商户和景点数量
        List<Long> merchantIds = nodes.stream()
                .filter(n -> n.getMerchantId() != null)
                .map(RouteNode::getMerchantId)
                .distinct()
                .collect(Collectors.toList());
        statistics.setTotalMerchants(merchantIds.size());

        List<Long> attractionIds = nodes.stream()
                .filter(n -> n.getAttractionId() != null)
                .map(RouteNode::getAttractionId)
                .distinct()
                .collect(Collectors.toList());
        statistics.setTotalAttractions(attractionIds.size());

        statistics.setAverageRating(routeDetail.getRating());
        statistics.setTotalVisitors(0); // 这里可以从访问记录表统计
        statistics.setCompletionRate(85); // 这里可以从用户完成记录统计

        return statistics;
    }
}

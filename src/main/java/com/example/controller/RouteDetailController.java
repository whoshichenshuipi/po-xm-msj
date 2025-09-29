package com.example.controller;

import com.example.dto.RouteDetailResponse;
import com.example.service.RouteDetailService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;

/**
 * 路线详情控制器
 * 
 * @author example
 * @since 1.0.0
 */
@RestController
@RequestMapping("/routes")
@CrossOrigin(origins = "*")
@Tag(name = "路线详情", description = "美食街路线详情管理")
public class RouteDetailController {

    @Autowired
    private RouteDetailService routeDetailService;

    /**
     * 根据路线ID获取路线详情
     */
    @GetMapping("/{routeId}")
    @Operation(summary = "获取路线详情", description = "根据路线ID获取完整的路线详情信息")
    public ResponseEntity<RouteDetailResponse> getRouteDetail(
            @Parameter(description = "路线ID", required = true)
            @PathVariable Long routeId) {
        
        RouteDetailResponse routeDetail = routeDetailService.getRouteDetailById(routeId);
        if (routeDetail == null) {
            return ResponseEntity.notFound().build();
        }
        
        return ResponseEntity.ok(routeDetail);
    }

    /**
     * 根据路线类型获取路线列表
     */
    @GetMapping("/type/{routeType}")
    @Operation(summary = "根据类型获取路线", description = "根据路线类型获取路线列表")
    public ResponseEntity<List<RouteDetailResponse>> getRoutesByType(
            @Parameter(description = "路线类型", required = true)
            @PathVariable String routeType) {
        
        List<RouteDetailResponse> routes = routeDetailService.getRoutesByType(routeType);
        return ResponseEntity.ok(routes);
    }

    /**
     * 获取热门路线
     */
    @GetMapping("/popular")
    @Operation(summary = "获取热门路线", description = "获取评分最高的热门路线")
    public ResponseEntity<List<RouteDetailResponse>> getPopularRoutes(
            @Parameter(description = "返回数量限制", example = "10")
            @RequestParam(defaultValue = "10") Integer limit) {
        
        List<RouteDetailResponse> routes = routeDetailService.getPopularRoutes(limit);
        return ResponseEntity.ok(routes);
    }

    /**
     * 获取所有路线类型
     */
    @GetMapping("/types")
    @Operation(summary = "获取路线类型", description = "获取所有可用的路线类型")
    public ResponseEntity<List<String>> getRouteTypes() {
        List<String> types = Arrays.asList(
            "传统美食", "现代创意", "文化体验", "夜宵美食", 
            "素食健康", "甜品小食", "特色饮品", "地方特色"
        );
        return ResponseEntity.ok(types);
    }
}

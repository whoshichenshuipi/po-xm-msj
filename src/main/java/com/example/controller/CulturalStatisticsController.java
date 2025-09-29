package com.example.controller;

import com.example.service.CulturalStatisticsService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/cultural-statistics")
@CrossOrigin(origins = "*")
@Slf4j
@Tag(name = "文化内容统计报表", description = "文化内容的各类统计数据和报表")
public class CulturalStatisticsController {

    @Autowired
    private CulturalStatisticsService statisticsService;

    @Operation(summary = "获取总体统计信息")
    @GetMapping("/overall")
    public ResponseEntity<?> getOverallStats() {
        log.info("获取总体统计信息");
        try {
            Map<String, Object> stats = statisticsService.getOverallStats();
            return ResponseEntity.ok(stats);
        } catch (Exception e) {
            log.error("获取总体统计信息失败", e);
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("error", "获取统计信息失败");
            errorResponse.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

    @Operation(summary = "获取按类型统计")
    @GetMapping("/by-type")
    public ResponseEntity<?> getTypeStatistics() {
        log.info("获取按类型统计信息");
        try {
            Map<String, Object> stats = statisticsService.getTypeStatistics();
            return ResponseEntity.ok(stats);
        } catch (Exception e) {
            log.error("获取按类型统计信息失败", e);
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("error", "获取统计信息失败");
            errorResponse.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

    @Operation(summary = "获取时间趋势统计")
    @GetMapping("/trend")
    public ResponseEntity<?> getTrendStatistics(@RequestParam(defaultValue = "monthly") String period) {
        log.info("获取时间趋势统计，周期: {}", period);
        try {
            Map<String, Object> stats = statisticsService.getTrendStatistics(period);
            return ResponseEntity.ok(stats);
        } catch (Exception e) {
            log.error("获取时间趋势统计失败", e);
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("error", "获取统计信息失败");
            errorResponse.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

    @Operation(summary = "获取热门内容统计")
    @GetMapping("/popular")
    public ResponseEntity<?> getPopularContentStats(@RequestParam(defaultValue = "10") int limit) {
        log.info("获取热门内容统计，数量: {}", limit);
        try {
            Map<String, Object> stats = statisticsService.getPopularContentStats(limit);
            return ResponseEntity.ok(stats);
        } catch (Exception e) {
            log.error("获取热门内容统计失败", e);
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("error", "获取统计信息失败");
            errorResponse.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

    @Operation(summary = "获取用户行为统计")
    @GetMapping("/user-behavior")
    public ResponseEntity<?> getUserBehaviorStats() {
        log.info("获取用户行为统计");
        try {
            Map<String, Object> stats = statisticsService.getUserBehaviorStats();
            return ResponseEntity.ok(stats);
        } catch (Exception e) {
            log.error("获取用户行为统计失败", e);
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("error", "获取统计信息失败");
            errorResponse.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

    @Operation(summary = "获取标签统计")
    @GetMapping("/tags")
    public ResponseEntity<?> getTagStatistics() {
        log.info("获取标签统计");
        try {
            Map<String, Object> stats = statisticsService.getTagStatistics();
            return ResponseEntity.ok(stats);
        } catch (Exception e) {
            log.error("获取标签统计失败", e);
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("error", "获取统计信息失败");
            errorResponse.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

    @Operation(summary = "获取完整统计报表")
    @GetMapping("/dashboard")
    public ResponseEntity<?> getDashboardStats() {
        log.info("获取完整统计报表");
        try {
            Map<String, Object> dashboard = new HashMap<>();
            
            // 总体统计
            dashboard.put("overall", statisticsService.getOverallStats());
            
            // 类型统计
            dashboard.put("byType", statisticsService.getTypeStatistics());
            
            // 热门内容
            dashboard.put("popular", statisticsService.getPopularContentStats(5));
            
            // 用户行为
            dashboard.put("userBehavior", statisticsService.getUserBehaviorStats());
            
            // 标签统计
            dashboard.put("tags", statisticsService.getTagStatistics());
            
            // 时间趋势
            dashboard.put("trend", statisticsService.getTrendStatistics("monthly"));
            
            return ResponseEntity.ok(dashboard);
        } catch (Exception e) {
            log.error("获取完整统计报表失败", e);
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("error", "获取统计报表失败");
            errorResponse.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }
}

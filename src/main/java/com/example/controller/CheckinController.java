package com.example.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.entity.Checkin;
import com.example.service.CheckinService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.Map;
import java.util.List;
import java.util.ArrayList;

@RestController
@RequestMapping("/checkins")
@Slf4j
@CrossOrigin(origins = "*")
@Tag(name = "文化打卡", description = "文化景点打卡、活动参与记录")
public class CheckinController {

    @Autowired
    private CheckinService service;

    @PostMapping
    @Operation(summary = "创建打卡记录", description = "创建新的文化打卡记录")
    public ResponseEntity<?> create(@Valid @RequestBody Checkin checkin, BindingResult bindingResult) {
        try {
            log.info("=== 开始创建打卡记录 ===");
            log.info("接收到的打卡数据: {}", checkin);
            
            // 检查验证错误
            if (bindingResult.hasErrors()) {
                log.error("❌ 数据验证失败");
                Map<String, Object> errorResponse = new HashMap<>();
                errorResponse.put("status", "error");
                errorResponse.put("message", "数据验证失败");
                
                List<Map<String, String>> errors = new ArrayList<>();
                for (FieldError error : bindingResult.getFieldErrors()) {
                    Map<String, String> errorDetail = new HashMap<>();
                    errorDetail.put("field", error.getField());
                    errorDetail.put("message", error.getDefaultMessage());
                    errorDetail.put("rejectedValue", String.valueOf(error.getRejectedValue()));
                    errors.add(errorDetail);
                    log.error("字段验证错误 - 字段: {}, 消息: {}, 值: {}", 
                            error.getField(), error.getDefaultMessage(), error.getRejectedValue());
                }
                errorResponse.put("errors", errors);
                
                return ResponseEntity.badRequest().body(errorResponse);
            }
            
            // 数据预处理
            if (checkin.getUserId() == null) {
                log.warn("⚠️ 用户ID为空，设置为默认值1");
                checkin.setUserId(1L);
            }
            
            if (checkin.getCheckinTime() == null) {
                log.warn("⚠️ 打卡时间为空，设置为当前时间");
                checkin.setCheckinTime(java.time.LocalDateTime.now());
            }
            
            // 创建打卡记录
            Checkin created = service.create(checkin);
            log.info("✅ 创建打卡记录成功: {}", created.toString());
            
            Map<String, Object> successResponse = new HashMap<>();
            successResponse.put("status", "success");
            successResponse.put("message", "创建打卡记录成功");
            successResponse.put("data", created);
            
            return ResponseEntity.status(HttpStatus.CREATED).body(successResponse);
            
        } catch (Exception e) {
            log.error("❌ 创建打卡记录失败", e);
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("status", "error");
            errorResponse.put("message", "创建打卡记录失败: " + e.getMessage());
            errorResponse.put("error", e.getClass().getSimpleName());
            
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

    @PutMapping("/{id}")
    @Operation(summary = "更新打卡记录", description = "更新指定的打卡记录")
    public ResponseEntity<Checkin> update(@PathVariable Long id, @Valid @RequestBody Checkin checkin) {
        checkin.setId(id);
        Checkin updated = service.update(checkin);
        log.info("更新打卡记录: {}", updated.toString());
        return ResponseEntity.ok(updated);
    }

    @GetMapping("/{id}")
    @Operation(summary = "获取打卡详情", description = "根据ID获取打卡记录详情")
    public ResponseEntity<Checkin> getById(@PathVariable Long id) {
        Checkin checkin = service.getById(id);
        return ResponseEntity.ok(checkin);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "删除打卡记录", description = "删除指定的打卡记录")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        log.info("删除打卡记录: {}", id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    @Operation(summary = "分页查询打卡记录", description = "根据条件分页查询打卡记录")
    public ResponseEntity<Page<Checkin>> page(
            @RequestParam(required = false) Long userId,
            @RequestParam(required = false) Long merchantId,
            @RequestParam(required = false) String type,
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) String locationTag,
            @RequestParam(required = false) String startDate,
            @RequestParam(required = false) String endDate,
            @RequestParam(defaultValue = "1") int pageNo,
            @RequestParam(defaultValue = "10") int pageSize) {
        
        try {
            // 参数验证和调整
            if (pageNo < 1) {
                pageNo = 1;
                log.warn("页码参数无效，调整为1");
            }
            if (pageSize < 1 || pageSize > 100) {
                pageSize = Math.min(Math.max(pageSize, 1), 100);
                log.warn("页面大小参数无效，调整为: {}", pageSize);
            }
            
            log.info("查询打卡记录 - userId: {}, merchantId: {}, type: {}, keyword: {}, locationTag: {}, startDate: {}, endDate: {}, pageNo: {}, pageSize: {}", 
                    userId, merchantId, type, keyword, locationTag, startDate, endDate, pageNo, pageSize);
            
            Page<Checkin> result = service.page(userId, merchantId, type, keyword, locationTag, startDate, endDate, pageNo, pageSize);
            
            log.info("查询成功 - 总记录数: {}, 当前页记录数: {}", result.getTotal(), result.getRecords().size());
            return ResponseEntity.ok(result);
            
        } catch (Exception e) {
            log.error("查询打卡记录失败", e);
            // 返回空的分页结果而不是抛出异常
            Page<Checkin> emptyPage = new Page<>(pageNo, pageSize);
            return ResponseEntity.ok(emptyPage);
        }
    }

    @PostMapping("/init-data")
    @Operation(summary = "初始化测试数据", description = "手动初始化打卡测试数据")
    public ResponseEntity<Map<String, Object>> initTestData() {
        log.info("开始初始化测试数据...");
        Map<String, Object> result = service.initTestData();
        return ResponseEntity.ok(result);
    }

    @GetMapping("/test-db")
    @Operation(summary = "测试数据库连接", description = "测试数据库连接和基本查询功能")
    public ResponseEntity<Map<String, Object>> testDatabase() {
        log.info("测试数据库连接...");
        Map<String, Object> result = service.testDatabaseConnection();
        return ResponseEntity.ok(result);
    }

    @GetMapping("/stats/{userId}")
    @Operation(summary = "获取用户打卡统计", description = "获取指定用户的打卡统计数据")
    public ResponseEntity<Map<String, Object>> getUserStats(@PathVariable Long userId) {
        log.info("获取用户打卡统计: {}", userId);
        Map<String, Object> stats = service.getUserStats(userId);
        return ResponseEntity.ok(stats);
    }
}



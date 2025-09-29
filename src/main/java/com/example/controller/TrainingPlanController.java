package com.example.controller;

import com.example.annotation.RequirePermission;
import com.example.entity.TrainingPlan;
import com.example.entity.UserRole;
import com.example.service.TrainingPlanService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 培训计划控制器
 * 
 * @author example
 * @since 1.0.0
 */
@RestController
@RequestMapping("/trainings")
@CrossOrigin(origins = "*")
@Tag(name = "培训计划管理", description = "文化培训计划管理")
public class TrainingPlanController {

    @Autowired
    private TrainingPlanService trainingService;

    /**
     * 获取培训列表
     */
    @GetMapping
    @RequirePermission(roles = {UserRole.ADMIN, UserRole.MERCHANT}, description = "管理员和商户可以查看培训列表")
    @Operation(summary = "获取培训列表", description = "获取培训计划列表")
    public ResponseEntity<List<TrainingPlan>> list(
            @RequestParam(required = false) String type,
            @RequestParam(required = false) String status) {
        
        List<TrainingPlan> list = trainingService.list(type, status);
        return ResponseEntity.ok(list);
    }

    /**
     * 获取培训详情
     */
    @GetMapping("/{id}")
    @RequirePermission(roles = {UserRole.ADMIN, UserRole.MERCHANT}, description = "管理员和商户可以查看培训详情")
    @Operation(summary = "获取培训详情", description = "根据ID获取培训详情")
    public ResponseEntity<TrainingPlan> getById(@PathVariable Long id) {
        TrainingPlan training = trainingService.findById(id);
        if (training == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(training);
    }

    /**
     * 创建培训计划
     */
    @PostMapping
    @RequirePermission(roles = {UserRole.ADMIN}, description = "只有管理员可以创建培训计划")
    @Operation(summary = "创建培训计划", description = "管理员创建培训计划")
    public ResponseEntity<TrainingPlan> create(
            @RequestBody TrainingPlan training,
            HttpSession session) {
        
        Long currentUserId = (Long) session.getAttribute("userId");
        training.setCreatedBy(currentUserId);
        training.setCreatedAt(LocalDateTime.now());
        training.setUpdatedAt(LocalDateTime.now());
        
        TrainingPlan created = trainingService.create(training);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    /**
     * 更新培训计划
     */
    @PutMapping("/{id}")
    @RequirePermission(roles = {UserRole.ADMIN}, description = "只有管理员可以更新培训计划")
    @Operation(summary = "更新培训计划", description = "管理员更新培训计划")
    public ResponseEntity<TrainingPlan> update(
            @PathVariable Long id,
            @RequestBody TrainingPlan training) {
        
        training.setId(id);
        training.setUpdatedAt(LocalDateTime.now());
        
        TrainingPlan updated = trainingService.update(training);
        return ResponseEntity.ok(updated);
    }

    /**
     * 删除培训计划
     */
    @DeleteMapping("/{id}")
    @RequirePermission(roles = {UserRole.ADMIN}, description = "只有管理员可以删除培训计划")
    @Operation(summary = "删除培训计划", description = "删除培训计划")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        trainingService.delete(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * 报名培训
     */
    @PostMapping("/{id}/register")
    @RequirePermission(roles = {UserRole.MERCHANT}, description = "商户可以报名培训")
    @Operation(summary = "报名培训", description = "商户报名参加培训")
    public ResponseEntity<String> register(@PathVariable Long id) {
        try {
            trainingService.register(id);
            return ResponseEntity.ok("报名成功");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}


package com.example.controller;

import com.example.entity.CulturalCategory;
import com.example.service.CulturalCategoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/cultural-categories")
@CrossOrigin(origins = "*")
@Slf4j
@Tag(name = "文化内容分类管理", description = "文化内容分类的增删改查和状态管理")
public class CulturalCategoryController {

    @Autowired
    private CulturalCategoryService service;

    @Operation(summary = "创建文化内容分类")
    @PostMapping
    public ResponseEntity<?> create(@Valid @RequestBody CulturalCategory category, BindingResult bindingResult) {
        log.info("接收到创建分类请求: {}", category);
        
        if (bindingResult.hasErrors()) {
            Map<String, String> errors = new HashMap<>();
            bindingResult.getFieldErrors().forEach(error -> {
                errors.put(error.getField(), error.getDefaultMessage());
            });
            log.warn("分类创建验证失败: {}", errors);
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("error", "数据验证失败");
            errorResponse.put("details", errors);
            return ResponseEntity.badRequest().body(errorResponse);
        }
        
        try {
            // 检查分类名称是否已存在
            if (service.findByName(category.getName()).isPresent()) {
                Map<String, Object> errorResponse = new HashMap<>();
                errorResponse.put("error", "分类名称已存在");
                return ResponseEntity.badRequest().body(errorResponse);
            }
            
            CulturalCategory created = service.create(category);
            log.info("分类创建成功，ID: {}", created.getId());
            return ResponseEntity.status(HttpStatus.CREATED).body(created);
            
        } catch (Exception e) {
            log.error("创建分类时发生错误", e);
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("error", "服务器内部错误");
            errorResponse.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

    @Operation(summary = "根据ID获取分类")
    @GetMapping("/{id}")
    public ResponseEntity<?> findById(@PathVariable Long id) {
        Optional<CulturalCategory> category = service.findById(id);
        return category.<ResponseEntity<?>>map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @Operation(summary = "获取所有启用的分类")
    @GetMapping("/enabled")
    public ResponseEntity<List<CulturalCategory>> findAllEnabled() {
        return ResponseEntity.ok(service.findAllEnabled());
    }

    @Operation(summary = "获取所有分类")
    @GetMapping
    public ResponseEntity<List<CulturalCategory>> findAll() {
        return ResponseEntity.ok(service.findAll());
    }

    @Operation(summary = "更新分类")
    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable Long id, @Valid @RequestBody CulturalCategory category, BindingResult bindingResult) {
        log.info("接收到更新分类请求，ID: {}, 分类: {}", id, category);
        
        if (bindingResult.hasErrors()) {
            Map<String, String> errors = new HashMap<>();
            bindingResult.getFieldErrors().forEach(error -> {
                errors.put(error.getField(), error.getDefaultMessage());
            });
            log.warn("分类更新验证失败: {}", errors);
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("error", "数据验证失败");
            errorResponse.put("details", errors);
            return ResponseEntity.badRequest().body(errorResponse);
        }
        
        try {
            category.setId(id);
            CulturalCategory updated = service.update(category);
            log.info("分类更新成功，ID: {}", updated.getId());
            return ResponseEntity.ok(updated);
        } catch (RuntimeException e) {
            log.error("更新分类时发生错误，ID: " + id, e);
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("error", "更新失败");
            errorResponse.put("message", e.getMessage());
            return ResponseEntity.badRequest().body(errorResponse);
        }
    }

    @Operation(summary = "删除分类")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "切换分类启用状态")
    @PostMapping("/{id}/toggle")
    public ResponseEntity<?> toggleEnabled(@PathVariable Long id) {
        log.info("切换分类启用状态，ID: {}", id);
        try {
            CulturalCategory category = service.toggleEnabled(id);
            log.info("分类状态切换成功，ID: {}, 状态: {}", id, category.getEnabled());
            return ResponseEntity.ok(category);
        } catch (RuntimeException e) {
            log.error("切换分类状态失败，ID: " + id, e);
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("error", "状态切换失败");
            errorResponse.put("message", e.getMessage());
            return ResponseEntity.badRequest().body(errorResponse);
        }
    }

    @Operation(summary = "更新分类内容数量")
    @PostMapping("/{id}/update-count")
    public ResponseEntity<?> updateContentCount(@PathVariable Long id) {
        log.info("更新分类内容数量，ID: {}", id);
        try {
            service.updateContentCount(id);
            Map<String, Object> successResponse = new HashMap<>();
            successResponse.put("message", "内容数量更新成功");
            return ResponseEntity.ok(successResponse);
        } catch (Exception e) {
            log.error("更新分类内容数量失败，ID: " + id, e);
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("error", "更新失败");
            errorResponse.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }
}

package com.example.controller;

import com.example.entity.CulturalTag;
import com.example.service.CulturalTagService;
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
@RequestMapping("/cultural-tags")
@CrossOrigin(origins = "*")
@Slf4j
@Tag(name = "文化内容标签管理", description = "文化内容标签的增删改查和使用统计")
public class CulturalTagController {

    @Autowired
    private CulturalTagService service;

    @Operation(summary = "创建文化内容标签")
    @PostMapping
    public ResponseEntity<?> create(@Valid @RequestBody CulturalTag tag, BindingResult bindingResult) {
        log.info("接收到创建标签请求: {}", tag);
        
        if (bindingResult.hasErrors()) {
            Map<String, String> errors = new HashMap<>();
            bindingResult.getFieldErrors().forEach(error -> {
                errors.put(error.getField(), error.getDefaultMessage());
            });
            log.warn("标签创建验证失败: {}", errors);
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("error", "数据验证失败");
            errorResponse.put("details", errors);
            return ResponseEntity.badRequest().body(errorResponse);
        }
        
        try {
            // 检查标签名称是否已存在
            if (service.findByName(tag.getName()).isPresent()) {
                Map<String, Object> errorResponse = new HashMap<>();
                errorResponse.put("error", "标签名称已存在");
                return ResponseEntity.badRequest().body(errorResponse);
            }
            
            CulturalTag created = service.create(tag);
            log.info("标签创建成功，ID: {}", created.getId());
            return ResponseEntity.status(HttpStatus.CREATED).body(created);
            
        } catch (Exception e) {
            log.error("创建标签时发生错误", e);
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("error", "服务器内部错误");
            errorResponse.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

    @Operation(summary = "根据ID获取标签")
    @GetMapping("/{id}")
    public ResponseEntity<?> findById(@PathVariable Long id) {
        Optional<CulturalTag> tag = service.findById(id);
        return tag.<ResponseEntity<?>>map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @Operation(summary = "获取所有启用的标签")
    @GetMapping("/enabled")
    public ResponseEntity<List<CulturalTag>> findAllEnabled() {
        return ResponseEntity.ok(service.findAllEnabled());
    }

    @Operation(summary = "获取所有标签")
    @GetMapping
    public ResponseEntity<List<CulturalTag>> findAll() {
        return ResponseEntity.ok(service.findAll());
    }

    //@Operation(summary = "获取热门标签")
    //@GetMapping("/popular")
    //public ResponseEntity<List<CulturalTag>> getPopularTags(@RequestParam(defaultValue = "10") int limit) {
     //   return ResponseEntity.ok(service.findTopUsedTags(limit));
   // }

    @Operation(summary = "搜索标签")
    @GetMapping("/search")
    public ResponseEntity<List<CulturalTag>> searchTags(@RequestParam String keyword) {
        return ResponseEntity.ok(service.searchByName(keyword));
    }

    @Operation(summary = "更新标签")
    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable Long id, @Valid @RequestBody CulturalTag tag, BindingResult bindingResult) {
        log.info("接收到更新标签请求，ID: {}, 标签: {}", id, tag);
        
        if (bindingResult.hasErrors()) {
            Map<String, String> errors = new HashMap<>();
            bindingResult.getFieldErrors().forEach(error -> {
                errors.put(error.getField(), error.getDefaultMessage());
            });
            log.warn("标签更新验证失败: {}", errors);
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("error", "数据验证失败");
            errorResponse.put("details", errors);
            return ResponseEntity.badRequest().body(errorResponse);
        }
        
        try {
            tag.setId(id);
            CulturalTag updated = service.update(tag);
            log.info("标签更新成功，ID: {}", updated.getId());
            return ResponseEntity.ok(updated);
        } catch (RuntimeException e) {
            log.error("更新标签时发生错误，ID: " + id, e);
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("error", "更新失败");
            errorResponse.put("message", e.getMessage());
            return ResponseEntity.badRequest().body(errorResponse);
        }
    }

    @Operation(summary = "删除标签")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "切换标签启用状态")
    @PostMapping("/{id}/toggle")
    public ResponseEntity<?> toggleEnabled(@PathVariable Long id) {
        log.info("切换标签启用状态，ID: {}", id);
        try {
            CulturalTag tag = service.toggleEnabled(id);
            log.info("标签状态切换成功，ID: {}, 状态: {}", id, tag.getEnabled());
            return ResponseEntity.ok(tag);
        } catch (RuntimeException e) {
            log.error("切换标签状态失败，ID: " + id, e);
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("error", "状态切换失败");
            errorResponse.put("message", e.getMessage());
            return ResponseEntity.badRequest().body(errorResponse);
        }
    }

    @Operation(summary = "更新标签使用次数")
    @PostMapping("/{id}/update-usage")
    public ResponseEntity<?> updateUsageCount(@PathVariable Long id) {
        log.info("更新标签使用次数，ID: {}", id);
        try {
            Optional<CulturalTag> tagOpt = service.findById(id);
            if (tagOpt.isPresent()) {
                service.updateUsageCount(tagOpt.get().getName());
                Map<String, Object> successResponse = new HashMap<>();
                successResponse.put("message", "使用次数更新成功");
                return ResponseEntity.ok(successResponse);
            } else {
                Map<String, Object> errorResponse = new HashMap<>();
                errorResponse.put("error", "标签不存在");
                return ResponseEntity.badRequest().body(errorResponse);
            }
        } catch (Exception e) {
            log.error("更新标签使用次数失败，ID: " + id, e);
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("error", "更新失败");
            errorResponse.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

    @Operation(summary = "更新所有标签使用次数")
    @PostMapping("/update-all-usage")
    public ResponseEntity<?> updateAllUsageCounts() {
        log.info("更新所有标签使用次数");
        try {
            service.updateAllUsageCounts();
            Map<String, Object> successResponse = new HashMap<>();
            successResponse.put("message", "所有标签使用次数更新成功");
            return ResponseEntity.ok(successResponse);
        } catch (Exception e) {
            log.error("更新所有标签使用次数失败", e);
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("error", "更新失败");
            errorResponse.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }
}

package com.example.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.annotation.RequirePermission;
import com.example.entity.CulturalResource;
import com.example.entity.UserRole;
import com.example.mapper.CulturalResourceMapper;
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
 * 文化资源对接控制器
 * 
 * @author example
 * @since 1.0.0
 */
@RestController
@RequestMapping("/cultural-resources")
@CrossOrigin(origins = "*")
@Tag(name = "文化资源对接", description = "为商户推送文化资源信息")
public class CulturalResourceController {

    @Autowired
    private CulturalResourceMapper resourceMapper;

    /**
     * 获取资源列表
     */
    @GetMapping
    @RequirePermission(roles = {UserRole.ADMIN, UserRole.MERCHANT}, description = "管理员和商户可以查看资源列表")
    @Operation(summary = "获取资源列表", description = "获取文化资源列表")
    public ResponseEntity<List<CulturalResource>> list(
            @RequestParam(required = false) String category,
            @RequestParam(required = false) String status) {
        
        QueryWrapper<CulturalResource> wrapper = new QueryWrapper<>();
        
        if (category != null && !category.isEmpty()) {
            wrapper.eq("category", category);
        }
        
        if (status != null && !status.isEmpty()) {
            wrapper.eq("status", status);
        }
        
        List<CulturalResource> list = resourceMapper.selectList(wrapper);
        return ResponseEntity.ok(list);
    }

    /**
     * 获取资源详情
     */
    @GetMapping("/{id}")
    @RequirePermission(roles = {UserRole.ADMIN, UserRole.MERCHANT}, description = "管理员和商户可以查看资源详情")
    @Operation(summary = "获取资源详情", description = "根据ID获取资源详情")
    public ResponseEntity<CulturalResource> getById(@PathVariable Long id) {
        CulturalResource resource = resourceMapper.selectById(id);
        if (resource == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(resource);
    }

    /**
     * 创建资源
     */
    @PostMapping
    @RequirePermission(roles = {UserRole.ADMIN}, description = "只有管理员可以创建资源")
    @Operation(summary = "创建资源", description = "管理员发布文化资源")
    public ResponseEntity<CulturalResource> create(
            @RequestBody CulturalResource resource,
            HttpSession session) {
        
        resource.setStatus("PUBLISHED");
        Long currentUserId = (Long) session.getAttribute("userId");
        resource.setPublisherId(currentUserId);
        resource.setPublishDate(LocalDateTime.now());
        resource.setCreatedAt(LocalDateTime.now());
        resource.setUpdatedAt(LocalDateTime.now());
        
        resourceMapper.insert(resource);
        return ResponseEntity.status(HttpStatus.CREATED).body(resource);
    }

    /**
     * 更新资源
     */
    @PutMapping("/{id}")
    @RequirePermission(roles = {UserRole.ADMIN}, description = "只有管理员可以更新资源")
    @Operation(summary = "更新资源", description = "管理员更新文化资源")
    public ResponseEntity<CulturalResource> update(
            @PathVariable Long id,
            @RequestBody CulturalResource resource) {
        
        resource.setId(id);
        resource.setUpdatedAt(LocalDateTime.now());
        
        resourceMapper.updateById(resource);
        return ResponseEntity.ok(resource);
    }

    /**
     * 删除资源
     */
    @DeleteMapping("/{id}")
    @RequirePermission(roles = {UserRole.ADMIN}, description = "只有管理员可以删除资源")
    @Operation(summary = "删除资源", description = "删除文化资源")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        resourceMapper.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * 推送资源给商户
     */
    @PostMapping("/{id}/push")
    @RequirePermission(roles = {UserRole.ADMIN}, description = "只有管理员可以推送资源")
    @Operation(summary = "推送资源", description = "将资源推送给目标商户")
    public ResponseEntity<String> push(@PathVariable Long id) {
        // 这里可以实现推送逻辑，比如发送通知给目标商户
        return ResponseEntity.ok("资源已推送");
    }
}


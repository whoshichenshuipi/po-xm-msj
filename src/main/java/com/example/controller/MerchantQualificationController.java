package com.example.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.annotation.RequirePermission;
import com.example.entity.MerchantQualification;
import com.example.entity.UserRole;
import com.example.service.MerchantQualificationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/merchant-qualifications")
@CrossOrigin(origins = "*")
@Tag(name = "商户资质", description = "商户文化资质管理")
public class MerchantQualificationController {

    @Autowired
    private MerchantQualificationService service;

    @PostMapping
    @RequirePermission(roles = {UserRole.ADMIN, UserRole.MERCHANT}, description = "管理员和商户可以创建资质")
    @Operation(summary = "创建资质", description = "创建商户资质")
    public ResponseEntity<MerchantQualification> create(@Valid @RequestBody MerchantQualification q) {
        MerchantQualification created = service.create(q);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @PutMapping("/{id}")
    @RequirePermission(roles = {UserRole.ADMIN, UserRole.MERCHANT}, description = "管理员和商户可以更新资质")
    @Operation(summary = "更新资质", description = "更新商户资质")
    public ResponseEntity<MerchantQualification> update(@PathVariable Long id, @Valid @RequestBody MerchantQualification q) {
        q.setId(id);
        return ResponseEntity.ok(service.update(q));
    }

    @DeleteMapping("/{id}")
    @RequirePermission(roles = {UserRole.ADMIN}, description = "只有管理员可以删除资质")
    @Operation(summary = "删除资质", description = "删除商户资质")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}")
    @RequirePermission(roles = {UserRole.ADMIN, UserRole.MERCHANT, UserRole.CONSUMER}, description = "所有角色可以查看资质详情")
    @Operation(summary = "获取资质详情", description = "根据ID获取资质详情")
    public ResponseEntity<?> findById(@PathVariable Long id) {
        MerchantQualification found = service.findById(id);
        return found == null ? ResponseEntity.notFound().build() : ResponseEntity.ok(found);
    }

    @GetMapping
    @RequirePermission(roles = {UserRole.ADMIN, UserRole.MERCHANT, UserRole.CONSUMER}, description = "所有角色可以查看资质列表")
    @Operation(summary = "获取资质列表", description = "分页查询商户资质列表")
    public ResponseEntity<?> page(
            @RequestParam(required = false) Long merchantId,
            @RequestParam(required = false) String status,
            @RequestParam(required = false) String level,
            @RequestParam(required = false) String keyword,
            @RequestParam(defaultValue = "1") int pageNo,
            @RequestParam(defaultValue = "10") int pageSize) {
        
        Page<MerchantQualification> page = service.page(merchantId, status, level, keyword, pageNo, pageSize);
        
        // 转换为标准响应格式
        Map<String, Object> response = new HashMap<>();
        response.put("records", page.getRecords());
        response.put("total", page.getTotal());
        response.put("current", page.getCurrent());
        response.put("size", page.getSize());
        
        return ResponseEntity.ok(response);
    }
    
    @GetMapping("/{id}/status")
    @Operation(summary = "获取实时状态", description = "获取资质的实时过期状态")
    public ResponseEntity<Map<String, String>> getRealTimeStatus(@PathVariable Long id) {
        String status = service.getRealTimeStatus(id);
        if (status == null) {
            return ResponseEntity.notFound().build();
        }
        
        Map<String, String> response = new HashMap<>();
        response.put("status", status);
        return ResponseEntity.ok(response);
    }
    
    @PostMapping("/update-status")
    @RequirePermission(roles = {UserRole.ADMIN}, description = "只有管理员可以手动更新状态")
    @Operation(summary = "手动更新状态", description = "手动更新所有资质的过期状态")
    public ResponseEntity<Map<String, String>> updateStatus() {
        service.updateExpiredStatus();
        Map<String, String> response = new HashMap<>();
        response.put("message", "状态更新完成");
        return ResponseEntity.ok(response);
    }
}



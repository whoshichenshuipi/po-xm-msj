package com.example.controller;

import com.example.annotation.RequirePermission;
import com.example.entity.TrainingCertificate;
import com.example.entity.UserRole;
import com.example.service.TrainingCertificateService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 培训证书控制器
 * 
 * @author example
 * @since 1.0.0
 */
@RestController
@RequestMapping("/training-certificates")
@CrossOrigin(origins = "*")
@Tag(name = "培训证书管理", description = "培训证书颁发和管理")
public class TrainingCertificateController {

    @Autowired
    private TrainingCertificateService certificateService;

    /**
     * 获取证书列表
     */
    @GetMapping
    @Operation(summary = "获取证书列表", description = "获取培训证书列表")
    public ResponseEntity<List<TrainingCertificate>> list(
            @RequestParam(required = false) Long merchantId) {
        
        List<TrainingCertificate> list = certificateService.list(merchantId);
        return ResponseEntity.ok(list);
    }

    /**
     * 获取证书详情
     */
    @GetMapping("/{id}")
    @Operation(summary = "获取证书详情", description = "根据ID获取证书详情")
    public ResponseEntity<TrainingCertificate> getById(@PathVariable Long id) {
        TrainingCertificate certificate = certificateService.findById(id);
        if (certificate == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(certificate);
    }

    /**
     * 颁发证书
     */
    @PostMapping
    @RequirePermission(roles = {UserRole.ADMIN}, description = "只有管理员可以颁发证书")
    @Operation(summary = "颁发证书", description = "为完成培训的商户颁发证书")
    public ResponseEntity<TrainingCertificate> issue(
            @RequestBody TrainingCertificate certificate) {
        
        certificate.setCreatedAt(LocalDateTime.now());
        certificate.setUpdatedAt(LocalDateTime.now());
        
        TrainingCertificate issued = certificateService.issue(certificate);
        return ResponseEntity.status(HttpStatus.CREATED).body(issued);
    }

    /**
     * 更新证书
     */
    @PutMapping("/{id}")
    @RequirePermission(roles = {UserRole.ADMIN}, description = "只有管理员可以更新证书")
    @Operation(summary = "更新证书", description = "更新证书信息")
    public ResponseEntity<TrainingCertificate> update(
            @PathVariable Long id,
            @RequestBody TrainingCertificate certificate) {
        
        certificate.setId(id);
        certificate.setUpdatedAt(LocalDateTime.now());
        
        TrainingCertificate updated = certificateService.update(certificate);
        return ResponseEntity.ok(updated);
    }

    /**
     * 删除证书
     */
    @DeleteMapping("/{id}")
    @RequirePermission(roles = {UserRole.ADMIN}, description = "只有管理员可以删除证书")
    @Operation(summary = "删除证书", description = "删除证书")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        certificateService.delete(id);
        return ResponseEntity.noContent().build();
    }
}


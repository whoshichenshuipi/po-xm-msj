package com.example.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.annotation.RequirePermission;
import com.example.entity.MerchantApproval;
import com.example.entity.UserRole;
import com.example.mapper.MerchantApprovalMapper;
import com.example.util.PermissionValidator;
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
 * 商户入驻审核控制器
 * 
 * @author example
 * @since 1.0.0
 */
@RestController
@RequestMapping("/merchant-approvals")
@CrossOrigin(origins = "*")
@Tag(name = "商户入驻审核", description = "商户基础信息、文化资质、文化定位审核")
public class MerchantApprovalController {

    @Autowired
    private MerchantApprovalMapper approvalMapper;

    /**
     * 获取审核列表
     */
    @GetMapping
    @Operation(summary = "获取审核列表", description = "根据审核类型获取待审核列表")
    public ResponseEntity<List<MerchantApproval>> list(
            @RequestParam(required = false) String approvalType,
            @RequestParam(required = false) String status) {
        
        // 管理员可以查看所有，商户只能查看自己的
        QueryWrapper<MerchantApproval> wrapper = new QueryWrapper<>();
        
        if (approvalType != null && !approvalType.isEmpty()) {
            wrapper.eq("approval_type", approvalType);
        }
        
        if (status != null && !status.isEmpty()) {
            wrapper.eq("status", status);
        }
        
        List<MerchantApproval> list = approvalMapper.selectList(wrapper);
        return ResponseEntity.ok(list);
    }

    /**
     * 获取审核详情
     */
    @GetMapping("/{id}")
    @Operation(summary = "获取审核详情", description = "根据ID获取审核详情")
    public ResponseEntity<MerchantApproval> getById(@PathVariable Long id) {
        MerchantApproval approval = approvalMapper.selectById(id);
        if (approval == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(approval);
    }

    /**
     * 创建审核申请
     */
    @PostMapping
    @RequirePermission(requireLogin = true, description = "需要登录才能提交审核申请")
    @Operation(summary = "创建审核申请", description = "提交商户入驻审核申请")
    public ResponseEntity<MerchantApproval> create(@RequestBody MerchantApproval approval) {
        approval.setStatus("PENDING");
        approval.setSubmitTime(LocalDateTime.now());
        approval.setCreatedAt(LocalDateTime.now());
        approval.setUpdatedAt(LocalDateTime.now());
        
        approvalMapper.insert(approval);
        return ResponseEntity.status(HttpStatus.CREATED).body(approval);
    }

    /**
     * 通过审核
     */
    @PostMapping("/{id}/approve")
    @RequirePermission(roles = {UserRole.ADMIN}, description = "只有管理员可以审核")
    @Operation(summary = "通过审核", description = "管理员通过商户审核")
    public ResponseEntity<MerchantApproval> approve(
            @PathVariable Long id,
            @RequestParam(required = false) String comment,
            HttpSession session) {
        
        MerchantApproval approval = approvalMapper.selectById(id);
        if (approval == null) {
            return ResponseEntity.notFound().build();
        }
        
        approval.setStatus("APPROVED");
        approval.setReviewComment(comment);
        // 从session获取当前用户ID
        Long currentUserId = (Long) session.getAttribute("userId");
        approval.setReviewerId(currentUserId);
        approval.setReviewedAt(LocalDateTime.now());
        approval.setUpdatedAt(LocalDateTime.now());
        
        approvalMapper.updateById(approval);
        return ResponseEntity.ok(approval);
    }

    /**
     * 驳回审核
     */
    @PostMapping("/{id}/reject")
    @RequirePermission(roles = {UserRole.ADMIN}, description = "只有管理员可以驳回")
    @Operation(summary = "驳回审核", description = "管理员驳回商户审核")
    public ResponseEntity<MerchantApproval> reject(
            @PathVariable Long id,
            @RequestParam String comment,
            HttpSession session) {
        
        MerchantApproval approval = approvalMapper.selectById(id);
        if (approval == null) {
            return ResponseEntity.notFound().build();
        }
        
        approval.setStatus("REJECTED");
        approval.setReviewComment(comment);
        Long currentUserId = (Long) session.getAttribute("userId");
        approval.setReviewerId(currentUserId);
        approval.setReviewedAt(LocalDateTime.now());
        approval.setUpdatedAt(LocalDateTime.now());
        
        approvalMapper.updateById(approval);
        return ResponseEntity.ok(approval);
    }
}


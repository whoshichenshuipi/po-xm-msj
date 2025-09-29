package com.example.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.annotation.RequirePermission;
import com.example.entity.CulturalProduct;
import com.example.entity.UserRole;
import com.example.mapper.CulturalProductMapper;
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
 * 文化产品管理控制器
 * 
 * @author example
 * @since 1.0.0
 */
@RestController
@RequestMapping("/cultural-products")
@CrossOrigin(origins = "*")
@Tag(name = "文化产品管理", description = "文化特色产品信息和文化故事管理")
public class CulturalProductController {

    @Autowired
    private CulturalProductMapper productMapper;

    /**
     * 获取产品列表
     */
    @GetMapping
    @RequirePermission(roles = {UserRole.ADMIN, UserRole.MERCHANT}, description = "管理员和商户可以查看产品列表")
    @Operation(summary = "获取产品列表", description = "获取文化产品列表，支持状态筛选")
    public ResponseEntity<List<CulturalProduct>> list(
            @RequestParam(required = false) Long merchantId,
            @RequestParam(required = false) String status) {
        
        QueryWrapper<CulturalProduct> wrapper = new QueryWrapper<>();
        
        if (merchantId != null) {
            wrapper.eq("merchant_id", merchantId);
        }
        
        if (status != null && !status.isEmpty()) {
            wrapper.eq("status", status);
        }
        
        List<CulturalProduct> list = productMapper.selectList(wrapper);
        return ResponseEntity.ok(list);
    }

    /**
     * 获取产品详情
     */
    @GetMapping("/{id}")
    @RequirePermission(roles = {UserRole.ADMIN, UserRole.MERCHANT}, description = "管理员和商户可以查看产品详情")
    @Operation(summary = "获取产品详情", description = "根据ID获取产品详情")
    public ResponseEntity<CulturalProduct> getById(@PathVariable Long id) {
        CulturalProduct product = productMapper.selectById(id);
        if (product == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(product);
    }

    /**
     * 创建文化产品
     */
    @PostMapping
    @RequirePermission(roles = {UserRole.ADMIN, UserRole.MERCHANT}, description = "管理员和商户可以创建文化产品")
    @Operation(summary = "创建文化产品", description = "管理员和商户提交文化产品信息")
    public ResponseEntity<CulturalProduct> create(@RequestBody CulturalProduct product, HttpSession session) {
        Long currentUserId = (Long) session.getAttribute("userId");
        String currentUserRole = (String) session.getAttribute("userRole");
        
        // 设置商户信息
        if (UserRole.MERCHANT.name().equals(currentUserRole)) {
            product.setMerchantId(currentUserId);
            // 这里可以从用户信息中获取商户名称，暂时使用默认值
            product.setMerchantName("商户" + currentUserId);
        } else if (UserRole.ADMIN.name().equals(currentUserRole)) {
            // 管理员创建时，如果没有指定商户ID，使用默认值
            if (product.getMerchantId() == null) {
                product.setMerchantId(1L); // 默认商户ID
                product.setMerchantName("管理员创建");
            }
        }
        
        product.setStatus("PENDING");
        product.setCreatedAt(LocalDateTime.now());
        product.setUpdatedAt(LocalDateTime.now());
        
        productMapper.insert(product);
        return ResponseEntity.status(HttpStatus.CREATED).body(product);
    }

    /**
     * 审核产品
     */
    @PostMapping("/{id}/approve")
    @RequirePermission(roles = {UserRole.ADMIN}, description = "只有管理员可以审核")
    @Operation(summary = "审核产品", description = "管理员审核文化产品")
    public ResponseEntity<CulturalProduct> approve(
            @PathVariable Long id,
            HttpSession session) {
        
        CulturalProduct product = productMapper.selectById(id);
        if (product == null) {
            return ResponseEntity.notFound().build();
        }
        
        product.setStatus("APPROVED");
        Long currentUserId = (Long) session.getAttribute("userId");
        product.setApprovedBy(currentUserId);
        product.setApprovedAt(LocalDateTime.now());
        product.setUpdatedAt(LocalDateTime.now());
        
        productMapper.updateById(product);
        return ResponseEntity.ok(product);
    }

    /**
     * 驳回产品
     */
    @PostMapping("/{id}/reject")
    @RequirePermission(roles = {UserRole.ADMIN}, description = "只有管理员可以驳回")
    @Operation(summary = "驳回产品", description = "管理员驳回文化产品")
    public ResponseEntity<CulturalProduct> reject(
            @PathVariable Long id,
            HttpSession session) {
        
        CulturalProduct product = productMapper.selectById(id);
        if (product == null) {
            return ResponseEntity.notFound().build();
        }
        
        product.setStatus("REJECTED");
        Long currentUserId = (Long) session.getAttribute("userId");
        product.setApprovedBy(currentUserId);
        product.setApprovedAt(LocalDateTime.now());
        product.setUpdatedAt(LocalDateTime.now());
        
        productMapper.updateById(product);
        return ResponseEntity.ok(product);
    }

    /**
     * 获取已审核通过的产品列表（用于前台展示）
     */
    @GetMapping("/approved")
    @Operation(summary = "获取已通过的产品", description = "获取所有已通过审核的文化产品")
    public ResponseEntity<List<CulturalProduct>> getApprovedProducts() {
        QueryWrapper<CulturalProduct> wrapper = new QueryWrapper<>();
        wrapper.eq("status", "APPROVED");
        wrapper.orderByDesc("approved_at");
        
        List<CulturalProduct> list = productMapper.selectList(wrapper);
        return ResponseEntity.ok(list);
    }

    /**
     * 删除产品
     */
    @DeleteMapping("/{id}")
    @RequirePermission(roles = {UserRole.ADMIN}, description = "只有管理员可以删除")
    @Operation(summary = "删除产品", description = "删除文化产品")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        productMapper.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}


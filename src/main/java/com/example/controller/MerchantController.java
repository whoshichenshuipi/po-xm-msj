package com.example.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.annotation.RequirePermission;
import com.example.entity.Merchant;
import com.example.entity.UserRole;
import com.example.service.MerchantService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/merchants")
@CrossOrigin(origins = "*")
@Tag(name = "商户管理", description = "商户入驻、审核、文化资质管理")
public class MerchantController {

    @Autowired
    private MerchantService merchantService;

    @PostMapping
    @RequirePermission(
        roles = {UserRole.ADMIN, UserRole.MERCHANT}, 
        description = "管理员和商户可以创建商户信息",
        requireOwnership = true,
        ownerIdParam = "userId"
    )
    public ResponseEntity<Merchant> create(@Valid @RequestBody Merchant merchant) {
        Merchant created = merchantService.create(merchant);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @PutMapping("/{id}")
    @RequirePermission(
        roles = {UserRole.ADMIN, UserRole.MERCHANT}, 
        description = "管理员和商户可以更新商户信息",
        requireOwnership = true,
        ownerIdParam = "userId"
    )
    public ResponseEntity<?> update(@PathVariable Long id, @Valid @RequestBody Merchant merchant) {
        try {
            merchant.setId(id);
            return ResponseEntity.ok(merchantService.update(merchant));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    @RequirePermission(
        roles = {UserRole.ADMIN}, 
        description = "只有管理员可以删除商户"
    )
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        merchantService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}")
    @RequirePermission(
        roles = {UserRole.ADMIN, UserRole.MERCHANT, UserRole.CONSUMER}, 
        description = "所有角色都可以查看商户信息"
    )
    public ResponseEntity<?> findById(@PathVariable Long id) {
        Merchant m = merchantService.findById(id);
        return m == null ? ResponseEntity.notFound().build() : ResponseEntity.ok(m);
    }

    @GetMapping
    @RequirePermission(
        roles = {UserRole.ADMIN, UserRole.CONSUMER}, 
        description = "管理员和消费者可以查看商户列表"
    )
    public ResponseEntity<Page<Merchant>> page(
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) Boolean approved,
            @RequestParam(defaultValue = "1") int pageNo,
            @RequestParam(defaultValue = "10") int pageSize) {
        return ResponseEntity.ok(merchantService.page(keyword, approved, pageNo, pageSize));
    }

    @PostMapping("/{id}/approve")
    @RequirePermission(
        roles = {UserRole.ADMIN}, 
        description = "只有管理员可以审核商户"
    )
    public ResponseEntity<?> approve(@PathVariable Long id, @RequestParam boolean approved) {
        try {
            return ResponseEntity.ok(merchantService.approve(id, approved));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}



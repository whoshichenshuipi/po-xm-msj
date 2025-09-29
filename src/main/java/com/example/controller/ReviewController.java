package com.example.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.annotation.RequirePermission;
import com.example.entity.Review;
import com.example.entity.UserRole;
import com.example.service.ReviewService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/reviews")
@CrossOrigin(origins = "*")
@Tag(name = "评价管理", description = "文化体验评价、商户评价")
public class ReviewController {

    @Autowired
    private ReviewService service;

    @PostMapping
    @RequirePermission(
        roles = {UserRole.ADMIN, UserRole.CONSUMER}, 
        description = "管理员和消费者可以创建评价"
    )
    public ResponseEntity<Review> create(@Valid @RequestBody Review r) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.create(r));
    }

    @DeleteMapping("/{id}")
    @RequirePermission(
        roles = {UserRole.ADMIN, UserRole.CONSUMER}, 
        description = "管理员和消费者可以删除评价",
        requireOwnership = true,
        ownerIdParam = "userId"
    )
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    @RequirePermission(
        roles = {UserRole.ADMIN, UserRole.MERCHANT, UserRole.CONSUMER}, 
        description = "所有角色都可以查看评价"
    )
    public ResponseEntity<Page<Review>> page(
            @RequestParam(required = false) Long merchantId,
            @RequestParam(required = false) Long productId,
            @RequestParam(required = false) Long userId,
            @RequestParam(defaultValue = "1") int pageNo,
            @RequestParam(defaultValue = "10") int pageSize) {
        return ResponseEntity.ok(service.page(merchantId, productId, userId, pageNo, pageSize));
    }
}



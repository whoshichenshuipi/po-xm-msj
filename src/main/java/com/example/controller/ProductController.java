package com.example.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.annotation.RequirePermission;
import com.example.entity.Product;
import com.example.entity.UserRole;
import com.example.service.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/products")
@CrossOrigin(origins = "*")
@Tag(name = "产品管理", description = "文化产品、特色商品管理")
public class ProductController {

    @Autowired
    private ProductService productService;

    @PostMapping
    @RequirePermission(
        roles = {UserRole.ADMIN, UserRole.MERCHANT}, 
        description = "管理员和商户可以创建产品",
        requireOwnership = true,
        ownerIdParam = "merchantId"
    )
    public ResponseEntity<Product> create(@Valid @RequestBody Product p) {
        Product created = productService.create(p);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @PutMapping("/{id}")
    @RequirePermission(
        roles = {UserRole.ADMIN, UserRole.MERCHANT}, 
        description = "管理员和商户可以更新产品",
        requireOwnership = true,
        ownerIdParam = "merchantId"
    )
    public ResponseEntity<Product> update(@PathVariable Long id, @Valid @RequestBody Product p) {
        p.setId(id);
        return ResponseEntity.ok(productService.update(p));
    }

    @DeleteMapping("/{id}")
    @RequirePermission(
        roles = {UserRole.ADMIN, UserRole.MERCHANT}, 
        description = "管理员和商户可以删除产品",
        requireOwnership = true,
        ownerIdParam = "merchantId"
    )
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        productService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}")
    @RequirePermission(
        roles = {UserRole.ADMIN, UserRole.MERCHANT, UserRole.CONSUMER}, 
        description = "所有角色都可以查看产品详情"
    )
    public ResponseEntity<?> findById(@PathVariable Long id) {
        Product found = productService.findById(id);
        return found == null ? ResponseEntity.notFound().build() : ResponseEntity.ok(found);
    }

    @GetMapping
    @RequirePermission(
        roles = {UserRole.ADMIN, UserRole.MERCHANT, UserRole.CONSUMER}, 
        description = "所有角色都可以浏览产品列表"
    )
    public ResponseEntity<Page<Product>> page(
            @RequestParam(required = false) Long merchantId,
            @RequestParam(required = false) String tag,
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) Boolean onSale,
            @RequestParam(defaultValue = "1") int pageNo,
            @RequestParam(defaultValue = "10") int pageSize) {
        return ResponseEntity.ok(productService.page(merchantId, tag, keyword, onSale, pageNo, pageSize));
    }
}



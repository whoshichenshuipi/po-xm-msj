package com.example.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.annotation.RequirePermission;
import com.example.entity.Reservation;
import com.example.entity.UserRole;
import com.example.service.ReservationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/reservations")
@CrossOrigin(origins = "*")
@Tag(name = "预订管理", description = "餐位预订、体验活动预订")
public class ReservationController {

    @Autowired
    private ReservationService service;

    @PostMapping
    @RequirePermission(
        roles = {UserRole.ADMIN, UserRole.CONSUMER}, 
        description = "管理员和消费者可以创建预订"
    )
    public ResponseEntity<Reservation> create(@Valid @RequestBody Reservation r) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.create(r));
    }

    @PutMapping("/{id}")
    @RequirePermission(
        roles = {UserRole.ADMIN, UserRole.CONSUMER, UserRole.MERCHANT}, 
        description = "所有角色都可以更新预订",
        requireOwnership = true,
        ownerIdParam = "userId"
    )
    public ResponseEntity<Reservation> update(@PathVariable Long id, @Valid @RequestBody Reservation r) {
        r.setId(id);
        return ResponseEntity.ok(service.update(r));
    }

    @DeleteMapping("/{id}")
    @RequirePermission(
        roles = {UserRole.ADMIN, UserRole.CONSUMER}, 
        description = "管理员和消费者可以取消预订",
        requireOwnership = true,
        ownerIdParam = "userId"
    )
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}")
    @RequirePermission(
        roles = {UserRole.ADMIN, UserRole.CONSUMER, UserRole.MERCHANT}, 
        description = "所有角色都可以查看预订详情"
    )
    public ResponseEntity<?> findById(@PathVariable Long id) {
        Reservation found = service.findById(id);
        return found == null ? ResponseEntity.notFound().build() : ResponseEntity.ok(found);
    }

    @GetMapping
    @RequirePermission(
        roles = {UserRole.ADMIN, UserRole.CONSUMER, UserRole.MERCHANT}, 
        description = "所有角色都可以查看预订列表"
    )
    public ResponseEntity<Page<Reservation>> page(
            @RequestParam(required = false) Long userId,
            @RequestParam(required = false) Long merchantId,
            @RequestParam(required = false) String status,
            @RequestParam(defaultValue = "1") int pageNo,
            @RequestParam(defaultValue = "10") int pageSize) {
        return ResponseEntity.ok(service.page(userId, merchantId, status, pageNo, pageSize));
    }
}



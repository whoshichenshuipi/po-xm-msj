package com.example.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.annotation.RequirePermission;
import com.example.entity.RevenueRecord;
import com.example.entity.UserRole;
import com.example.service.RevenueService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequestMapping("/revenues")
@CrossOrigin(origins = "*")
@Tag(name = "营收统计", description = "商户营收数据统计")
public class RevenueController {

    @Autowired
    private RevenueService service;

    @PostMapping
    @RequirePermission(
        roles = {UserRole.ADMIN, UserRole.MERCHANT}, 
        description = "管理员和商户可以创建营收记录",
        requireOwnership = true,
        ownerIdParam = "merchantId"
    )
    public ResponseEntity<RevenueRecord> create(@RequestBody RevenueRecord r) {
        return ResponseEntity.ok(service.create(r));
    }

    @GetMapping
    @RequirePermission(
        roles = {UserRole.ADMIN, UserRole.MERCHANT}, 
        description = "管理员和商户可以查看营收统计",
        requireOwnership = true,
        ownerIdParam = "merchantId"
    )
    public ResponseEntity<Page<RevenueRecord>> page(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate start,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate end,
            @RequestParam(required = false) Long merchantId,
            @RequestParam(defaultValue = "1") int pageNo,
            @RequestParam(defaultValue = "10") int pageSize) {
        return ResponseEntity.ok(service.page(start, end, merchantId, pageNo, pageSize));
    }
}



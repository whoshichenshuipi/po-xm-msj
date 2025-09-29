package com.example.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.entity.SystemLog;
import com.example.service.SystemLogService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/system-logs")
@CrossOrigin(origins = "*")
@Tag(name = "系统日志", description = "系统操作日志查询")
public class SystemLogController {

    @Autowired
    private SystemLogService service;

    @PostMapping
    public ResponseEntity<SystemLog> create(@RequestBody SystemLog log) {
        return ResponseEntity.ok(service.create(log));
    }

    @GetMapping
    public ResponseEntity<Page<SystemLog>> page(
            @RequestParam(required = false) Long userId,
            @RequestParam(required = false) String module,
            @RequestParam(required = false) String result,
            @RequestParam(defaultValue = "1") int pageNo,
            @RequestParam(defaultValue = "10") int pageSize) {
        return ResponseEntity.ok(service.page(userId, module, result, pageNo, pageSize));
    }
}

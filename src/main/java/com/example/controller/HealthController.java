package com.example.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

/**
 * 健康检查控制器
 * 
 * @author example
 * @since 1.0.0
 */
@RestController
@RequestMapping("/health")
@Tag(name = "健康检查", description = "系统健康状态检查")
public class HealthController {

    /**
     * 健康检查接口
     * 
     * @return 健康状态信息
     */
    @Operation(summary = "健康检查", description = "检查系统运行状态")
    @GetMapping
    public Map<String, Object> health() {
        Map<String, Object> health = new HashMap<>();
        health.put("status", "UP");
        health.put("timestamp", LocalDateTime.now());
        health.put("service", "Spring Boot Demo");
        health.put("version", "1.0.0");
        return health;
    }

    /**
     * 简单问候接口
     * 
     * @return 问候信息
     */
    @GetMapping("/hello")
    public Map<String, String> hello() {
        Map<String, String> response = new HashMap<>();
        response.put("message", "Hello, Spring Boot!");
        response.put("timestamp", LocalDateTime.now().toString());
        return response;
    }
}

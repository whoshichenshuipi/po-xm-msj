package com.example.controller;

import com.example.annotation.RequirePermission;
import com.example.dto.AuthResponse;
import com.example.dto.LoginRequest;
import com.example.dto.RegisterRequest;
import com.example.dto.UserResponse;
import com.example.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

/**
 * 认证控制器
 * 
 * @author example
 * @since 1.0.0
 */
@RestController
@RequestMapping("/auth")
@CrossOrigin(origins = "*")
@Tag(name = "认证管理", description = "用户登录、注册、登出")
public class AuthController {

    @Autowired
    private AuthService authService;

    /**
     * 用户注册
     */
    @PostMapping("/register")
    @RequirePermission(requireLogin = false, description = "注册接口，无需登录")
    @Operation(summary = "用户注册", description = "新用户注册，默认为消费者角色")
    public ResponseEntity<AuthResponse> register(@Valid @RequestBody RegisterRequest request, HttpSession session) {
        AuthResponse response = authService.register(request);
        
        if (response.getToken() != null) {
            // 注册成功，设置session
            session.setAttribute("token", response.getToken());
            session.setAttribute("user", response.getUser());
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } else {
            return ResponseEntity.badRequest().body(response);
        }
    }

    /**
     * 用户登录
     */
    @PostMapping("/login")
    @RequirePermission(requireLogin = false, description = "登录接口，无需登录")
    @Operation(summary = "用户登录", description = "支持用户名或邮箱登录")
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody LoginRequest request, HttpSession session) {
        AuthResponse response = authService.login(request);
        
        if (response.getToken() != null) {
            // 登录成功，设置session
            session.setAttribute("token", response.getToken());
            session.setAttribute("user", response.getUser());
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.badRequest().body(response);
        }
    }

    /**
     * 用户登出
     */
    @PostMapping("/logout")
    @RequirePermission(roles = {"ADMIN", "MERCHANT", "CONSUMER"}, description = "所有角色都可以登出")
    @Operation(summary = "用户登出", description = "清除用户session")
    public ResponseEntity<String> logout(HttpSession session) {
        session.invalidate();
        return ResponseEntity.ok("登出成功");
    }

    /**
     * 获取当前用户信息
     */
    @GetMapping("/me")
    @RequirePermission(roles = {"ADMIN", "MERCHANT", "CONSUMER"}, description = "所有角色都可以查看自己的信息")
    @Operation(summary = "获取当前用户信息", description = "获取当前登录用户的信息")
    public ResponseEntity<UserResponse> getCurrentUser(HttpSession session) {
        UserResponse user = (UserResponse) session.getAttribute("user");
        if (user != null) {
            return ResponseEntity.ok(user);
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

    /**
     * 检查登录状态
     */
    @GetMapping("/status")
    @RequirePermission(requireLogin = false, description = "检查登录状态，无需登录")
    @Operation(summary = "检查登录状态", description = "检查用户是否已登录")
    public ResponseEntity<AuthResponse> checkStatus(HttpSession session) {
        UserResponse user = (UserResponse) session.getAttribute("user");
        if (user != null) {
            return ResponseEntity.ok(new AuthResponse("已登录", user));
        } else {
            return ResponseEntity.ok(new AuthResponse("未登录"));
        }
    }
}

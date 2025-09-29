package com.example.controller;

import com.example.annotation.RequirePermission;
import com.example.entity.UserRole;
import com.example.util.PermissionValidator;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

/**
 * 权限测试控制器
 * 用于测试和验证权限系统功能
 *
 * @author example
 * @since 1.0.0
 */
@RestController
@RequestMapping("/permission-test")
@CrossOrigin(origins = "*")
public class PermissionTestController {

    /**
     * 测试管理员权限
     */
    @GetMapping("/admin-only")
    @RequirePermission(
        roles = {UserRole.ADMIN},
        description = "只有管理员可以访问"
    )
    public ResponseEntity<String> adminOnly() {
        return ResponseEntity.ok("管理员权限测试通过");
    }

    /**
     * 测试商户权限
     */
    @GetMapping("/merchant-only")
    @RequirePermission(
        roles = {UserRole.MERCHANT},
        description = "只有商户可以访问"
    )
    public ResponseEntity<String> merchantOnly() {
        return ResponseEntity.ok("商户权限测试通过");
    }

    /**
     * 测试消费者权限
     */
    @GetMapping("/consumer-only")
    @RequirePermission(
        roles = {UserRole.CONSUMER},
        description = "只有消费者可以访问"
    )
    public ResponseEntity<String> consumerOnly() {
        return ResponseEntity.ok("消费者权限测试通过");
    }

    /**
     * 测试多角色权限
     */
    @GetMapping("/admin-merchant")
    @RequirePermission(
        roles = {UserRole.ADMIN, UserRole.MERCHANT},
        description = "管理员和商户可以访问"
    )
    public ResponseEntity<String> adminMerchant() {
        return ResponseEntity.ok("管理员和商户权限测试通过");
    }

    /**
     * 测试所有角色权限
     */
    @GetMapping("/all-roles")
    @RequirePermission(
        roles = {UserRole.ADMIN, UserRole.MERCHANT, UserRole.CONSUMER},
        description = "所有角色都可以访问"
    )
    public ResponseEntity<String> allRoles() {
        return ResponseEntity.ok("所有角色权限测试通过");
    }

    /**
     * 测试资源所有权验证
     */
    @GetMapping("/ownership-test")
    @RequirePermission(
        roles = {UserRole.ADMIN, UserRole.MERCHANT},
        description = "测试资源所有权验证",
        requireOwnership = true,
        ownerIdParam = "merchantId"
    )
    public ResponseEntity<String> ownershipTest(@RequestParam Long merchantId) {
        return ResponseEntity.ok("资源所有权验证通过，商户ID: " + merchantId);
    }

    /**
     * 测试无需权限的接口
     */
    @GetMapping("/public")
    @RequirePermission(
        requireLogin = false,
        description = "公开接口，无需登录"
    )
    public ResponseEntity<String> publicAccess() {
        return ResponseEntity.ok("公开接口访问成功");
    }

    /**
     * 获取当前用户信息
     */
//    @GetMapping("/current-user")
//    @RequirePermission(
//        roles = {UserRole.ADMIN, UserRole.MERCHANT, UserRole.CONSUMER},
//        description = "获取当前用户信息"
//    )
//    public ResponseEntity<?> getCurrentUser() {
//        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
//        if (attributes == null) {
//            return ResponseEntity.badRequest().body("无法获取请求信息");
//        }
//
//        HttpServletRequest request = attributes.getRequest();
//        var user = PermissionValidator.getCurrentUser(request);
//
//        if (user == null) {
//            return ResponseEntity.ok("未登录用户");
//        }
//
//        return ResponseEntity.ok(user);
//    }

//    /**
//     * 测试权限验证工具类
//     */
//    @GetMapping("/permission-info")
//    @RequirePermission(
//        roles = {UserRole.ADMIN},
//        description = "只有管理员可以查看权限信息"
//    )
//    public ResponseEntity<?> getPermissionInfo() {
//        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
//        if (attributes == null) {
//            return ResponseEntity.badRequest().body("无法获取请求信息");
//        }
//
//        HttpServletRequest request = attributes.getRequest();
//        var user = PermissionValidator.getCurrentUser(request);
//
//        if (user == null) {
//            return ResponseEntity.ok("未登录用户");
//        }
//
//        return ResponseEntity.ok(String.format(
//            "用户信息: %s, 角色: %s, 是否管理员: %s, 是否商户: %s, 是否消费者: %s",
//            user.getUsername(),
//            user.getRole(),
//            PermissionValidator.isAdmin(user),
//            PermissionValidator.isMerchant(user),
//            PermissionValidator.isConsumer(user)
//        ));
//    }
}
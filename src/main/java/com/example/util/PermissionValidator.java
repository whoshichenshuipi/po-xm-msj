package com.example.util;

import com.example.annotation.RequirePermission;
import com.example.dto.UserResponse;
import com.example.entity.UserRole;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.lang.reflect.Method;
import java.util.Arrays;

/**
 * 权限验证工具类
 * 
 * @author example
 * @since 1.0.0
 */
public class PermissionValidator {
    
    /**
     * 验证用户权限
     * 
     * @param method 方法对象
     * @param request HTTP请求
     * @return 权限验证结果，null表示验证通过
     */
    public static ResponseEntity<String> validatePermission(Method method, HttpServletRequest request) {
        RequirePermission permission = method.getAnnotation(RequirePermission.class);
        
        if (permission == null) {
            return null; // 无需权限验证
        }
        
        // 检查是否需要登录
        if (permission.requireLogin()) {
            UserResponse currentUser = getCurrentUser(request);
            if (currentUser == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body("请先登录");
            }
            
            // 检查角色权限
            UserRole[] allowedRoles = permission.roles();
            if (allowedRoles.length > 0) {
                boolean hasRole = Arrays.stream(allowedRoles)
                    .anyMatch(role -> role.equals(currentUser.getRole()));
                
                if (!hasRole) {
                    return ResponseEntity.status(HttpStatus.FORBIDDEN)
                        .body("权限不足，需要角色: " + Arrays.toString(allowedRoles));
                }
            }
            
            // 检查资源所有权（用于商户只能操作自己的资源）
            if (permission.requireOwnership()) {
                if (!validateResourceOwnership(request, currentUser, permission.ownerIdParam())) {
                    return ResponseEntity.status(HttpStatus.FORBIDDEN)
                        .body("只能操作自己的资源");
                }
            }
        }
        
        return null; // 权限验证通过
    }
    
    /**
     * 获取当前登录用户
     */
    public static UserResponse getCurrentUser(HttpServletRequest request) {
        // 优先从Session获取用户信息
        HttpSession session = request.getSession(false);
        if (session != null) {
            UserResponse user = (UserResponse) session.getAttribute("user");
            if (user != null) {
                return user;
            }
        }
        
        // 如果Session中没有用户信息，尝试从请求头获取（兼容API调用）
        String userId = request.getHeader("X-User-Id");
        String userRole = request.getHeader("X-User-Role");
        String username = request.getHeader("X-User-Name");
        
        if (userId != null && userRole != null && username != null) {
            try {
                UserResponse user = new UserResponse();
                user.setId(Long.parseLong(userId));
                user.setUsername(username);
                user.setRole(UserRole.valueOf(userRole));
                return user;
            } catch (Exception e) {
                return null;
            }
        }
        
        return null;
    }
    
    /**
     * 验证资源所有权
     */
    private static boolean validateResourceOwnership(HttpServletRequest request, UserResponse currentUser, String ownerIdParam) {
        // 管理员可以操作所有资源
        if (currentUser.getRole() == UserRole.ADMIN) {
            return true;
        }
        
        // 获取资源所有者ID
        String ownerIdStr = request.getParameter(ownerIdParam);
        if (ownerIdStr == null) {
            // 尝试从路径变量获取
            String requestURI = request.getRequestURI();
            String[] pathSegments = requestURI.split("/");
            for (int i = 0; i < pathSegments.length - 1; i++) {
                if (pathSegments[i].equals(ownerIdParam.replace("Id", ""))) {
                    ownerIdStr = pathSegments[i + 1];
                    break;
                }
            }
        }
        
        if (ownerIdStr == null) {
            return false;
        }
        
        try {
            Long ownerId = Long.parseLong(ownerIdStr);
            return currentUser.getId().equals(ownerId);
        } catch (NumberFormatException e) {
            return false;
        }
    }
    
    /**
     * 检查用户是否为管理员
     */
    public static boolean isAdmin(UserResponse user) {
        return user != null && user.getRole() == UserRole.ADMIN;
    }
    
    /**
     * 检查用户是否为商户
     */
    public static boolean isMerchant(UserResponse user) {
        return user != null && user.getRole() == UserRole.MERCHANT;
    }
    
    /**
     * 检查用户是否为消费者
     */
    public static boolean isConsumer(UserResponse user) {
        return user != null && user.getRole() == UserRole.CONSUMER;
    }
}

package com.example.util;

import com.example.dto.UserResponse;
import com.example.entity.UserRole;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 * 权限工具类
 * 
 * @author example
 * @since 1.0.0
 */
public class PermissionUtil {
    
    /**
     * 获取当前用户ID
     */
    public static Long getCurrentUserId() {
        HttpServletRequest request = getCurrentRequest();
        if (request == null) return null;
        
        // 优先从Session获取
        HttpSession session = request.getSession(false);
        if (session != null) {
            UserResponse user = (UserResponse) session.getAttribute("user");
            if (user != null) {
                return user.getId();
            }
        }
        
        // 从请求头获取（兼容API调用）
        String userId = request.getHeader("X-User-Id");
        return userId != null ? Long.parseLong(userId) : null;
    }
    
    /**
     * 获取当前用户角色
     */
    public static UserRole getCurrentUserRole() {
        HttpServletRequest request = getCurrentRequest();
        if (request == null) return null;
        
        // 优先从Session获取
        HttpSession session = request.getSession(false);
        if (session != null) {
            UserResponse user = (UserResponse) session.getAttribute("user");
            if (user != null) {
                return user.getRole();
            }
        }
        
        // 从请求头获取（兼容API调用）
        String role = request.getHeader("X-User-Role");
        return role != null ? UserRole.valueOf(role) : null;
    }
    
    /**
     * 检查当前用户是否为管理员
     */
    public static boolean isAdmin() {
        return UserRole.ADMIN.equals(getCurrentUserRole());
    }
    
    /**
     * 检查当前用户是否为商户
     */
    public static boolean isMerchant() {
        return UserRole.MERCHANT.equals(getCurrentUserRole());
    }
    
    /**
     * 检查当前用户是否为消费者
     */
    public static boolean isConsumer() {
        return UserRole.CONSUMER.equals(getCurrentUserRole());
    }
    
    /**
     * 检查当前用户是否为资源所有者
     */
    public static boolean isResourceOwner(Long resourceOwnerId) {
        if (isAdmin()) {
            return true; // 管理员可以操作所有资源
        }
        
        Long currentUserId = getCurrentUserId();
        return currentUserId != null && currentUserId.equals(resourceOwnerId);
    }
    
    /**
     * 获取当前请求
     */
    private static HttpServletRequest getCurrentRequest() {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        return attributes != null ? attributes.getRequest() : null;
    }
}

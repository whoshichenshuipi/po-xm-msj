package com.example.annotation;

import com.example.entity.UserRole;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 权限控制注解
 * 用于标记需要特定权限才能访问的方法
 * 
 * @author example
 * @since 1.0.0
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface RequirePermission {
    
    /**
     * 允许访问的角色
     */
    UserRole[] roles() default {};
    
    /**
     * 权限描述
     */
    String description() default "";
    
    /**
     * 是否需要登录
     */
    boolean requireLogin() default true;
    
    /**
     * 是否需要资源所有权验证（用于商户只能操作自己的资源）
     */
    boolean requireOwnership() default false;
    
    /**
     * 资源所有者ID参数名（用于从请求参数中获取资源所有者ID）
     */
    String ownerIdParam() default "merchantId";
}

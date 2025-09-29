package com.example.aspect;

import com.example.annotation.RequirePermission;
import com.example.util.PermissionValidator;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;

/**
 * 权限验证AOP切面
 * 
 * @author example
 * @since 1.0.0
 */
@Aspect
@Component
public class PermissionAspect {

    /**
     * 环绕通知，处理权限验证
     */
    @Around("@annotation(com.example.annotation.RequirePermission)")
    public Object checkPermission(ProceedingJoinPoint joinPoint) throws Throwable {
        // 获取方法签名和注解
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        
        // 获取当前请求
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (attributes == null) {
            return ResponseEntity.status(401).body("无法获取请求信息");
        }
        
        HttpServletRequest request = attributes.getRequest();
        
        // 使用权限验证工具进行验证
        ResponseEntity<String> validationResult = PermissionValidator.validatePermission(method, request);
        if (validationResult != null) {
            return validationResult;
        }
        
        // 权限验证通过，继续执行原方法
        return joinPoint.proceed();
    }
}

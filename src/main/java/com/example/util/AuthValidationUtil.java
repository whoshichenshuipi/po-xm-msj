/**
 * 认证功能验证脚本
 * 用于快速验证后端认证功能是否正常工作
 */

package com.example.util;

import com.example.dto.AuthResponse;
import com.example.dto.LoginRequest;
import com.example.dto.RegisterRequest;
import com.example.dto.UserResponse;
import com.example.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 认证功能验证工具
 */
@Component
public class AuthValidationUtil {

    @Autowired
    private AuthService authService;

    /**
     * 验证注册功能
     */
    public boolean validateRegistration() {
        try {
            RegisterRequest request = new RegisterRequest();
            request.setUsername("validation_test_" + System.currentTimeMillis());
            request.setEmail("validation" + System.currentTimeMillis() + "@test.com");
            request.setFullName("验证测试用户");
            request.setPassword("123456");
            request.setConfirmPassword("123456");

            AuthResponse response = authService.register(request);
            
            return response != null && 
                   response.getToken() != null && 
                   response.getUser() != null &&
                   "CONSUMER".equals(response.getUser().getRole().name());
        } catch (Exception e) {
            System.err.println("注册功能验证失败: " + e.getMessage());
            return false;
        }
    }

    /**
     * 验证登录功能
     */
    public boolean validateLogin() {
        try {
            LoginRequest request = new LoginRequest();
            request.setUsernameOrEmail("admin");
            request.setPassword("123456");

            AuthResponse response = authService.login(request);
            
            return response != null && 
                   response.getToken() != null && 
                   response.getUser() != null &&
                   "ADMIN".equals(response.getUser().getRole().name()) &&
                   "admin".equals(response.getUser().getUsername());
        } catch (Exception e) {
            System.err.println("登录功能验证失败: " + e.getMessage());
            return false;
        }
    }

    /**
     * 验证邮箱登录功能
     */
    public boolean validateEmailLogin() {
        try {
            LoginRequest request = new LoginRequest();
            request.setUsernameOrEmail("admin@example.com");
            request.setPassword("123456");

            AuthResponse response = authService.login(request);
            
            return response != null && 
                   response.getToken() != null && 
                   response.getUser() != null &&
                   "ADMIN".equals(response.getUser().getRole().name()) &&
                   "admin".equals(response.getUser().getUsername());
        } catch (Exception e) {
            System.err.println("邮箱登录功能验证失败: " + e.getMessage());
            return false;
        }
    }

    /**
     * 验证Token解析功能
     */
    public boolean validateTokenParsing() {
        try {
            // 先登录获取token
            LoginRequest loginRequest = new LoginRequest();
            loginRequest.setUsernameOrEmail("admin");
            loginRequest.setPassword("123456");

            AuthResponse loginResponse = authService.login(loginRequest);
            if (loginResponse == null || loginResponse.getToken() == null) {
                return false;
            }

            // 使用token获取用户信息
            UserResponse userResponse = authService.getUserByToken(loginResponse.getToken());
            
            return userResponse != null && 
                   "admin".equals(userResponse.getUsername()) &&
                   "ADMIN".equals(userResponse.getRole().name());
        } catch (Exception e) {
            System.err.println("Token解析功能验证失败: " + e.getMessage());
            return false;
        }
    }

    /**
     * 验证错误处理
     */
    public boolean validateErrorHandling() {
        try {
            // 测试用户名不存在
            LoginRequest request1 = new LoginRequest();
            request1.setUsernameOrEmail("nonexistent");
            request1.setPassword("123456");
            AuthResponse response1 = authService.login(request1);
            if (response1 == null || response1.getToken() != null) {
                return false;
            }

            // 测试密码错误
            LoginRequest request2 = new LoginRequest();
            request2.setUsernameOrEmail("admin");
            request2.setPassword("wrongpassword");
            AuthResponse response2 = authService.login(request2);
            if (response2 == null || response2.getToken() != null) {
                return false;
            }

            // 测试用户名重复注册
            RegisterRequest registerRequest = new RegisterRequest();
            registerRequest.setUsername("admin"); // 已存在的用户名
            registerRequest.setEmail("new@test.com");
            registerRequest.setFullName("新用户");
            registerRequest.setPassword("123456");
            registerRequest.setConfirmPassword("123456");
            AuthResponse registerResponse = authService.register(registerRequest);
            if (registerResponse == null || registerResponse.getToken() != null) {
                return false;
            }

            return true;
        } catch (Exception e) {
            System.err.println("错误处理验证失败: " + e.getMessage());
            return false;
        }
    }

    /**
     * 运行所有验证
     */
//    public void runAllValidations() {
//        System.out.println("开始认证功能验证...");
//        System.out.println("=".repeat(50));
//
//        boolean registrationValid = validateRegistration();
//        System.out.println("注册功能: " + (registrationValid ? "✅ 通过" : "❌ 失败"));
//
//        boolean loginValid = validateLogin();
//        System.out.println("用户名登录: " + (loginValid ? "✅ 通过" : "❌ 失败"));
//
//        boolean emailLoginValid = validateEmailLogin();
//        System.out.println("邮箱登录: " + (emailLoginValid ? "✅ 通过" : "❌ 失败"));
//
//        boolean tokenValid = validateTokenParsing();
//        System.out.println("Token解析: " + (tokenValid ? "✅ 通过" : "❌ 失败"));
//
//        boolean errorHandlingValid = validateErrorHandling();
//        System.out.println("错误处理: " + (errorHandlingValid ? "✅ 通过" : "❌ 失败"));
//
//        System.out.println("=".repeat(50));
//        boolean allValid = registrationValid && loginValid && emailLoginValid && tokenValid && errorHandlingValid;
//        System.out.println("总体结果: " + (allValid ? "✅ 所有功能正常" : "❌ 存在问题"));
//    }
}

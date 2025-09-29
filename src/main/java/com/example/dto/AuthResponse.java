package com.example.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

/**
 * 认证响应DTO
 * 
 * @author example
 * @since 1.0.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AuthResponse {
    
    private String token;
    private UserResponse user;
    private String message;
    
    public AuthResponse(String message) {
        this.message = message;
    }
    
    public AuthResponse(String token, UserResponse user) {
        this.token = token;
        this.user = user;
        this.message = "登录成功";
    }
}

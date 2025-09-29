package com.example.service;

import com.example.dto.AuthResponse;
import com.example.dto.LoginRequest;
import com.example.dto.RegisterRequest;
import com.example.dto.UserResponse;
import com.example.entity.User;
import com.example.entity.UserRole;
import com.example.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

/**
 * 认证服务类
 * 
 * @author example
 * @since 1.0.0
 */
@Service
@Transactional
public class AuthService {

    @Autowired
    private UserRepository userRepository;

    /**
     * 用户注册
     */
    public AuthResponse register(RegisterRequest request) {
        // 验证密码确认
        if (!request.getPassword().equals(request.getConfirmPassword())) {
            return new AuthResponse("密码和确认密码不匹配");
        }

        // 检查用户名是否已存在
        if (userRepository.findByUsername(request.getUsername()).isPresent()) {
            return new AuthResponse("用户名已存在");
        }

        // 检查邮箱是否已存在
        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            return new AuthResponse("邮箱已被注册");
        }

        // 创建新用户
        User user = new User();
        user.setUsername(request.getUsername());
        user.setEmail(request.getEmail());
        user.setFullName(request.getFullName());
        user.setPassword(hashPassword(request.getPassword()));
        // 使用请求中的角色，如果没有指定则默认为消费者角色
        user.setRole(request.getRole() != null ? request.getRole() : UserRole.CONSUMER);

        User savedUser = userRepository.save(user);
        
        // 生成简单的token（实际项目中应使用JWT）
        String token = generateToken(savedUser);
        
        return new AuthResponse(token, UserResponse.fromUser(savedUser));
    }

    /**
     * 用户登录
     */
    public AuthResponse login(LoginRequest request) {
        // 查找用户（支持用户名或邮箱登录）
        Optional<User> userOpt = userRepository.findByUsername(request.getUsernameOrEmail());
        if (!userOpt.isPresent()) {
            userOpt = userRepository.findByEmail(request.getUsernameOrEmail());
        }

        if (!userOpt.isPresent()) {
            return new AuthResponse("用户名或邮箱不存在");
        }

        User user = userOpt.get();
        
        // 验证密码
        if (!verifyPassword(request.getPassword(), user.getPassword())) {
            return new AuthResponse("密码错误");
        }

        // 生成token
        String token = generateToken(user);
        
        return new AuthResponse(token, UserResponse.fromUser(user));
    }

    /**
     * 根据token获取用户信息
     */
    public UserResponse getUserByToken(String token) {
        if (token == null || token.isEmpty()) {
            return null;
        }

        try {
            // 简单的token解析（实际项目中应使用JWT）
            Long userId = Long.parseLong(token.split("_")[0]);
            Optional<User> userOpt = userRepository.findById(userId);
            
            if (userOpt.isPresent()) {
                return UserResponse.fromUser(userOpt.get());
            }
        } catch (Exception e) {
            // token格式错误
        }

        return null;
    }

    /**
     * 简单的密码哈希（实际项目中应使用BCrypt等安全算法）
     */
    private String hashPassword(String password) {
        // 这里使用简单的哈希，实际项目中应使用BCrypt
        return String.valueOf(password.hashCode());
    }

    /**
     * 验证密码
     */
    private boolean verifyPassword(String rawPassword, String hashedPassword) {
        return hashPassword(rawPassword).equals(hashedPassword);
    }

    /**
     * 生成简单的token
     */
    private String generateToken(User user) {
        // 简单的token格式：userId_uuid
        return user.getId() + "_" + UUID.randomUUID().toString().replace("-", "");
    }
}

package com.example.controller;

import com.example.annotation.RequirePermission;
import com.example.entity.User;
import com.example.entity.UserRole;
import com.example.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

/**
 * 用户控制器
 * 
 * @author example
 * @since 1.0.0
 */
@RestController
@RequestMapping("/users")
@CrossOrigin(origins = "*")
@Tag(name = "用户管理", description = "用户CRUD操作")
public class UserController {

    @Autowired
    private UserService userService;

    /**
     * 创建用户
     * 
     * @param user 用户信息
     * @return 创建结果
     */
    @Operation(summary = "创建用户", description = "创建新用户账户")
    @PostMapping
    @RequirePermission(
        roles = {UserRole.ADMIN}, 
        description = "只有管理员可以创建用户"
    )
    public ResponseEntity<?> createUser(@Valid @RequestBody User user) {
        try {
            User createdUser = userService.createUser(user);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdUser);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    /**
     * 获取所有用户
     * 
     * @return 用户列表
     */
    @GetMapping
    @RequirePermission(
        roles = {UserRole.ADMIN}, 
        description = "只有管理员可以查看所有用户"
    )
    public ResponseEntity<List<User>> getAllUsers() {
        List<User> users = userService.findAll();
        return ResponseEntity.ok(users);
    }

    /**
     * 根据ID获取用户
     * 
     * @param id 用户ID
     * @return 用户信息
     */
    @GetMapping("/{id}")
    @RequirePermission(
        roles = {UserRole.ADMIN, UserRole.MERCHANT, UserRole.CONSUMER}, 
        description = "所有角色都可以查看用户信息"
    )
    public ResponseEntity<?> getUserById(@PathVariable Long id) {
        Optional<User> user = userService.findById(id);
        if (user.isPresent()) {
            return ResponseEntity.ok(user.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * 根据用户名获取用户
     * 
     * @param username 用户名
     * @return 用户信息
     */
    @GetMapping("/username/{username}")
    @RequirePermission(
        roles = {UserRole.ADMIN, UserRole.MERCHANT, UserRole.CONSUMER}, 
        description = "所有角色都可以根据用户名查询用户"
    )
    public ResponseEntity<?> getUserByUsername(@PathVariable String username) {
        Optional<User> user = userService.findByUsername(username);
        if (user.isPresent()) {
            return ResponseEntity.ok(user.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * 根据姓名模糊查询用户
     * 
     * @param fullName 姓名关键字
     * @return 用户列表
     */
    @GetMapping("/search")
    @RequirePermission(
        roles = {UserRole.ADMIN}, 
        description = "只有管理员可以搜索用户"
    )
    public ResponseEntity<List<User>> searchUsersByFullName(@RequestParam String fullName) {
        List<User> users = userService.findByFullNameContaining(fullName);
        return ResponseEntity.ok(users);
    }

    /**
     * 更新用户信息
     * 
     * @param id 用户ID
     * @param user 用户信息
     * @return 更新结果
     */
    @PutMapping("/{id}")
    @RequirePermission(
        roles = {UserRole.ADMIN}, 
        description = "只有管理员可以更新用户信息"
    )
    public ResponseEntity<?> updateUser(@PathVariable Long id, @Valid @RequestBody User user) {
        try {
            user.setId(id);
            User updatedUser = userService.updateUser(user);
            return ResponseEntity.ok(updatedUser);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    /**
     * 删除用户
     * 
     * @param id 用户ID
     * @return 删除结果
     */
    @DeleteMapping("/{id}")
    @RequirePermission(
        roles = {UserRole.ADMIN}, 
        description = "只有管理员可以删除用户"
    )
    public ResponseEntity<?> deleteUser(@PathVariable Long id) {
        try {
            userService.deleteUser(id);
            return ResponseEntity.ok().body("用户删除成功");
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}

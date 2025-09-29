package com.example.dto;

import com.example.entity.UserRole;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

/**
 * 权限响应DTO
 * 
 * @author example
 * @since 1.0.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PermissionResponse {
    
    /**
     * 用户ID
     */
    private Long userId;
    
    /**
     * 用户名
     */
    private String username;
    
    /**
     * 用户角色
     */
    private UserRole role;
    
    /**
     * 角色名称（中文）
     */
    private String roleName;
    
    /**
     * 是否为管理员
     */
    private Boolean isAdmin;
    
    /**
     * 是否为商户
     */
    private Boolean isMerchant;
    
    /**
     * 是否为消费者
     */
    private Boolean isConsumer;
    
    /**
     * 权限描述
     */
    private String permissionDescription;
    
    /**
     * 构造方法 - 根据用户信息创建权限响应
     */
    public PermissionResponse(UserResponse user) {
        this.userId = user.getId();
        this.username = user.getUsername();
        this.role = user.getRole();
        this.roleName = getRoleName(user.getRole());
        this.isAdmin = user.getRole() == UserRole.ADMIN;
        this.isMerchant = user.getRole() == UserRole.MERCHANT;
        this.isConsumer = user.getRole() == UserRole.CONSUMER;
        this.permissionDescription = getPermissionDescription(user.getRole());
    }
    
    /**
     * 获取角色中文名称
     */
    private String getRoleName(UserRole role) {
        switch (role) {
            case ADMIN:
                return "管理员";
            case MERCHANT:
                return "商家";
            case CONSUMER:
                return "消费者";
            default:
                return "未知角色";
        }
    }
    
    /**
     * 获取权限描述
     */
    private String getPermissionDescription(UserRole role) {
        switch (role) {
            case ADMIN:
                return "拥有系统最高权限，可以管理所有功能模块";
            case MERCHANT:
                return "商户权限，可以管理自己的店铺、商品、订单等";
            case CONSUMER:
                return "消费者权限，可以浏览商品、下单、评价等";
            default:
                return "未知权限";
        }
    }
}

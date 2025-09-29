# 美食街管理系统 - 登录注册功能使用说明

## 功能概述

本项目已成功集成了基于AOP的权限管理系统和完整的用户认证功能，包括：

- **用户注册**：新用户注册，默认为消费者角色
- **用户登录**：支持用户名或邮箱登录
- **权限验证**：基于Session的权限验证，支持三种角色
- **Web界面**：美观的登录注册页面

## 系统架构

### 角色权限
- **ADMIN（管理员）**：拥有全模块操作权限
- **MERCHANT（商户）**：仅能操作自身的产品管理、订单处理等功能
- **CONSUMER（消费者）**：仅能使用浏览、预订、评价等功能

### 技术栈
- Spring Boot 2.7.18
- Spring AOP（权限验证）
- JPA + MyBatis-Plus
- MySQL 8
- HTML/CSS/JavaScript（前端界面）

## 快速开始

### 1. 启动应用

```bash
# 确保MySQL数据库已启动
# 数据库名：food_street
# 用户名：root
# 密码：root

# 启动Spring Boot应用
mvn spring-boot:run
```

### 2. 访问系统

- **主页**：http://localhost:8080/api/index.html
- **登录页面**：http://localhost:8080/api/login.html
- **注册页面**：http://localhost:8080/api/register.html
- **API文档**：http://localhost:8080/api/swagger-ui.html

### 3. 测试账号

系统已预置测试账号：

| 用户名 | 邮箱 | 密码 | 角色 |
|--------|------|------|------|
| admin | admin@example.com | 123456 | 管理员 |
| user1 | user1@example.com | 123456 | 消费者 |
| user2 | user2@example.com | 123456 | 商户 |

## API接口

### 认证相关接口

#### 用户注册
```http
POST /api/auth/register
Content-Type: application/json

{
  "username": "newuser",
  "email": "newuser@example.com",
  "fullName": "新用户",
  "password": "123456",
  "confirmPassword": "123456"
}
```

#### 用户登录
```http
POST /api/auth/login
Content-Type: application/json

{
  "usernameOrEmail": "admin",
  "password": "123456"
}
```

#### 用户登出
```http
POST /api/auth/logout
```

#### 获取当前用户信息
```http
GET /api/auth/me
```

#### 检查登录状态
```http
GET /api/auth/status
```

### 权限测试接口

#### 管理员权限测试
```http
GET /api/test/admin
```

#### 商户权限测试
```http
GET /api/test/merchant
```

#### 消费者权限测试
```http
GET /api/test/consumer
```

#### 多角色权限测试
```http
GET /api/test/multi-role
```

## 权限配置

### 权限注解使用

```java
@RequirePermission(roles = {"ADMIN"}, description = "只有管理员可以访问")
@GetMapping("/admin-only")
public ResponseEntity<String> adminOnly() {
    return ResponseEntity.ok("管理员专用接口");
}

@RequirePermission(roles = {"ADMIN", "MERCHANT"}, description = "管理员和商户可以访问")
@PostMapping("/create-product")
public ResponseEntity<Product> createProduct(@RequestBody Product product) {
    // 创建产品逻辑
}
```

### 权限验证流程

1. **Session验证**：优先从HTTP Session获取用户信息
2. **请求头验证**：如果Session中没有用户信息，从请求头获取（兼容API调用）
3. **角色验证**：检查用户角色是否在允许的角色列表中
4. **权限通过**：继续执行原方法
5. **权限拒绝**：返回403 Forbidden错误

## 前端页面

### 登录页面功能
- 支持用户名或邮箱登录
- 实时表单验证
- 错误信息显示
- 登录成功后自动跳转

### 注册页面功能
- 完整的用户信息填写
- 密码确认验证
- 实时密码匹配检查
- 注册成功后跳转到登录页面

### 主页功能
- 自动检测登录状态
- 显示当前用户信息
- 权限测试按钮
- 登出功能

## 数据库结构

### 用户表（users）
```sql
CREATE TABLE users (
  id BIGINT AUTO_INCREMENT PRIMARY KEY,
  username VARCHAR(50) NOT NULL UNIQUE,
  email VARCHAR(255) NOT NULL UNIQUE,
  full_name VARCHAR(100) NOT NULL,
  password VARCHAR(100) NOT NULL,
  created_at DATETIME,
  updated_at DATETIME,
  role VARCHAR(32) NOT NULL
);
```

## 安全说明

### 密码安全
- 当前使用简单哈希，生产环境建议使用BCrypt
- 密码长度限制：6-100个字符
- 密码确认验证

### Session管理
- 使用HTTP Session存储用户信息
- 支持Session超时
- 登出时清除Session

### 权限控制
- 基于AOP的权限验证
- 细粒度的角色权限控制
- 防止未授权访问

## 扩展功能

### 添加新角色
1. 在`UserRole`枚举中添加新角色
2. 更新权限配置
3. 修改相关Controller的权限注解

### 自定义权限验证
```java
@GetMapping("/resource/{id}")
public ResponseEntity<?> getResource(@PathVariable Long id) {
    // 检查是否为资源所有者
    if (!PermissionUtil.isResourceOwner(resourceOwnerId)) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body("无权访问该资源");
    }
    // 业务逻辑
}
```

## 故障排除

### 常见问题

1. **登录失败**
   - 检查用户名/邮箱和密码是否正确
   - 确认数据库连接正常
   - 查看应用日志

2. **权限验证失败**
   - 确认用户已登录
   - 检查用户角色是否正确
   - 验证权限注解配置

3. **Session丢失**
   - 检查Session配置
   - 确认Cookie设置
   - 验证服务器时间

### 日志查看
```bash
# 查看应用日志
tail -f logs/application.log

# 查看权限验证日志
grep "PermissionAspect" logs/application.log
```

## 下一步计划

1. **JWT集成**：替换简单token为JWT
2. **密码加密**：使用BCrypt加密密码
3. **记住我功能**：添加记住登录状态
4. **密码重置**：实现忘记密码功能
5. **双因子认证**：添加短信验证码登录

## 联系支持

如有问题或建议，请联系开发团队。

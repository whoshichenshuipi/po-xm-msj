# 权限管理系统使用说明

## 概述

本项目使用AOP（面向切面编程）实现了基于角色的权限管理系统，支持三种用户角色：
- **ADMIN**：管理人员，拥有全模块操作权限
- **MERCHANT**：商户，仅能操作自身的产品管理、订单处理、文化资质申请等功能
- **CONSUMER**：消费者，仅能使用浏览、预订、评价等功能

## 权限注解

### @RequirePermission

用于标记需要权限验证的方法或类。

**参数说明：**
- `roles`：允许访问的角色数组，默认为空
- `description`：权限描述，用于文档说明
- `requireLogin`：是否需要登录，默认为true

**使用示例：**
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

## 权限配置

### 用户角色权限矩阵

| 功能模块 | ADMIN | MERCHANT | CONSUMER |
|---------|-------|----------|----------|
| 用户管理 | ✅ 全部 | ❌ | ❌ |
| 商户管理 | ✅ 全部 | ✅ 自身 | ✅ 查看 |
| 产品管理 | ✅ 全部 | ✅ 自身 | ✅ 查看 |
| 预订管理 | ✅ 全部 | ✅ 处理 | ✅ 创建/查看 |
| 评价管理 | ✅ 全部 | ✅ 查看 | ✅ 创建/查看 |
| 系统管理 | ✅ 全部 | ❌ | ❌ |

### 具体权限分配

#### 管理员 (ADMIN)
- 用户管理：创建、查看、更新、删除用户
- 商户管理：创建、查看、更新、删除、审核商户
- 产品管理：创建、查看、更新、删除产品
- 预订管理：创建、查看、更新、删除预订
- 评价管理：创建、查看、删除评价
- 系统管理：所有系统级操作

#### 商户 (MERCHANT)
- 商户管理：创建、查看、更新自身商户信息
- 产品管理：创建、查看、更新、删除自身产品
- 预订管理：查看、更新相关预订
- 评价管理：查看评价
- 文化资质申请：申请、查看资质

#### 消费者 (CONSUMER)
- 商户管理：查看商户信息
- 产品管理：查看产品信息
- 预订管理：创建、查看、更新、删除自身预订
- 评价管理：创建、查看、删除自身评价

## 请求头配置

权限验证通过HTTP请求头进行：

```
X-User-Id: 123          # 当前用户ID
X-User-Role: ADMIN      # 当前用户角色 (ADMIN/MERCHANT/CONSUMER)
```

## 测试接口

### 权限测试接口

- `GET /api/test/admin` - 测试管理员权限
- `GET /api/test/merchant` - 测试商户权限  
- `GET /api/test/consumer` - 测试消费者权限
- `GET /api/test/multi-role` - 测试多角色权限
- `GET /api/test/public` - 测试公开接口
- `GET /api/test/forbidden` - 测试权限不足情况

### 测试示例

**管理员测试：**
```bash
curl -H "X-User-Id: 1" -H "X-User-Role: ADMIN" http://localhost:8080/api/test/admin
```

**商户测试：**
```bash
curl -H "X-User-Id: 2" -H "X-User-Role: MERCHANT" http://localhost:8080/api/test/merchant
```

**消费者测试：**
```bash
curl -H "X-User-Id: 3" -H "X-User-Role: CONSUMER" http://localhost:8080/api/test/consumer
```

## 错误响应

### 401 Unauthorized
```json
{
  "message": "请先登录"
}
```

### 403 Forbidden
```json
{
  "message": "权限不足，需要角色: [ADMIN]"
}
```

## 扩展说明

### 添加新的权限验证

1. 在需要权限验证的方法上添加`@RequirePermission`注解
2. 配置允许的角色和描述信息
3. 系统会自动进行权限验证

### 自定义权限验证

可以通过`PermissionUtil`工具类进行更细粒度的权限控制：

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

## 注意事项

1. 确保在请求头中正确设置用户ID和角色信息
2. 权限验证在方法执行前进行，验证失败会直接返回错误响应
3. 管理员拥有所有权限，可以绕过资源所有者检查
4. 建议在生产环境中结合JWT或其他认证机制使用

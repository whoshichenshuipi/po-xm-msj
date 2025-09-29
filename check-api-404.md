# 食品详情API 404错误排查指南

## 问题分析

### 当前配置
- **后端端口**: 8080
- **Context Path**: `/api`
- **Controller路径**: `@RequestMapping("/food-details")`
- **完整API路径**: `http://localhost:8080/api/food-details/{id}`

### 404错误可能原因

1. ✅ **最可能**: 数据库中没有 ID=1 的食品记录
2. ⚠️ 后端服务未启动
3. ⚠️ 端口被占用
4. ⚠️ Controller 未被 Spring 扫描

## 排查步骤

### 步骤 1: 检查后端服务

```bash
# 查看后端是否在运行
curl http://localhost:8080/api/food-details/health

# 应该返回：
# {"success":true,"message":"FoodDetail API is running","timestamp":...}
```

### 步骤 2: 检查数据库数据

在 MySQL 客户端中执行：

```sql
USE food_street;

-- 检查 food_detail 表是否存在
SHOW TABLES LIKE 'food_detail';

-- 查看表结构和数据
SELECT * FROM food_detail;
SELECT COUNT(*) FROM food_detail;
```

### 步骤 3: 如果没有数据，执行初始化

**方法A - 执行完整初始化脚本**（推荐）

```bash
cd bo-xm-brack
mysql -u root -p food_street < init-route-tables.sql
```

**方法B - 只插入食品测试数据**

在 MySQL 客户端执行 `test-food-api.sql`

**方法C - 手动插入**

```sql
USE food_street;

INSERT INTO food_detail (
    name, description, image_url, video_url, price, rating, category, tags, 
    merchant_id, culture_story, preparation_time, difficulty_level, 
    serving_size, is_featured, view_count, like_count, status, created_at, updated_at
) VALUES
('传统炒饭', '粒粒分明的炒饭，香气四溢', '/src/assets/文化街.jpg', '/src/assets/炒饭.mp4', 
 28.00, 4.8, '主食类', '传统,香脆,营养', 1, 
 '炒饭作为中华传统美食', 15, 'EASY', 2, 1, 156, 23, 'ACTIVE', NOW(), NOW());
```

### 步骤 4: 验证修复

执行以下命令测试API：

```bash
# 测试健康检查
curl http://localhost:8080/api/food-details/health

# 测试获取食品列表
curl http://localhost:8080/api/food-details

# 测试获取特定食品
curl http://localhost:8080/api/food-details/1
```

应该返回JSON数据，而不是404。

## 快速修复命令（一条命令解决）

```bash
mysql -u root -p food_street < init-route-tables.sql
```

这会：
1. ✅ 创建 route_details 和 route_nodes 表
2. ✅ 插入路线测试数据
3. ✅ 插入食品测试数据
4. ✅ 解决404错误

## 检查后端服务状态

### 检查端口占用
```bash
netstat -ano | findstr :8080
```

### 检查后端日志
查看 IDEA 控制台或日志文件，确认：
- ✅ Spring Boot 启动成功
- ✅ 数据库连接成功
- ✅ Controller 已注册

### 检查API映射
在 IDEA 中：
1. 右键 `FoodDetailController`
2. 选择 "Copy Reference"
3. 确认路径是 `/api/food-details`

## 前端代码检查

前端请求路径（FoodDetail.vue:202）：

```javascript
fetchFoodDetail = async (id) => {
  const response = await fetch(`http://localhost:8080/api/food-details/${id}`, {
    method: 'GET',
    headers: { 'Content-Type': 'application/json' },
    mode: 'cors'
  })
  // ...
}
```

这个路径是正确的。

## 常见问题

### Q1: 数据库连接失败
**解决**: 检查 `application.yml` 中的数据库配置

### Q2: 表不存在
**解决**: 执行 `schema.sql` 创建表

### Q3: 数据不存在
**解决**: 执行 `init-route-tables.sql` 或 `test-food-api.sql`

### Q4: 端口被占用
**解决**: 修改 `application.yml` 中的端口或杀死占用进程

## 相关文件

- `bo-xm-brack/init-route-tables.sql` - 完整初始化脚本
- `bo-xm-brack/test-food-api.sql` - 快速测试脚本
- `bo-xm-brack/src/main/resources/schema.sql` - 表结构定义
- `bo-xm-brack/src/main/resources/data.sql` - 测试数据

## 验证成功标志

当访问 `http://localhost:8080/api/food-details/1` 时，应该返回：

```json
{
  "success": true,
  "data": {
    "id": 1,
    "name": "传统炒饭",
    "description": "粒粒分明的炒饭，香气四溢",
    "price": 28.00,
    "rating": 4.8,
    "category": "主食类",
    "tags": "传统,香脆,营养",
    "status": "ACTIVE"
  },
  "message": "获取食品详情成功"
}
```

而不是404错误。


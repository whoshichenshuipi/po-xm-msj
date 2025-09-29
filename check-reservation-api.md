# 预订API 404错误排查

## 问题分析

前端报错：`POST http://localhost:3001/api/reservations 404`

### 问题原因
1. ✅ vite.config.js 缺少代理配置（已修复）
2. ⏳ 后端服务可能未运行在 8080 端口
3. ⏳ 数据库表可能不存在

## 解决方案

### 1. 前端代理配置（已完成）

已修改 `bo-xm-qian/food-street-qian/vite.config.js`：

```javascript
server: {
  port: 3001,
  open: true,
  proxy: {
    '/api': {
      target: 'http://localhost:8080',
      changeOrigin: true,
      secure: false
    }
  }
}
```

### 2. 重启前端服务

由于修改了 vite.config.js，需要重启前端开发服务器：

```bash
cd bo-xm-qian/food-street-qian
npm run dev
```

或者如果正在运行，按 `Ctrl+C` 停止，然后重新运行。

### 3. 检查后端服务状态

在浏览器中访问：
```
http://localhost:8080/api/reservations/health
```

或者测试其他已知接口：
```
http://localhost:8080/api/auth/status
```

### 4. 验证数据库表

在 MySQL 中执行：

```sql
USE food_street;

-- 检查 reservation 表是否存在
SHOW TABLES LIKE 'reservation';

-- 查看表结构
DESCRIBE reservation;

-- 查看当前数据
SELECT * FROM reservation;
```

### 5. 如果表不存在，执行初始化

```bash
cd bo-xm-brack
mysql -u root -p food_street < src/main/resources/schema.sql
```

或者手动创建表：

```sql
USE food_street;

CREATE TABLE IF NOT EXISTS reservation (
  id BIGINT AUTO_INCREMENT PRIMARY KEY,
  user_id BIGINT NOT NULL,
  merchant_id BIGINT NOT NULL,
  product_id BIGINT,
  reserve_time DATETIME NOT NULL,
  people INT DEFAULT 1,
  status VARCHAR(32) DEFAULT 'PENDING',
  remark VARCHAR(500),
  created_at DATETIME,
  updated_at DATETIME
);
```

## 测试步骤

### 1. 重启前端服务（必须）

```bash
cd bo-xm-qian/food-street-qian
npm run dev
```

### 2. 检查后端服务

在浏览器访问：
- 健康检查：http://localhost:8080/api/health
- 查看预订列表：http://localhost:8080/api/reservations

应该返回数据，不是 404。

### 3. 测试预订功能

1. 访问食品详情页：http://localhost:3001/food-detail/1
2. 点击"立即预订"按钮
3. 填写预订信息
4. 点击"确定预订"
5. 查看浏览器控制台

### 4. 验证数据

在预订管理页面查看是否显示新创建的预订记录。

## 常见错误排查

### Error 1: 端口 8080 被占用
**解决**：
```bash
# 查找占用 8080 端口的进程
netstat -ano | findstr :8080

# 杀死进程（替换 PID）
taskkill /F /PID <PID>
```

### Error 2: 数据库连接失败
**检查**：`bo-xm-brack/src/main/resources/application.yml`

```yaml
datasource:
  url: jdbc:mysql://localhost:3306/food_street
  username: root
  password: root
```

### Error 3: 403 Forbidden
**原因**：用户未登录或权限不足
**解决**：先登录系统

### Error 4: 500 Internal Server Error
**检查**：后端日志，可能是数据库表不存在

## API 路径验证

### 后端接口
```
POST http://localhost:8080/api/reservations
Content-Type: application/json
```

### 前端请求（通过代理）
```
POST http://localhost:3001/api/reservations
Content-Type: application/json
```
↓ 代理转发 ↓
```
POST http://localhost:8080/api/reservations
```

## 数据格式

### 请求体（前端发送）
```json
{
  "merchantId": 1,
  "productId": 1,
  "reserveTime": "2024-10-28 18:00:00",
  "people": 1,
  "status": "PENDING",
  "remark": "备注信息"
}
```

### 响应体（后端返回，201 Created）
```json
{
  "id": 1,
  "userId": 1,
  "merchantId": 1,
  "productId": 1,
  "reserveTime": "2024-10-28T18:00:00",
  "people": 1,
  "status": "PENDING",
  "remark": "备注信息",
  "createdAt": "2024-10-27T10:00:00",
  "updatedAt": "2024-10-27T10:00:00"
}
```

## 检查清单

- [ ] 前端 vite.config.js 已配置代理
- [ ] 前端服务已重启
- [ ] 后端服务运行在 8080 端口
- [ ] 数据库 food_street 存在
- [ ] reservation 表存在
- [ ] 用户已登录系统
- [ ] 网络请求路径正确

## 下一步

1. 重启前端服务
2. 检查后端服务状态
3. 测试预订功能
4. 查看预订管理页面


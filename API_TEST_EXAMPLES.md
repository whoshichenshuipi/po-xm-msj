# 认证接口API测试示例

本文档提供了认证接口的完整测试示例，包括各种场景和边界情况的测试。

## 测试环境准备

### 前置条件

1. 后端服务运行在 `http://localhost:8080`
2. 数据库已初始化测试数据
3. 测试工具：curl、Postman 或浏览器控制台

### 测试数据

```json
{
  "testUsers": [
    {
      "username": "admin",
      "email": "admin@example.com",
      "password": "123456",
      "role": "ADMIN"
    },
    {
      "username": "user1",
      "email": "user1@example.com", 
      "password": "123456",
      "role": "CONSUMER"
    },
    {
      "username": "user2",
      "email": "user2@example.com",
      "password": "123456", 
      "role": "MERCHANT"
    }
  ]
}
```

## 1. 用户注册测试

### 1.1 正常注册测试

```bash
# 测试用例：正常用户注册
curl -X POST http://localhost:8080/api/auth/register \
  -H "Content-Type: application/json" \
  -d '{
    "username": "testuser001",
    "email": "testuser001@example.com",
    "fullName": "测试用户001",
    "password": "123456",
    "confirmPassword": "123456"
  }' \
  -w "\nHTTP状态码: %{http_code}\n"
```

**预期结果**:
- HTTP状态码: 201
- 响应包含用户信息和token
- 用户角色为CONSUMER

### 1.2 用户名重复测试

```bash
# 测试用例：用户名重复
curl -X POST http://localhost:8080/api/auth/register \
  -H "Content-Type: application/json" \
  -d '{
    "username": "admin",
    "email": "newemail@example.com",
    "fullName": "新用户",
    "password": "123456",
    "confirmPassword": "123456"
  }' \
  -w "\nHTTP状态码: %{http_code}\n"
```

**预期结果**:
- HTTP状态码: 400
- 错误信息: "用户名已存在"

### 1.3 邮箱重复测试

```bash
# 测试用例：邮箱重复
curl -X POST http://localhost:8080/api/auth/register \
  -H "Content-Type: application/json" \
  -d '{
    "username": "newuser",
    "email": "admin@example.com",
    "fullName": "新用户",
    "password": "123456",
    "confirmPassword": "123456"
  }' \
  -w "\nHTTP状态码: %{http_code}\n"
```

**预期结果**:
- HTTP状态码: 400
- 错误信息: "邮箱已被注册"

### 1.4 密码不匹配测试

```bash
# 测试用例：密码和确认密码不匹配
curl -X POST http://localhost:8080/api/auth/register \
  -H "Content-Type: application/json" \
  -d '{
    "username": "testuser002",
    "email": "testuser002@example.com",
    "fullName": "测试用户002",
    "password": "123456",
    "confirmPassword": "654321"
  }' \
  -w "\nHTTP状态码: %{http_code}\n"
```

**预期结果**:
- HTTP状态码: 400
- 错误信息: "密码和确认密码不匹配"

### 1.5 参数验证测试

```bash
# 测试用例：缺少必填字段
curl -X POST http://localhost:8080/api/auth/register \
  -H "Content-Type: application/json" \
  -d '{
    "username": "testuser003",
    "email": "testuser003@example.com"
  }' \
  -w "\nHTTP状态码: %{http_code}\n"
```

**预期结果**:
- HTTP状态码: 400
- 错误信息: 包含字段验证错误

## 2. 用户登录测试

### 2.1 用户名登录测试

```bash
# 测试用例：使用用户名登录
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "usernameOrEmail": "admin",
    "password": "123456"
  }' \
  -c cookies.txt \
  -w "\nHTTP状态码: %{http_code}\n"
```

**预期结果**:
- HTTP状态码: 200
- 响应包含用户信息和token
- Cookie被保存到cookies.txt

### 2.2 邮箱登录测试

```bash
# 测试用例：使用邮箱登录
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "usernameOrEmail": "admin@example.com",
    "password": "123456"
  }' \
  -c cookies.txt \
  -w "\nHTTP状态码: %{http_code}\n"
```

**预期结果**:
- HTTP状态码: 200
- 响应包含用户信息和token

### 2.3 用户名不存在测试

```bash
# 测试用例：用户名不存在
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "usernameOrEmail": "nonexistent",
    "password": "123456"
  }' \
  -w "\nHTTP状态码: %{http_code}\n"
```

**预期结果**:
- HTTP状态码: 400
- 错误信息: "用户名或邮箱不存在"

### 2.4 密码错误测试

```bash
# 测试用例：密码错误
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "usernameOrEmail": "admin",
    "password": "wrongpassword"
  }' \
  -w "\nHTTP状态码: %{http_code}\n"
```

**预期结果**:
- HTTP状态码: 400
- 错误信息: "密码错误"

## 3. 用户信息获取测试

### 3.1 已登录状态获取用户信息

```bash
# 先登录获取Session
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{"usernameOrEmail": "admin", "password": "123456"}' \
  -c cookies.txt

# 测试用例：获取当前用户信息
curl -X GET http://localhost:8080/api/auth/me \
  -b cookies.txt \
  -w "\nHTTP状态码: %{http_code}\n"
```

**预期结果**:
- HTTP状态码: 200
- 响应包含完整的用户信息

### 3.2 未登录状态获取用户信息

```bash
# 测试用例：未登录状态获取用户信息
curl -X GET http://localhost:8080/api/auth/me \
  -w "\nHTTP状态码: %{http_code}\n"
```

**预期结果**:
- HTTP状态码: 401
- 错误信息: "请先登录"

## 4. 登录状态检查测试

### 4.1 未登录状态检查

```bash
# 测试用例：未登录状态检查
curl -X GET http://localhost:8080/api/auth/status \
  -w "\nHTTP状态码: %{http_code}\n"
```

**预期结果**:
- HTTP状态码: 200
- 响应信息: "未登录"

### 4.2 已登录状态检查

```bash
# 先登录
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{"usernameOrEmail": "admin", "password": "123456"}' \
  -c cookies.txt

# 测试用例：已登录状态检查
curl -X GET http://localhost:8080/api/auth/status \
  -b cookies.txt \
  -w "\nHTTP状态码: %{http_code}\n"
```

**预期结果**:
- HTTP状态码: 200
- 响应信息: "已登录"
- 包含用户信息

## 5. 用户登出测试

### 5.1 正常登出测试

```bash
# 先登录
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{"usernameOrEmail": "admin", "password": "123456"}' \
  -c cookies.txt

# 测试用例：正常登出
curl -X POST http://localhost:8080/api/auth/logout \
  -b cookies.txt \
  -w "\nHTTP状态码: %{http_code}\n"
```

**预期结果**:
- HTTP状态码: 200
- 响应信息: "登出成功"

### 5.2 登出后状态检查

```bash
# 登出后检查状态
curl -X GET http://localhost:8080/api/auth/status \
  -b cookies.txt \
  -w "\nHTTP状态码: %{http_code}\n"
```

**预期结果**:
- HTTP状态码: 200
- 响应信息: "未登录"

## 6. 权限验证测试

### 6.1 管理员权限测试

```bash
# 管理员登录
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{"usernameOrEmail": "admin", "password": "123456"}' \
  -c admin_cookies.txt

# 测试管理员权限接口
curl -X GET http://localhost:8080/api/test/admin \
  -b admin_cookies.txt \
  -w "\nHTTP状态码: %{http_code}\n"
```

**预期结果**:
- HTTP状态码: 200
- 响应信息: "管理员权限测试通过"

### 6.2 消费者权限测试

```bash
# 消费者登录
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{"usernameOrEmail": "user1", "password": "123456"}' \
  -c consumer_cookies.txt

# 测试消费者权限接口
curl -X GET http://localhost:8080/api/test/consumer \
  -b consumer_cookies.txt \
  -w "\nHTTP状态码: %{http_code}\n"
```

**预期结果**:
- HTTP状态码: 200
- 响应信息: "消费者权限测试通过"

### 6.3 权限不足测试

```bash
# 消费者尝试访问管理员接口
curl -X GET http://localhost:8080/api/test/admin \
  -b consumer_cookies.txt \
  -w "\nHTTP状态码: %{http_code}\n"
```

**预期结果**:
- HTTP状态码: 403
- 错误信息: "权限不足，需要角色: [ADMIN]"

## 7. 浏览器控制台测试

### 7.1 JavaScript测试脚本

```javascript
// 在浏览器控制台中运行
(async function testAuthAPI() {
  const baseURL = 'http://localhost:8080/api';
  
  // 测试注册
  console.log('🧪 测试用户注册...');
  try {
    const registerResponse = await fetch(`${baseURL}/auth/register`, {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      credentials: 'include',
      body: JSON.stringify({
        username: 'testuser_' + Date.now(),
        email: 'test' + Date.now() + '@example.com',
        fullName: '测试用户',
        password: '123456',
        confirmPassword: '123456'
      })
    });
    
    const registerResult = await registerResponse.json();
    console.log('注册结果:', registerResult);
    
    if (registerResponse.status === 201) {
      console.log('✅ 注册成功');
    } else {
      console.log('❌ 注册失败:', registerResult.message);
    }
  } catch (error) {
    console.error('❌ 注册请求错误:', error);
  }
  
  // 测试登录
  console.log('🧪 测试用户登录...');
  try {
    const loginResponse = await fetch(`${baseURL}/auth/login`, {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      credentials: 'include',
      body: JSON.stringify({
        usernameOrEmail: 'admin',
        password: '123456'
      })
    });
    
    const loginResult = await loginResponse.json();
    console.log('登录结果:', loginResult);
    
    if (loginResponse.status === 200) {
      console.log('✅ 登录成功');
      
      // 测试获取用户信息
      console.log('🧪 测试获取用户信息...');
      const userResponse = await fetch(`${baseURL}/auth/me`, {
        credentials: 'include'
      });
      
      if (userResponse.status === 200) {
        const userResult = await userResponse.json();
        console.log('✅ 获取用户信息成功:', userResult);
      } else {
        console.log('❌ 获取用户信息失败');
      }
      
      // 测试登出
      console.log('🧪 测试用户登出...');
      const logoutResponse = await fetch(`${baseURL}/auth/logout`, {
        method: 'POST',
        credentials: 'include'
      });
      
      if (logoutResponse.status === 200) {
        console.log('✅ 登出成功');
      } else {
        console.log('❌ 登出失败');
      }
      
    } else {
      console.log('❌ 登录失败:', loginResult.message);
    }
  } catch (error) {
    console.error('❌ 登录请求错误:', error);
  }
})();
```

## 8. Postman测试集合

### 8.1 环境变量设置

```json
{
  "name": "美食街认证API测试",
  "values": [
    {
      "key": "base_url",
      "value": "http://localhost:8080/api",
      "enabled": true
    },
    {
      "key": "username",
      "value": "admin",
      "enabled": true
    },
    {
      "key": "password", 
      "value": "123456",
      "enabled": true
    }
  ]
}
```

### 8.2 测试集合

```json
{
  "info": {
    "name": "认证API测试集合",
    "description": "美食街管理系统认证接口测试"
  },
  "item": [
    {
      "name": "用户注册",
      "request": {
        "method": "POST",
        "header": [
          {
            "key": "Content-Type",
            "value": "application/json"
          }
        ],
        "body": {
          "mode": "raw",
          "raw": "{\n  \"username\": \"testuser\",\n  \"email\": \"test@example.com\",\n  \"fullName\": \"测试用户\",\n  \"password\": \"123456\",\n  \"confirmPassword\": \"123456\"\n}"
        },
        "url": {
          "raw": "{{base_url}}/auth/register",
          "host": ["{{base_url}}"],
          "path": ["auth", "register"]
        }
      }
    },
    {
      "name": "用户登录",
      "request": {
        "method": "POST",
        "header": [
          {
            "key": "Content-Type",
            "value": "application/json"
          }
        ],
        "body": {
          "mode": "raw",
          "raw": "{\n  \"usernameOrEmail\": \"{{username}}\",\n  \"password\": \"{{password}}\"\n}"
        },
        "url": {
          "raw": "{{base_url}}/auth/login",
          "host": ["{{base_url}}"],
          "path": ["auth", "login"]
        }
      }
    },
    {
      "name": "获取当前用户",
      "request": {
        "method": "GET",
        "url": {
          "raw": "{{base_url}}/auth/me",
          "host": ["{{base_url}}"],
          "path": ["auth", "me"]
        }
      }
    },
    {
      "name": "检查登录状态",
      "request": {
        "method": "GET",
        "url": {
          "raw": "{{base_url}}/auth/status",
          "host": ["{{base_url}}"],
          "path": ["auth", "status"]
        }
      }
    },
    {
      "name": "用户登出",
      "request": {
        "method": "POST",
        "url": {
          "raw": "{{base_url}}/auth/logout",
          "host": ["{{base_url}}"],
          "path": ["auth", "logout"]
        }
      }
    }
  ]
}
```

## 9. 性能测试

### 9.1 并发登录测试

```bash
# 使用Apache Bench进行并发测试
ab -n 100 -c 10 -p login_data.json -T application/json http://localhost:8080/api/auth/login

# login_data.json 内容:
{
  "usernameOrEmail": "admin",
  "password": "123456"
}
```

### 9.2 压力测试

```bash
# 使用wrk进行压力测试
wrk -t12 -c400 -d30s -s login.lua http://localhost:8080/api/auth/login

# login.lua 脚本:
wrk.method = "POST"
wrk.body = '{"usernameOrEmail":"admin","password":"123456"}'
wrk.headers["Content-Type"] = "application/json"
```

## 10. 测试报告模板

### 10.1 测试结果记录

| 测试用例 | 预期结果 | 实际结果 | 状态 | 备注 |
|----------|----------|----------|------|------|
| 正常注册 | 201 Created | ✅ | PASS | 用户成功创建 |
| 用户名重复 | 400 Bad Request | ✅ | PASS | 正确返回错误信息 |
| 邮箱重复 | 400 Bad Request | ✅ | PASS | 正确返回错误信息 |
| 密码不匹配 | 400 Bad Request | ✅ | PASS | 正确返回错误信息 |
| 用户名登录 | 200 OK | ✅ | PASS | 登录成功 |
| 邮箱登录 | 200 OK | ✅ | PASS | 登录成功 |
| 用户名不存在 | 400 Bad Request | ✅ | PASS | 正确返回错误信息 |
| 密码错误 | 400 Bad Request | ✅ | PASS | 正确返回错误信息 |
| 获取用户信息(已登录) | 200 OK | ✅ | PASS | 返回用户信息 |
| 获取用户信息(未登录) | 401 Unauthorized | ✅ | PASS | 正确返回未授权 |
| 状态检查(未登录) | 200 OK | ✅ | PASS | 返回未登录状态 |
| 状态检查(已登录) | 200 OK | ✅ | PASS | 返回已登录状态 |
| 正常登出 | 200 OK | ✅ | PASS | 登出成功 |
| 登出后状态检查 | 200 OK | ✅ | PASS | 返回未登录状态 |

### 10.2 测试总结

- **总测试用例数**: 14
- **通过用例数**: 14
- **失败用例数**: 0
- **通过率**: 100%
- **测试环境**: Windows 10, Java 8, Spring Boot 2.7.18
- **测试时间**: 2024-01-01
- **测试人员**: 开发团队

## 11. 故障排除

### 11.1 常见问题

1. **连接被拒绝**
   - 检查后端服务是否启动
   - 确认端口8080未被占用

2. **Session丢失**
   - 检查Cookie设置
   - 确认withCredentials配置

3. **权限验证失败**
   - 检查用户角色设置
   - 确认权限注解配置

### 11.2 调试技巧

```bash
# 启用详细日志
curl -v -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{"usernameOrEmail": "admin", "password": "123456"}'

# 查看响应头
curl -I http://localhost:8080/api/auth/status

# 保存和查看Cookie
curl -c cookies.txt -b cookies.txt http://localhost:8080/api/auth/login
cat cookies.txt
```

## 12. 自动化测试脚本

### 12.1 Shell脚本

```bash
#!/bin/bash
# auth_api_test.sh

BASE_URL="http://localhost:8080/api"
COOKIE_FILE="test_cookies.txt"

echo "开始认证API测试..."

# 测试注册
echo "测试用户注册..."
REGISTER_RESPONSE=$(curl -s -w "%{http_code}" -X POST $BASE_URL/auth/register \
  -H "Content-Type: application/json" \
  -d '{
    "username": "testuser_'$(date +%s)'",
    "email": "test'$(date +%s)'@example.com",
    "fullName": "测试用户",
    "password": "123456",
    "confirmPassword": "123456"
  }')

HTTP_CODE="${REGISTER_RESPONSE: -3}"
if [ "$HTTP_CODE" = "201" ]; then
    echo "✅ 注册测试通过"
else
    echo "❌ 注册测试失败，HTTP状态码: $HTTP_CODE"
fi

# 测试登录
echo "测试用户登录..."
LOGIN_RESPONSE=$(curl -s -w "%{http_code}" -X POST $BASE_URL/auth/login \
  -H "Content-Type: application/json" \
  -d '{"usernameOrEmail": "admin", "password": "123456"}' \
  -c $COOKIE_FILE)

HTTP_CODE="${LOGIN_RESPONSE: -3}"
if [ "$HTTP_CODE" = "200" ]; then
    echo "✅ 登录测试通过"
else
    echo "❌ 登录测试失败，HTTP状态码: $HTTP_CODE"
    exit 1
fi

# 测试获取用户信息
echo "测试获取用户信息..."
USER_RESPONSE=$(curl -s -w "%{http_code}" -X GET $BASE_URL/auth/me -b $COOKIE_FILE)

HTTP_CODE="${USER_RESPONSE: -3}"
if [ "$HTTP_CODE" = "200" ]; then
    echo "✅ 获取用户信息测试通过"
else
    echo "❌ 获取用户信息测试失败，HTTP状态码: $HTTP_CODE"
fi

# 测试登出
echo "测试用户登出..."
LOGOUT_RESPONSE=$(curl -s -w "%{http_code}" -X POST $BASE_URL/auth/logout -b $COOKIE_FILE)

HTTP_CODE="${LOGOUT_RESPONSE: -3}"
if [ "$HTTP_CODE" = "200" ]; then
    echo "✅ 登出测试通过"
else
    echo "❌ 登出测试失败，HTTP状态码: $HTTP_CODE"
fi

# 清理
rm -f $COOKIE_FILE

echo "认证API测试完成！"

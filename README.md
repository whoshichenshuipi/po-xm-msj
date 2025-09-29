# 美食街管理系统

这是一个基于 Java 8 和 Spring Boot 2.7.18 的美食街管理系统，包含了完整的用户认证、权限管理和业务功能模块。

## 项目特性

- **Java 8** 支持
- **Spring Boot 2.7.18** 框架
- **Spring Data JPA** 数据访问层
- **MyBatis-Plus** ORM框架
- **MySQL 8** 数据库
- **AOP权限管理** 基于切面的权限控制
- **Session认证** 用户登录状态管理
- **RESTful API** 设计
- **数据验证** 支持
- **单元测试** 覆盖
- **Lombok** 简化代码
- **Swagger文档** API文档自动生成

## 技术栈

- Java 8
- Spring Boot 2.7.18
- Spring Data JPA
- MyBatis-Plus
- MySQL 8
- Spring AOP
- Maven
- Lombok
- JUnit 4
- Swagger/OpenAPI 3

## 项目结构

```
src/
├── main/
│   ├── java/
│   │   └── com/example/
│   │       ├── Application.java              # 主启动类
│   │       ├── annotation/                   # 自定义注解
│   │       │   └── RequirePermission.java    # 权限注解
│   │       ├── aspect/                       # AOP切面
│   │       │   └── PermissionAspect.java     # 权限验证切面
│   │       ├── config/                       # 配置类
│   │       │   └── MybatisPlusConfig.java   # MyBatis-Plus配置
│   │       ├── controller/                   # 控制器层
│   │       │   ├── AuthController.java       # 认证控制器
│   │       │   ├── UserController.java       # 用户管理控制器
│   │       │   ├── MerchantController.java   # 商户管理控制器
│   │       │   ├── ProductController.java    # 产品管理控制器
│   │       │   ├── ReservationController.java # 预订管理控制器
│   │       │   ├── ReviewController.java     # 评价管理控制器
│   │       │   └── PermissionTestController.java # 权限测试控制器
│   │       ├── dto/                          # 数据传输对象
│   │       │   ├── AuthResponse.java         # 认证响应DTO
│   │       │   ├── LoginRequest.java         # 登录请求DTO
│   │       │   ├── RegisterRequest.java     # 注册请求DTO
│   │       │   └── UserResponse.java         # 用户响应DTO
│   │       ├── entity/                       # 实体类
│   │       │   ├── User.java                 # 用户实体
│   │       │   ├── UserRole.java             # 用户角色枚举
│   │       │   ├── Merchant.java             # 商户实体
│   │       │   ├── Product.java              # 产品实体
│   │       │   ├── Reservation.java          # 预订实体
│   │       │   └── Review.java               # 评价实体
│   │       ├── mapper/                       # MyBatis映射器
│   │       │   ├── UserMapper.java           # 用户映射器
│   │       │   ├── MerchantMapper.java       # 商户映射器
│   │       │   ├── ProductMapper.java        # 产品映射器
│   │       │   ├── ReservationMapper.java    # 预订映射器
│   │       │   └── ReviewMapper.java         # 评价映射器
│   │       ├── repository/                   # 数据访问层
│   │       │   ├── UserRepository.java       # 用户数据访问
│   │       │   └── CulturalContentRepository.java # 文化内容数据访问
│   │       ├── service/                      # 服务层
│   │       │   ├── AuthService.java          # 认证服务
│   │       │   ├── UserService.java          # 用户服务
│   │       │   ├── MerchantService.java       # 商户服务
│   │       │   ├── ProductService.java       # 产品服务
│   │       │   ├── ReservationService.java   # 预订服务
│   │       │   └── ReviewService.java        # 评价服务
│   │       └── util/                         # 工具类
│   │           └── PermissionUtil.java       # 权限工具类
│   └── resources/
│       ├── application.yml                   # 配置文件
│       ├── schema.sql                        # 数据库结构
│       ├── data.sql                          # 初始化数据
│       └── static/                           # 静态资源
│           ├── index.html                    # 主页
│           ├── login.html                    # 登录页面
│           └── register.html                 # 注册页面
└── test/
    └── java/
        └── com/example/
            ├── ApplicationTests.java         # 主测试类
            ├── controller/
            │   └── UserControllerTest.java   # 控制器测试
            └── test/
                ├── AuthServiceTest.java     # 认证服务测试
                └── PermissionSystemTest.java # 权限系统测试
```

## 快速开始

### 环境要求

- JDK 8 或更高版本
- Maven 3.6 或更高版本
- MySQL 8.0 或更高版本
- Node.js 16+ (前端开发)

### 数据库配置

1. **创建数据库**
   ```sql
   CREATE DATABASE food_street CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
   ```

2. **配置数据库连接**
   修改 `src/main/resources/application.yml` 中的数据库配置：
   ```yaml
   spring:
     datasource:
       url: jdbc:mysql://localhost:3306/food_street?useUnicode=true&characterEncoding=utf8&useSSL=false&serverTimezone=Asia/Shanghai
       username: root
       password: your_password
   ```

### 运行项目

1. **克隆项目**
   ```bash
   git clone <repository-url>
   cd bo-xm-brack
   ```

2. **编译项目**
   ```bash
   mvn clean compile
   ```

3. **运行项目**
   ```bash
   mvn spring-boot:run
   ```


### 使用 Maven 打包

```bash
mvn clean package
java -jar target/spring-boot-demo-1.0.0.jar
```

## API 接口

### 认证相关接口

#### 用户注册
- `POST /api/auth/register` - 用户注册
- `POST /api/auth/login` - 用户登录
- `POST /api/auth/logout` - 用户登出
- `GET /api/auth/me` - 获取当前用户信息
- `GET /api/auth/status` - 检查登录状态

#### 用户管理
- `POST /api/users` - 创建用户（管理员）
- `GET /api/users` - 获取所有用户（管理员）
- `GET /api/users/{id}` - 根据ID获取用户
- `PUT /api/users/{id}` - 更新用户（管理员）
- `DELETE /api/users/{id}` - 删除用户（管理员）

#### 商户管理
- `GET /api/merchants` - 获取商户列表
- `POST /api/merchants` - 创建商户（管理员）
- `GET /api/merchants/{id}` - 获取商户详情
- `PUT /api/merchants/{id}` - 更新商户信息
- `DELETE /api/merchants/{id}` - 删除商户（管理员）

#### 产品管理
- `GET /api/products` - 获取产品列表
- `POST /api/products` - 创建产品（商户/管理员）
- `GET /api/products/{id}` - 获取产品详情
- `PUT /api/products/{id}` - 更新产品（商户/管理员）
- `DELETE /api/products/{id}` - 删除产品（商户/管理员）

#### 预订管理
- `GET /api/reservations` - 获取预订列表
- `POST /api/reservations` - 创建预订（消费者）
- `GET /api/reservations/{id}` - 获取预订详情
- `PUT /api/reservations/{id}` - 更新预订状态
- `DELETE /api/reservations/{id}` - 取消预订

#### 评价管理
- `GET /api/reviews` - 获取评价列表
- `POST /api/reviews` - 创建评价（消费者）
- `DELETE /api/reviews/{id}` - 删除评价（管理员）

#### 权限测试接口
- `GET /api/test/admin` - 管理员权限测试
- `GET /api/test/merchant` - 商户权限测试
- `GET /api/test/consumer` - 消费者权限测试
- `GET /api/test/multi-role` - 多角色权限测试
- `GET /api/test/public` - 公开接口测试

### 示例请求

#### 用户注册
```bash
curl -X POST http://localhost:8080/api/auth/register \
  -H "Content-Type: application/json" \
  -d '{
    "username": "newuser",
    "email": "newuser@example.com",
    "fullName": "新用户",
    "password": "123456",
    "confirmPassword": "123456"
  }'
```

#### 用户登录
```bash
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "usernameOrEmail": "admin",
    "password": "123456"
  }'
```

#### 获取当前用户信息
```bash
curl -X GET http://localhost:8080/api/auth/me \
  -H "Cookie: JSESSIONID=your_session_id"
```

## 数据库配置

项目使用 MySQL 8.0 数据库，配置信息如下：

- **数据库类型**: MySQL 8.0
- **数据库名**: food_street
- **连接URL**: jdbc:mysql://localhost:3306/food_street
- **用户名**: root
- **密码**: 根据实际配置
- **字符集**: utf8mb4

### 测试账号

系统预置了以下测试账号：

| 用户名 | 邮箱 | 密码 | 角色 | 说明 |
|--------|------|------|------|------|
| admin | admin@example.com | 123456 | ADMIN | 系统管理员 |
| user1 | user1@example.com | 123456 | CONSUMER | 普通消费者 |
| user2 | user2@example.com | 123456 | MERCHANT | 商户用户 |

## 权限系统

### 角色定义

系统定义了三种用户角色：

- **ADMIN（管理员）**: 拥有系统所有权限，可以管理用户、商户、产品等
- **MERCHANT（商户）**: 可以管理自己的产品、处理订单、查看评价等
- **CONSUMER（消费者）**: 可以浏览产品、创建预订、发表评价等

### 权限控制

系统使用AOP（面向切面编程）实现权限控制：

### 认证方式

- **Session认证**: 使用HTTP Session管理用户登录状态
- **Cookie支持**: 自动处理Session Cookie
- **跨域支持**: 支持前后端分离部署

## 文档

- [认证接口API文档](API_AUTH_DOCUMENTATION.md) - 详细的认证接口说明
- [API测试示例](API_TEST_EXAMPLES.md) - 完整的API测试用例
- [登录注册功能指南](LOGIN_REGISTER_GUIDE.md) - 登录注册功能使用说明
- [权限系统说明](PERMISSION_SYSTEM_README.md) - 权限系统详细说明

## 配置说明

主要配置文件 `src/main/resources/application.yml`：

- **服务端口**: 8080
- **上下文路径**: /api
- **数据库**: MySQL 8.0
- **JPA**: 自动创建表结构
- **日志**: 控制台输出，包含SQL日志
- **Session**: 30分钟超时
- **跨域**: 支持所有来源

## 测试

运行单元测试：

```bash
mvn test
```

运行认证相关测试：

```bash
mvn test -Dtest=AuthServiceTest
mvn test -Dtest=PermissionSystemTest
```

## 开发说明

### 添加新功能

1. 在 `entity` 包中创建实体类
2. 在 `repository` 包中创建数据访问接口
3. 在 `service` 包中创建业务逻辑
4. 在 `controller` 包中创建REST接口
5. 添加相应的单元测试


#### 首页

https://bo-spj.oss-cn-beijing.aliyuncs.com/bo-spj-sp/%E5%B1%8F%E5%B9%95%E6%88%AA%E5%9B%BE%202025-09-29%20133607.png?Expires=1759125035&OSSAccessKeyId=TMP.3KpEHdzkdMccorGswRs9U9wBRrUqF8MtF88MyEDK4Wgf4eGJ31i5ZGw7oNqHL68EHZZpztkgQMHnE5JW4TtTKywMaWvFJo&Signature=EfgQNlwe6bHmhYDge56YjPmOF60%3D

### 代码规范

- 使用 Lombok 简化代码
- 遵循 RESTful API 设计规范
- 添加适当的注释和文档
- 编写单元测试覆盖主要功能

乱码问题 项目采用的 UFT-8
一般出现乱码就是 UTF-8 和 GBK 二者相反
请百度 IDEA 乱码和 Eclipse 乱码问题（描述清楚即可）
⭐

点击交互按钮，没有发生反应。
很明显，请求失败，浏览器打开开发者工具，Edge 浏览器直接 ctrl+shift+i ，其他浏览器按 F12
查看红色的请求和响应状态码问题
⭐

先阅读文档再进行问题的查询或者提问
提问有技巧，模糊的发言，让高级架构师找BUG也无从下手
⭐

QQ：2268713376
## 许可证

MIT License

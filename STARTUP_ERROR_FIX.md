# Spring Boot 启动错误修复

## 问题描述

启动Spring Boot应用时遇到以下错误：

```
APPLICATION FAILED TO START
Field permissionValidator in com.example.controller.MerchantApprovalController required a bean of type 'com.example.util.PermissionValidator' that could not be found.
```

## 问题原因

在`MerchantApprovalController`中，错误地尝试使用`@Autowired`注入`PermissionValidator`：

```java
@Autowired
private PermissionValidator permissionValidator;
```

但是`PermissionValidator`是一个工具类，所有方法都是`static`的，并且没有`@Component`或`@Service`注解，因此不能被Spring管理为Bean。

## 解决方案

**从Controller中移除不必要的依赖注入**：

修改前：
```java
@Autowired
private MerchantApprovalMapper approvalMapper;

@Autowired
private PermissionValidator permissionValidator;  // 错误：不需要注入
```

修改后：
```java
@Autowired
private MerchantApprovalMapper approvalMapper;
```

**其他修复**：

1. **修复JSON转换问题**：在`MerchantCulturalRatingController`中，将`ObjectMapper.convertValue()`改为简单的字符串拼接方式生成JSON字符串。

## 验证方法

重新启动应用，应该不会再出现这个错误：

```bash
mvn spring-boot:run
```

或者：

```bash
./mvnw spring-boot:run
```

## 相关文件

- ✅ `src/main/java/com/example/controller/MerchantApprovalController.java`
- ✅ `src/main/java/com/example/controller/MerchantCulturalRatingController.java`

## 注意事项

`PermissionValidator`是一个工具类，所有方法都是静态的，不需要被Spring管理。如果需要使用其中的方法，应该直接调用静态方法，而不是通过依赖注入。

例如：

```java
// 正确的方式
UserResponse currentUser = PermissionValidator.getCurrentUser(request);

// 错误的方式
@Autowired
private PermissionValidator permissionValidator;
```


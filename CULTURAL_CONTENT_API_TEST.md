# 文化内容API测试指南

## 📋 API优化说明

已优化文化内容控制器的400错误问题，主要改进包括：

### 🔧 问题分析
1. **验证注解缺失**: `type`字段没有`@NotNull`验证
2. **错误处理不完善**: 缺少详细的错误信息返回
3. **日志记录不足**: 难以排查问题原因
4. **前后端接口不匹配**: 部分API接口缺失

### ✅ 优化内容

#### 1. 实体类验证增强
```java
@NotNull(message = "文化内容类型不能为空")
@Enumerated(EnumType.STRING)
@Column(nullable = false, length = 32)
private CulturalType type;
```

#### 2. 控制器错误处理优化
- 添加`BindingResult`参数处理验证错误
- 详细的错误信息返回
- 完整的日志记录
- 异常捕获和处理

#### 3. 新增API接口
- `GET /cultural/approved` - 获取已审核内容（分页）
- `POST /cultural/{id}/approve` - 审核通过
- `POST /cultural/{id}/reject` - 审核拒绝
- `POST /cultural/{id}/view` - 增加浏览量

## 🧪 API测试用例

### 1. 创建文化内容 (POST /cultural)

#### 正确的请求示例：
```json
{
  "title": "传统茶艺文化体验",
  "summary": "深入了解中国传统茶艺文化的精髓",
  "content": "茶艺是中华文化的重要组成部分，通过学习茶艺，可以感受到中华文化的博大精深...",
  "type": "FOLK_CUSTOM",
  "tags": "茶艺,传统文化,文化体验",
  "merchantId": 1
}
```

#### 错误请求示例（缺少type）：
```json
{
  "title": "传统茶艺文化体验",
  "summary": "深入了解中国传统茶艺文化的精髓",
  "content": "茶艺是中华文化的重要组成部分..."
}
```

**错误响应：**
```json
{
  "error": "文化内容类型不能为空",
  "supportedTypes": ["FOOD_CULTURE", "FOLK_CUSTOM", "CRAFT_SKILL", "EVENT_HISTORY"]
}
```

#### 错误请求示例（空标题）：
```json
{
  "title": "",
  "type": "FOLK_CUSTOM",
  "content": "内容..."
}
```

**错误响应：**
```json
{
  "error": "数据验证失败",
  "details": {
    "title": "不能为空"
  }
}
```

### 2. 支持的文化类型
- `FOOD_CULTURE` - 食品文化
- `FOLK_CUSTOM` - 民俗文化  
- `CRAFT_SKILL` - 制作技艺
- `EVENT_HISTORY` - 活动历史

### 3. 其他API测试

#### 获取已审核内容
```
GET /cultural/approved?pageNo=1&pageSize=6
```

#### 审核通过
```
POST /cultural/1/approve
```

#### 审核拒绝
```
POST /cultural/1/reject
```

## 🐛 常见错误排查

### 400错误可能原因：
1. **缺少必需字段**: `title`或`type`为空
2. **字段长度超限**: `title`超过200字符，`summary`超过2000字符
3. **类型值错误**: `type`不是有效的枚举值
4. **JSON格式错误**: 请求体格式不正确

### 排查步骤：
1. 检查请求JSON格式是否正确
2. 确认必需字段是否提供
3. 验证字段值是否符合要求
4. 查看服务器日志获取详细错误信息

## 📝 测试建议

1. **使用Postman或类似工具测试API**
2. **先测试最小化的正确请求**
3. **逐步添加字段验证**
4. **检查服务器日志文件**
5. **确认数据库连接正常**

## 🔍 日志监控

服务器现在会记录以下信息：
- 接收到的请求内容
- 验证失败的详细信息  
- 成功操作的确认
- 异常错误的堆栈跟踪

查看日志命令（如果使用Spring Boot默认配置）：
```bash
tail -f logs/spring.log
```

或在控制台查看实时日志。

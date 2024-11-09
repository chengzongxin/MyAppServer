# 用户管理系统

## 开发日志

### 2024-11-09 更新：添加用户登录功能

#### 1. 更新数据库表结构
```sql
-- 修改用户表，添加必要字段
ALTER TABLE user 
ADD COLUMN username VARCHAR(50) NOT NULL COMMENT '用户名',
ADD COLUMN created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
ADD COLUMN updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
ADD COLUMN status TINYINT DEFAULT 1 COMMENT '用户状态：0-禁用，1-正常',
ADD COLUMN last_login_time TIMESTAMP NULL COMMENT '最后登录时间',
ADD UNIQUE KEY uk_username (username),
ADD UNIQUE KEY uk_email (email);

-- 完整的用户表结构
CREATE TABLE user (
    id INT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) NOT NULL COMMENT '用户名',
    name VARCHAR(50) NOT NULL COMMENT '姓名',
    email VARCHAR(50) NOT NULL COMMENT '邮箱',
    password VARCHAR(100) NOT NULL COMMENT '密码',
    status TINYINT DEFAULT 1 COMMENT '用户状态：0-禁用，1-正常',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    last_login_time TIMESTAMP NULL COMMENT '最后登录时间',
    UNIQUE KEY uk_username (username),
    UNIQUE KEY uk_email (email)
) COMMENT '用户表';
```

#### 2. 添加登录相关类

1. 创建登录请求DTO（LoginRequest.java）：
```java
@Data
public class LoginRequest {
    private String username;    // 支持用户名登录
    private String email;       // 支持邮箱登录
    private String password;    // 密码
}
```

2. 创建登录响应DTO（LoginResponse.java）：
```java
@Data
@Builder
public class LoginResponse {
    private Integer userId;
    private String username;
    private String name;
    private String email;
    private String token;
    private LocalDateTime lastLoginTime;
}
```

3. 更新 User 实体类，添加新字段：
```java
@Data
public class User {
    private Integer id;
    private String username;    // 用户名
    private String name;        // 姓名
    private String email;       // 邮箱
    private String password;    // 密码
    private Integer status;     // 用户状态：0-禁用，1-正常
    private LocalDateTime createdAt;    // 创建时间
    private LocalDateTime updatedAt;    // 更新时间
    private LocalDateTime lastLoginTime; // 最后登录时
}
```

#### 3. 更新 Mapper 接口
在 UserMapper.java 中添加新的方法：
```java
@Select("SELECT * FROM user WHERE username = #{username}")
User findByUsername(@Param("username") String username);

@Select("SELECT * FROM user WHERE email = #{email}")
User findByEmail(@Param("email") String email);

@Update("UPDATE user SET last_login_time = NOW() WHERE id = #{id}")
int updateLoginTime(@Param("id") Integer id);
```

#### 4. 添加登录功能
1. 在 UserService 接口中添加登录方法：
```java
LoginResponse login(LoginRequest loginRequest);
```

2. 在 UserServiceImpl 中实现登录逻辑：
- 支持用户名或邮箱登录
- 验证用户状态
- 验证密码
- 更新最后登录时间
- 生成 JWT token

3. 在 UserController 中添加登录接口：
```java
@PostMapping("/login")
public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest loginRequest) {
    return ResponseEntity.ok(userService.login(loginRequest));
}
```

#### 5. 测试登录功能
使用以下命令测试登录接口：
```bash
# 使用用户名登录
curl -X POST http://localhost:8080/api/users/login ^
-H "Content-Type: application/json" ^
-d "{\"username\":\"zhangsan\",\"password\":\"123456\"}"

# 使用邮箱登录
curl -X POST http://localhost:8080/api/users/login ^
-H "Content-Type: application/json" ^
-d "{\"email\":\"zhangsan@example.com\",\"password\":\"123456\"}"
```

### 注意事
1. 确保数据库表结构已更新
2. 用户名和邮箱都是唯一的
3. 密码使用 BCrypt 加密存储
4. 登录时会更新最后登录时间
5. 后续需要实现具体的 JWT token 生成逻辑

### 下一步计划
1. 实现 JWT token 生成和验证
2. 添加登录失败次数限制
3. 添加验证码功能
4. 实现"记住我"功能

## 技术栈

- Java 17
- Spring Boot 3.1.4
- MyBatis 3.0.3
- MySQL 8.0+
- Maven
- Lombok
- Spring Security Crypto (密码加密)
- JWT (用户认证)

## 环境要求

- JDK 17 或以上
- MySQL 8.0 或以上
- Maven 3.6 或以上
- IDE（推荐使用 IntelliJ IDEA）

## 快速开始

### 1. 数据库配置

```sql
CREATE DATABASE mydatabase;
USE mydatabase;

CREATE TABLE user (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(50) NOT NULL,
    email VARCHAR(50),
    password VARCHAR(100) NOT NULL
);
```

### 2. 项目配置

1. 添加依赖到 pom.xml：
```xml
<!-- 密码加密 -->
<dependency>
    <groupId>org.springframework.security</groupId>
    <artifactId>spring-security-crypto</artifactId>
</dependency>
<!-- JWT 相关 -->
<dependency>
    <groupId>io.jsonwebtoken</groupId>
    <artifactId>jjwt-api</artifactId>
    <version>0.11.5</version>
</dependency>
<dependency>
    <groupId>io.jsonwebtoken</groupId>
    <artifactId>jjwt-impl</artifactId>
    <version>0.11.5</version>
    <scope>runtime</scope>
</dependency>
<dependency>
    <groupId>io.jsonwebtoken</groupId>
    <artifactId>jjwt-jackson</artifactId>
    <version>0.11.5</version>
    <scope>runtime</scope>
</dependency>
<!-- Spring Security -->
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-security</artifactId>
</dependency>
```

2. 配置数据库连接（application.yml）：
```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/mydatabase?useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true
    username: root
    password: your_password
    driver-class-name: com.mysql.cj.jdbc.Driver
```

3. 配置 Spring Security：
创建 WebSecurityConfig.java 配置类来禁用默认的安全设置，允许所有请求通过。这是因为我们目前只需要使用密码加密功能，不需要完整的安全框架。

### 注意事项

1. 如果遇到 ClassNotFoundException，请确保：
   - 已添加所有必要的依赖
   - 已执行 Maven 更新 (mvn clean install)
   - IDE 已正确刷新项目

### 3. API 测试命令

基础用户管理接口：
```bash
REM 1. 创建用户
curl -X POST http://localhost:8080/api/users ^
-H "Content-Type: application/json" ^
-d "{\"name\":\"张三\",\"email\":\"zhangsan@example.com\",\"password\":\"123456\"}"

REM 2. 查看所有用户
curl -X GET http://localhost:8080/api/users

REM 3. 查看指定用户
curl -X GET http://localhost:8080/api/users/1

REM 4. 更新用户
curl -X PUT http://localhost:8080/api/users/1 ^
-H "Content-Type: application/json" ^
-d "{\"name\":\"张三改\",\"email\":\"zhangsan_new@example.com\",\"password\":\"123456\"}"

REM 5. 删除用户
curl -X DELETE http://localhost:8080/api/users/1
```

用户登录接口：
```bash
REM 用户登录
curl -X POST http://localhost:8080/api/users/login ^
-H "Content-Type: application/json" ^
-d "{\"email\":\"zhangsan@example.com\",\"password\":\"123456\"}"
```

### 4. 项目结构
```
src/main/java/com/example/myappserver/
├── MyAppServerApplication.java
├── config/
│   └── SecurityConfig.java (密码加密配置)
├── controller/
│   └── UserController.java (API接口)
├── service/
│   ├── UserService.java
│   └── impl/
│       └── UserServiceImpl.java
├── mapper/
│   └── UserMapper.java (数据库操)
├── model/
│   └── User.java (用户实体)
├── dto/
│   ├── LoginRequest.java (登录请求)
│   └── LoginResponse.java (登录响应)
└── exception/
    └── BusinessException.java (务异常)
```

### 5. 功能说明

1. 用户管理基础功能：
   - 创建用户
   - 查询用户
   - 更新用户
   - 删除用户

2. 用户登录功能：
   - 密码加密存储
   - JWT token 认证
   - 登录异常处理

### 6. 注意事项

1. 密码安全：
   - 使用 BCrypt 加密存储
   - 禁止明文传输
   - 建议定期更换密码

2. 接口安全：
   - 登录接口限流
   - 密码错误次数限制
   - Token 过期时间控制

## 后续优化计划

1. 添加参数验证
2. 完善异常处理
3. 添加接口文档（Swagger）
4. 添加日志记录
5. 实现完整的权限控制
6. 添加验证码功能
7. 添加登录状态记住功能

## 许可证

[MIT License](LICENSE)

### 7. API 文档访问

项目集成了 Swagger 文档，可以通过以下地址访问：
- Swagger UI：http://localhost:8080/swagger-ui.html
- OpenAPI 文档：http://localhost:8080/v3/api-docs

使用说明：
1. 启动项目
2. 在浏览器中访问 Swagger UI 地址
3. 可以查看所有接口的详细信息
4. 支持在线调试接口

注意事项：
1. 生产环境建议关闭 Swagger
2. 可以通过配置文件控制是否启用 Swagger

### 7. API 文档（Swagger）

1. 配置步骤：
   ```xml
   <!-- 添加 Swagger 依赖 -->
   <dependency>
       <groupId>org.springdoc</groupId>
       <artifactId>springdoc-openapi-starter-webmvc-ui</artifactId>
       <version>2.1.0</version>
   </dependency>
   ```

2. 配置文件（application.yml）：
   ```yaml
   springdoc:
     swagger-ui:
       path: /swagger-ui.html
     api-docs:
       path: /v3/api-docs
     packages-to-scan: com.example.myappserver.controller
     paths-to-match: /api/**
   ```

3. 创建 Swagger 配置类：
   ```java
   @Configuration
   public class SwaggerConfig {
       @Bean
       public OpenAPI openAPI() {
           return new OpenAPI()
                   .info(new Info()
                           .title("用户管理系统 API 文档")
                           .description("这是一个用户管理系统的 API 文档")
                           .version("v1.0.0")
                           .contact(new Contact()
                                   .name("开发者")
                                   .email("developer@example.com")));
       }
   }
   ```

4. 访问地址：
   - Swagger UI：http://localhost:8080/swagger-ui.html
   - OpenAPI 档：http://localhost:8080/v3/api-docs

5. 主要注解说明：
   - @Tag：标记 Controller 类
   - @Operation：标记接口方法
   - @Parameter：标记参数
   - @Schema：标记实体类和字段
   - @ApiResponse：标记响应信息

6. 使用说明：
   - 启动项目后访问 Swagger UI 地址
   - 可以查看所有接口的详细信息
   - 支持在线调接口
   - 可以直接导出 API 文档

7. 注意事项：
   - 生产环境建议关闭 Swagger
   - 密码等敏感信息要谨慎处理
   - 定期更新文档内容
   - 保持示例数据的真实性

### 8. Swagger 在线调试指南

1. 访问 Swagger UI
   - 启动项目
   - 打开浏览器访问：http://localhost:8080/swagger-ui.html

2. 接口测试步骤
   1) 创建用户
      - 展开 "用户管理" 部分
      - 找到 POST /api/users 接口
      - 点击 "Try it out"
      - 输入测试数据：
      ```json
      {
        "username": "zhangsan",
        "name": "张三",
        "email": "zhangsan@example.com",
        "password": "123456",
        "status": 1
      }
      ```
      - 点击 "Execute"
      - 查看响应结果

   2) 用户登录
      - 找到 POST /api/users/login 接口
      - 点击 "Try it out"
      - 输入测试数据：
      ```json
      {
        "username": "zhangsan",
        "password": "123456"
      }
      ```
      - 点击 "Execute"
      - 查看响应结果

3. 响应说明
   - 200: 请求成功
   - 400: 请求参数错误
   - 401: 未授权
   - 404: 资源不存在
   - 500: 服务器错误

4. 调试技巧
   - 可以通过 "Schema" 查看数据结构
   - 响应示例可作为参考
   - 错误信息会显示在 Response body 中
   - 可以查看完整的请求和响应头

5. 注意事项
   - 测试前确保数据库已正确配置
   - 注意接口的参数要求
   - 密码等敏感信息不要使用真实数据
   - 测试完成后及时清理测试数据

6. 常见问题
   - 如果返回 404，检查项目是否正常启动
   - 如果返回 500，检查数据库连接
   - 如果显示跨域错误，检查 CORS 配置
   - 如果数据格式错误，参考 Schema 说明

### 测试用户登录功能

1. 首先创建用户：
```bash
curl -X POST http://localhost:8080/api/users ^
-H "Content-Type: application/json" ^
-d "{\"username\":\"zhangsan\",\"name\":\"张三\",\"email\":\"zhangsan@example.com\",\"password\":\"123456\",\"status\":1}"
```

2. 然后尝试登录：
```bash
curl -X POST http://localhost:8080/api/users/login ^
-H "Content-Type: application/json" ^
-d "{\"username\":\"zhangsan\",\"password\":\"123456\"}"
```

注意事项：
1. 确保数据库中的密码字段长度足够（建议 VARCHAR(100)）
2. 创建用户时密码会自动加密
3. 登录时会自动验证加密后的密码
4. 登录成功后会更新最后登录时间

### 密码验证问题排查

1. 检查数据库中的密码是否正确加密：
```sql
SELECT username, password FROM user WHERE username = 'zhangsan';
```

2. 确认创建用户流程：
   - 密码应该使用 BCryptPasswordEncoder 加密
   - 可以在日志中查看原始密码和加密后的密码
   - 数据库中存储的应该是加密后的密码

3. 登录流程验证：
   - 从数据库获取用户信息
   - 使用 passwordEncoder.matches() 比对密码
   - 日志中可以查看比对过程

4. 常见问题：
   - 密码没有正确加密存储
   - BCryptPasswordEncoder 没有正确注入
   - 数据库密码字段长度不够
   - 密码比对方法使用不当

### 9. 文件上传功能

#### 9.1 配置说明

1. 添加华为云 OBS 依赖：
```xml
<dependency>
    <groupId>com.huaweicloud</groupId>
    <artifactId>esdk-obs-java</artifactId>
    <version>3.23.9</version>
</dependency>
```

2. 配置 OBS 参数（application.yml）：
```yaml
huaweicloud:
  obs:
    ak: ${OBS_ACCESS_KEY}
    sk: ${OBS_SECRET_KEY}
    endpoint: https://obs.cn-north-4.myhuaweicloud.com
    bucketName: your-bucket-name
```

#### 9.2 接口说明

1. 上传文件
- 请求方式：POST
- URL：/api/files/upload
- 参数：
  - file: 文件（MultipartFile）
  - directory: 存储目录（可选，默认为 "images"）
- 响应示例：
```json
{
    "objectKey": "images/uuid.jpg",
    "url": "https://bucket-name.obs.cn-north-4.myhuaweicloud.com/images/uuid.jpg"
}
```

2. 删除文件
- 请求方式：DELETE
- URL：/api/files/{objectKey}
- 参数：
  - objectKey: 文件的唯一标识

3. 获取文件访问链接
- 请求方式：GET
- URL：/api/files/{objectKey}/url
- 参数：
  - objectKey: 文件的唯一标识
- 响应示例：
```
https://bucket-name.obs.cn-north-4.myhuaweicloud.com/images/uuid.jpg
```

#### 9.3 测试命令

1. 上传文件：
```bash
curl -X POST http://localhost:8080/api/files/upload ^
-F "file=@C:\path\to\image.jpg" ^
-F "directory=images"
```

2. 删除文件：
```bash
curl -X DELETE http://localhost:8080/api/files/images/uuid.jpg
```

3. 获取文件链接：
```bash
curl -X GET http://localhost:8080/api/files/images/uuid.jpg/url
```

#### 9.4 注意事项

1. 安全性：
   - 确保 AK/SK 不要硬编码在代码中
   - 建议使环境变量或配置中心
   - 生产环境建议启用桶的访问控制

2. 性能优化：
   - 建议限制上传文件的大小
   - 大文件建议使用分片上传
   - 可以配置 CDN 加速

3. 错误处理：
   - 文件类型限制
   - 文件大小限制
   - 存储空间检查
   - 网络异常处理

4. 最佳实践：
   - 使用唯一的文件名
   - 合理的目录结构
   - 定期清理无用文件
   - 监控存储使用情况

### 10. 用户头像功能

#### 10.1 数据库更新
```
-- 添加头像字段
ALTER TABLE user 
ADD COLUMN avatar_url VARCHAR(255) COMMENT '用户头像URL';
```

#### 10.2 更新用户头像
- 请求方式：POST
- URL：/api/users/{id}/avatar
- 参数：
  - id: 用户ID（路径参数）
  - file: 头像文件（MultipartFile）
- 响应示例：
```json
{
    "id": 1,
    "username": "zhangsan",
    "name": "张三",
    "email": "zhangsan@example.com",
    "avatarUrl": "https://obs.example.com/avatars/xxx.jpg",
    "status": 1
}
```

#### 10.3 测试命令
```bash
# 更新用户头像
curl -X POST http://localhost:8080/api/users/1/avatar ^
-F "file=@C:\path\to\avatar.jpg"
```

#### 10.4 注意事项
1. 文件大小限制：建议不超过2MB
2. 支持的文件格式：jpg、jpeg、png
3. 旧头像会被自动删除
4. 头像文件存储在 OBS 的 avatars 目录下

### 11. 用户头像接口使用指南

#### 11.1 接口说明

1. 更新用户头像
- 接口：POST /api/users/{id}/avatar
- 说明：上传或更新用户头像
- 参数：
  - id: 用户ID（路径参数）
  - file: 头像文件（form-data）
- 请求示例：
```bash
curl -X POST http://localhost:8080/api/users/1/avatar \
-H "Authorization: Bearer your-token-here" \
-F "file=@/path/to/avatar.jpg"
```
- 响应示例：
```json
{
    "id": 1,
    "username": "zhangsan",
    "name": "张三",
    "email": "zhangsan@example.com",
    "avatarUrl": "https://my-app.obs.cn-south-1.myhuaweicloud.com/avatars/xxx.jpg",
    "status": 1
}
```

#### 11.2 使用说明

1. 文件上传要求：
   - 支持的文件格式：jpg、jpeg、png
   - 最大文件大小：2MB
   - 建议图片尺寸：200x200 像素
   - 文件名要求：不含特殊字符

2. 调用步骤：
   ```javascript
   // 前端示例代码（使用 FormData）
   const formData = new FormData();
   formData.append('file', fileInput.files[0]);
   
   fetch('http://localhost:8080/api/users/1/avatar', {
     method: 'POST',
     headers: {
       'Authorization': 'Bearer your-token-here'
     },
     body: formData
   });
   ```

3. 注意事项：
   - 请求必须使用 multipart/form-data 格式
   - 文件字段名必须为 "file"
   - 需要携带用户认证 token
   - 旧头像会被自动删除

4. 错误处理：
   - 400：文件格式不支持或大小超限
   - 401：未授权（token无效）
   - 404：用户不存在
   - 500：服务器错误

#### 11.3 测试方法

1. 使用 curl 命令测试：
```bash
# Windows CMD
curl -X POST http://localhost:8080/api/users/1/avatar ^
-H "Authorization: Bearer your-token-here" ^
-F "file=@C:\path\to\avatar.jpg"

# PowerShell 或 Linux
curl -X POST http://localhost:8080/api/users/1/avatar \
-H "Authorization: Bearer your-token-here" \
-F "file=@/path\to\avatar.jpg"
```

2. 使用 Postman 测试：
   - 选择 POST 方法
   - 输入 URL：http://localhost:8080/api/users/1/avatar
   - 在 Headers 中添加 Authorization
   - 选择 Body > form-data
   - 添加 file 字段，选择文件

3. 使用 Swagger UI 测试：
   - 访问 http://localhost:8080/swagger-ui.html
   - 找到 /api/users/{id}/avatar 接口
   - 点击 "Try it out"
   - 输入用户ID
   - 上传文件
   - 点击 "Execute"

#### 11.4 最佳实践

1. 文件处理：
   - 上传前进行客户端文件格式验证
   - 压缩大图片再上传
   - 使用合适的图片格式（如 webp）

2. 错误处理：
   - 实现上传进度提示
   - 添加重试机制
   - 友好的错误提示

3. 性能优化：
   - 使用图片压缩
   - 配置 CDN 加速
   - 实现图片缓存

4. 安全考虑：
   - 验证文件类型
   - 限制文件大小
   - 防止恶意文件上传
   - 使用 HTTPS 传输

### 12. 博客模块接口文档

#### 12.1 博客文章接口

1. 获取所有文章
```bash
curl -X GET http://localhost:8080/api/posts
```

2. 获取文章详情
```bash
curl -X GET http://localhost:8080/api/posts/1
```

3. 创建文章
```bash
curl -X POST http://localhost:8080/api/posts \
-H "Content-Type: application/json" \
-d '{
    "title": "Spring Boot 入门教程",
    "content": "这是一篇关于 Spring Boot 的入门教程...",
    "summary": "本文介绍了 Spring Boot 的基本概念和使用方法",
    "categoryIds": [1, 2],
    "tags": ["Spring", "Java"]
}'
```

4. 更新文章
```bash
curl -X PUT http://localhost:8080/api/posts/1 \
-H "Content-Type: application/json" \
-d '{
    "title": "Spring Boot 入门教程（更新版）",
    "content": "更新后的内容...",
    "summary": "更新后的摘要",
    "categoryIds": [1, 2, 3],
    "tags": ["Spring", "Java", "Web"]
}'
```

5. 删除文章
```bash
curl -X DELETE http://localhost:8080/api/posts/1
```

6. 增加浏览量
```bash
curl -X POST http://localhost:8080/api/posts/1/view
```

7. 点赞文章
```bash
curl -X POST http://localhost:8080/api/posts/1/like
```

#### 12.2 博客分类接口

1. 获取所有分类
```bash
curl -X GET http://localhost:8080/api/categories
```

2. 创建分类
```bash
curl -X POST http://localhost:8080/api/categories \
-H "Content-Type: application/json" \
-d '{
    "name": "Java开发",
    "description": "Java相关的技术文章"
}'
```

3. 更新分类
```bash
curl -X PUT http://localhost:8080/api/categories/1 \
-H "Content-Type: application/json" \
-d '{
    "name": "Java进阶",
    "description": "Java进阶技术文章"
}'
```

4. 删除分类
```bash
curl -X DELETE http://localhost:8080/api/categories/1
```

#### 12.3 博客标签接口

1. 获取所有标签
```bash
curl -X GET http://localhost:8080/api/tags
```

2. 创建标签
```bash
curl -X POST http://localhost:8080/api/tags?name=Spring
```

3. 删除标签
```bash
curl -X DELETE http://localhost:8080/api/tags/1
```

4. 获取文章的标签
```bash
curl -X GET http://localhost:8080/api/tags/post/1
```

#### 12.4 博客评论接口

1. 获取文章评论
```bash
curl -X GET http://localhost:8080/api/comments/post/1
```

2. 创建评论
```bash
curl -X POST http://localhost:8080/api/comments \
-H "Content-Type: application/json" \
-d '{
    "postId": 1,
    "content": "这是一条评论",
    "parentId": null
}'
```

3. 删除评论
```bash
curl -X DELETE http://localhost:8080/api/comments/1
```

4. 获取评论回复
```bash
curl -X GET http://localhost:8080/api/comments/1/replies
```

5. 获取评论数量
```bash
curl -X GET http://localhost:8080/api/comments/post/1/count
```

#### 12.5 测试用例

1. 创建测试文章：
```bash
# 1. 先创建分类
curl -X POST http://localhost:8080/api/categories \
-H "Content-Type: application/json" \
-d '{"name": "Java教程", "description": "Java相关教程"}'

# 2. 创建标签
curl -X POST http://localhost:8080/api/tags?name=Spring
curl -X POST http://localhost:8080/api/tags?name=Java

# 3. 创建文章
curl -X POST http://localhost:8080/api/posts \
-H "Content-Type: application/json" \
-d '{
    "title": "Spring Boot 入门教程",
    "content": "这是一篇关于 Spring Boot 的入门教程...",
    "summary": "本文介绍了 Spring Boot 的基本概念和使用方法",
    "categoryIds": [1],
    "tags": ["Spring", "Java"]
}'
```

2. 测试评论功能：
```bash
# 1. 创建主评论
curl -X POST http://localhost:8080/api/comments \
-H "Content-Type: application/json" \
-d '{
    "postId": 1,
    "content": "很好的教程！",
    "parentId": null
}'

# 2. 创建回复
curl -X POST http://localhost:8080/api/comments \
-H "Content-Type: application/json" \
-d '{
    "postId": 1,
    "content": "谢谢支持！",
    "parentId": 1
}'
```

#### 12.6 注意事项

1. 文章管理：
   - 创建文章时必须指定分类
   - 标签是可选的
   - 删除文章会同时删除相关的评论

2. 分类管理：
   - 分类名称不能重复
   - 删除分类前需要确保没有文章使用该分类

3. 标签管理：
   - 标签名称不能重复
   - 创建文章时会自动创建不存在的标签

4. 评论管理：
   - 支持多级评论
   - 删除评论采用软删除
   - 评论数量会实时更新到文章表
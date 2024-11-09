# 用户管理系统

这是一个基于 Spring Boot 的用户管理系统，提供基础的用户 CRUD 操作。

## 技术栈

- Java 17
- Spring Boot 3.1.4
- MyBatis 3.0.3
- MySQL 8.0+
- Maven
- Lombok

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
    email VARCHAR(50)
);
```

### 2. Windows CMD 测试命令
以下命令可以直接复制到 CMD 中运行：

```bash
REM 1. 创建用户
curl -X POST http://localhost:8080/api/users -H "Content-Type: application/json" -d "{\"name\":\"张三\",\"email\":\"zhangsan@example.com\"}"

REM 2. 查看所有用户
curl -X GET http://localhost:8080/api/users

REM 3. 查看刚创建的用户
curl -X GET http://localhost:8080/api/users/1

REM 4. 更新用户信息
curl -X PUT http://localhost:8080/api/users/1 -H "Content-Type: application/json" -d "{\"name\":\"张三改\",\"email\":\"zhangsan_new@example.com\"}"

REM 5. 再次查看用户信息确认更新
curl -X GET http://localhost:8080/api/users/1

REM 6. 删除用户
curl -X DELETE http://localhost:8080/api/users/1

REM 7. 确认用户已被删除
curl -X GET http://localhost:8080/api/users/1
```

或者使用这种格式（命令换行更清晰）：

```bash
REM 1. 创建用户
curl -X POST http://localhost:8080/api/users ^
-H "Content-Type: application/json" ^
-d "{\"name\":\"张三\",\"email\":\"zhangsan@example.com\"}"

REM 2. 查看所有用户
curl -X GET http://localhost:8080/api/users

REM 3. 查看刚创建的用户
curl -X GET http://localhost:8080/api/users/1

REM 4. 更新用户信息
curl -X PUT http://localhost:8080/api/users/1 ^
-H "Content-Type: application/json" ^
-d "{\"name\":\"张三改\",\"email\":\"zhangsan_new@example.com\"}"

REM 5. 再次查看用户信息确认更新
curl -X GET http://localhost:8080/api/users/1

REM 6. 删除用户
curl -X DELETE http://localhost:8080/api/users/1

REM 7. 确认用户已被删除
curl -X GET http://localhost:8080/api/users/1
```

注意事项：
1. 确保项目已启动并运行在 8080 端口
2. Windows 10 默认已安装 curl，如果提示未找到 curl 命令，需要安装 curl
3. 命令中的 ^ 符号表示命令在下一行继续，可以让命令更清晰
4. REM 是 Windows 命令行的注释符号
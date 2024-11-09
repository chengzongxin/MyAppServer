package com.example.myappserver.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@Schema(description = "用户实体")
public class User {
    @Schema(description = "用户ID", example = "1")
    private Integer id;
    
    @Schema(description = "用户名", example = "zhangsan")
    private String username;
    
    @Schema(description = "姓名", example = "张三")
    private String name;
    
    @Schema(description = "邮箱", example = "zhangsan@example.com")
    private String email;
    
    @Schema(description = "密码", example = "123456")
    private String password;
    
    @Schema(description = "用户状态：0-禁用，1-正常", example = "1")
    private Integer status;
    
    @Schema(description = "创建时间")
    private LocalDateTime createdAt;
    
    @Schema(description = "更新时间")
    private LocalDateTime updatedAt;
    
    @Schema(description = "最后登录时间")
    private LocalDateTime lastLoginTime;
} 
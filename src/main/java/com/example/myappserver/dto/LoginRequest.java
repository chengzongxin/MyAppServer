package com.example.myappserver.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "登录请求")
public class LoginRequest {
    @Schema(description = "用户名", example = "zhangsan")
    private String username;
    
    @Schema(description = "邮箱", example = "zhangsan@example.com")
    private String email;
    
    @Schema(description = "密码", example = "123456")
    private String password;
} 
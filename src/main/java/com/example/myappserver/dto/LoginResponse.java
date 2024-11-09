package com.example.myappserver.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.Builder;
import java.time.LocalDateTime;

@Data
@Builder
@Schema(description = "登录响应")
public class LoginResponse {
    @Schema(description = "用户ID", example = "1")
    private Integer userId;
    
    @Schema(description = "用户名", example = "zhangsan")
    private String username;
    
    @Schema(description = "姓名", example = "张三")
    private String name;
    
    @Schema(description = "邮箱", example = "zhangsan@example.com")
    private String email;
    
    @Schema(description = "JWT Token", example = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...")
    private String token;
    
    @Schema(description = "最后登录时间")
    private LocalDateTime lastLoginTime;
} 
package com.example.myappserver.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
@Schema(description = "头像更新请求")
public class AvatarUpdateRequest {
    @Schema(description = "用户ID", example = "1")
    private Integer userId;
    
    @Schema(description = "头像文件")
    private MultipartFile file;
} 
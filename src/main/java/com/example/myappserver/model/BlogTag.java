package com.example.myappserver.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@Schema(description = "博客标签")
public class BlogTag {
    @Schema(description = "标签ID")
    private Integer id;
    
    @Schema(description = "标签名称")
    private String name;
    
    @Schema(description = "创建时间")
    private LocalDateTime createdAt;
} 
package com.example.myappserver.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@Schema(description = "博客分类")
public class BlogCategory {
    @Schema(description = "分类ID", example = "1")
    private Integer id;
    
    @Schema(description = "分类名称", example = "Java教程")
    private String name;
    
    @Schema(description = "分类描述", example = "Java相关教程")
    private String description;
    
    @Schema(description = "该分类下的文章数量", example = "10")
    private Integer postCount;
    
    @Schema(description = "创建时间")
    private LocalDateTime createdAt;
    
    @Schema(description = "更新时间")
    private LocalDateTime updatedAt;
} 
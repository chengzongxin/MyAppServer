package com.example.myappserver.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import java.util.List;

@Data
@Schema(description = "博客文章请求")
public class BlogPostRequest {
    @Schema(description = "文章标题", example = "Spring Boot 入门教程")
    private String title;
    
    @Schema(description = "文章内容", example = "这是一篇关于 Spring Boot 的入门教程...")
    private String content;
    
    @Schema(description = "文章摘要", example = "本文介绍了 Spring Boot 的基本概念和使用方法")
    private String summary;
    
    @Schema(description = "作者ID", example = "1")
    private Integer authorId;
    
    @Schema(description = "分类ID列表", example = "[1, 2]")
    private List<Integer> categoryIds;
    
    @Schema(description = "标签列表", example = "[\"Spring\", \"Java\"]")
    private List<String> tags;
} 
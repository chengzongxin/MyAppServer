package com.example.myappserver.dto;

import com.example.myappserver.model.BlogCategory;
import com.example.myappserver.model.BlogTag;
import com.example.myappserver.model.User;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.Builder;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@Schema(description = "博客文章响应")
public class BlogPostResponse {
    @Schema(description = "文章ID", example = "1")
    private Integer id;
    
    @Schema(description = "文章标题", example = "Spring Boot 入门教程")
    private String title;
    
    @Schema(description = "文章内容", example = "这是一篇关于 Spring Boot 的入门教程...")
    private String content;
    
    @Schema(description = "文章摘要", example = "本文介绍了 Spring Boot 的基本概念和使用方法")
    private String summary;
    
    @Schema(description = "封面图片URL")
    private String coverImage;
    
    @Schema(description = "作者信息")
    private User author;
    
    @Schema(description = "分类列表")
    private List<BlogCategory> categories;
    
    @Schema(description = "标签列表")
    private List<BlogTag> tags;
    
    @Schema(description = "浏览次数", example = "100")
    private Integer viewCount;
    
    @Schema(description = "点赞次数", example = "50")
    private Integer likeCount;
    
    @Schema(description = "评论次数", example = "30")
    private Integer commentCount;
    
    @Schema(description = "创建时间")
    private LocalDateTime createdAt;
    
    @Schema(description = "更新时间")
    private LocalDateTime updatedAt;
} 
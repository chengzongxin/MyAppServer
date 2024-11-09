package com.example.myappserver.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Schema(description = "博客文章")
public class BlogPost {
    @Schema(description = "文章ID")
    private Integer id;
    
    @Schema(description = "文章标题")
    private String title;
    
    @Schema(description = "文章内容")
    private String content;
    
    @Schema(description = "文章摘要")
    private String summary;
    
    @Schema(description = "封面图片URL")
    private String coverImage;
    
    @Schema(description = "作者ID")
    private Integer authorId;
    
    @Schema(description = "作者信息")
    private User author;
    
    @Schema(description = "状态：0-草稿，1-已发布，2-已删除")
    private Integer status;
    
    @Schema(description = "浏览次数")
    private Integer viewCount;
    
    @Schema(description = "点赞次数")
    private Integer likeCount;
    
    @Schema(description = "评论次数")
    private Integer commentCount;
    
    @Schema(description = "分类列表")
    private List<BlogCategory> categories;
    
    @Schema(description = "标签列表")
    private List<BlogTag> tags;
    
    @Schema(description = "创建时间")
    private LocalDateTime createdAt;
    
    @Schema(description = "更新时间")
    private LocalDateTime updatedAt;
} 
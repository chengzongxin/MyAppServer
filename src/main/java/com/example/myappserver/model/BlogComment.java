package com.example.myappserver.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@Schema(description = "博客评论")
public class BlogComment {
    @Schema(description = "评论ID")
    private Integer id;
    
    @Schema(description = "文章ID")
    private Integer postId;
    
    @Schema(description = "评论用户ID")
    private Integer userId;
    
    @Schema(description = "评论用户信息")
    private User user;
    
    @Schema(description = "评论内容")
    private String content;
    
    @Schema(description = "父评论ID")
    private Integer parentId;
    
    @Schema(description = "状态：0-已删除，1-正常")
    private Integer status;
    
    @Schema(description = "创建时间")
    private LocalDateTime createdAt;
} 
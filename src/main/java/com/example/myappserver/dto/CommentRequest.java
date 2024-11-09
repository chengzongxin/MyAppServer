package com.example.myappserver.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "评论请求")
public class CommentRequest {
    @Schema(description = "文章ID", example = "1")
    private Integer postId;
    
    @Schema(description = "评论内容", example = "这是一条评论")
    private String content;
    
    @Schema(description = "父评论ID（回复评论时使用）", example = "1")
    private Integer parentId;
} 
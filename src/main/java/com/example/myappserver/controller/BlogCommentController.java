package com.example.myappserver.controller;

import com.example.myappserver.model.BlogComment;
import com.example.myappserver.service.BlogCommentService;
import com.example.myappserver.util.JwtUtil;
import com.example.myappserver.dto.CommentRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Parameter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "博客评论", description = "博客评论管理接口")
@RestController
@RequestMapping("/api/comments")
@CrossOrigin
public class BlogCommentController {

    @Autowired
    private BlogCommentService blogCommentService;

    @Autowired
    private JwtUtil jwtUtil;

    @Operation(summary = "获取文章的所有评论")
    @GetMapping("/post/{postId}")
    public ResponseEntity<List<BlogComment>> getCommentsByPostId(
            @Parameter(description = "文章ID") @PathVariable Integer postId) {
        return ResponseEntity.ok(blogCommentService.findByPostId(postId));
    }

    @Operation(summary = "获取评论详情")
    @GetMapping("/{id}")
    public ResponseEntity<BlogComment> getCommentById(
            @Parameter(description = "评论ID") @PathVariable Integer id) {
        return ResponseEntity.ok(blogCommentService.findById(id));
    }

    @Operation(summary = "创建评论")
    @PostMapping
    public ResponseEntity<BlogComment> createComment(
            @Parameter(description = "评论信息") @RequestBody CommentRequest request,
            @RequestHeader("Authorization") String authorization) {
        String token = authorization.substring(7); // 去掉 "Bearer "
        Integer userId = jwtUtil.getUserIdFromToken(token);
        
        BlogComment comment = new BlogComment();
        comment.setPostId(request.getPostId());
        comment.setContent(request.getContent());
        comment.setParentId(request.getParentId());
        comment.setUserId(userId);
        
        return ResponseEntity.ok(blogCommentService.create(comment));
    }

    @Operation(summary = "删除评论")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteComment(
            @Parameter(description = "评论ID") @PathVariable Integer id) {
        blogCommentService.delete(id);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "获取评论的回复")
    @GetMapping("/{parentId}/replies")
    public ResponseEntity<List<BlogComment>> getReplies(
            @Parameter(description = "父评论ID") @PathVariable Integer parentId) {
        return ResponseEntity.ok(blogCommentService.findReplies(parentId));
    }

    @Operation(summary = "获取文章的评论数")
    @GetMapping("/post/{postId}/count")
    public ResponseEntity<Integer> getCommentCount(
            @Parameter(description = "文章ID") @PathVariable Integer postId) {
        return ResponseEntity.ok(blogCommentService.countByPostId(postId));
    }
} 
package com.example.myappserver.controller;

import com.example.myappserver.model.BlogTag;
import com.example.myappserver.service.BlogTagService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Parameter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "博客标签", description = "博客标签管理接口")
@RestController
@RequestMapping("/api/tags")
@CrossOrigin
public class BlogTagController {

    @Autowired
    private BlogTagService blogTagService;

    @Operation(summary = "获取所有标签")
    @GetMapping
    public ResponseEntity<List<BlogTag>> getAllTags() {
        return ResponseEntity.ok(blogTagService.findAll());
    }

    @Operation(summary = "获取标签详情")
    @GetMapping("/{id}")
    public ResponseEntity<BlogTag> getTagById(
            @Parameter(description = "标签ID") @PathVariable Integer id) {
        return ResponseEntity.ok(blogTagService.findById(id));
    }

    @Operation(summary = "创建标签")
    @PostMapping
    public ResponseEntity<BlogTag> createTag(
            @Parameter(description = "标签名称") @RequestParam String name) {
        return ResponseEntity.ok(blogTagService.createIfNotExists(name));
    }

    @Operation(summary = "删除标签")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTag(
            @Parameter(description = "标签ID") @PathVariable Integer id) {
        blogTagService.delete(id);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "获取文章的所有标签")
    @GetMapping("/post/{postId}")
    public ResponseEntity<List<BlogTag>> getTagsByPostId(
            @Parameter(description = "文章ID") @PathVariable Integer postId) {
        return ResponseEntity.ok(blogTagService.findByPostId(postId));
    }
} 
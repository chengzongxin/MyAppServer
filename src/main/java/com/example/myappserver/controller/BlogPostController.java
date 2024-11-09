package com.example.myappserver.controller;

import com.example.myappserver.dto.BlogPostRequest;
import com.example.myappserver.model.BlogCategory;
import com.example.myappserver.model.BlogPost;
import com.example.myappserver.model.BlogTag;
import com.example.myappserver.service.BlogPostService;
import com.example.myappserver.util.JwtUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Parameter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@Tag(name = "博客文章", description = "博客文章管理接口")
@RestController
@RequestMapping("/api/posts")
@CrossOrigin
public class BlogPostController {

    @Autowired
    private BlogPostService blogPostService;

    @Autowired
    private JwtUtil jwtUtil;

    @Operation(summary = "获取所有文章")
    @GetMapping
    public ResponseEntity<List<BlogPost>> getAllPosts() {
        return ResponseEntity.ok(blogPostService.findAll());
    }

    @Operation(summary = "获取文章详情")
    @GetMapping("/{id}")
    public ResponseEntity<BlogPost> getPostById(
            @Parameter(description = "文章ID") @PathVariable Integer id) {
        return ResponseEntity.ok(blogPostService.findById(id));
    }

    @Operation(summary = "创建文章")
    @PostMapping
    public ResponseEntity<BlogPost> createPost(
            @Parameter(description = "文章信息") @RequestBody BlogPostRequest request,
            @RequestHeader("Authorization") String authorization) {
        // 从 token 中获取用户ID
        String token = authorization.substring(7); // 去掉 "Bearer "
        Integer userId = jwtUtil.getUserIdFromToken(token);
        
        // 将 DTO 转换为实体
        BlogPost post = new BlogPost();
        post.setTitle(request.getTitle());
        post.setContent(request.getContent());
        post.setSummary(request.getSummary());
        post.setAuthorId(userId);  // 设置作者ID
        
        // 设置分类
        if (request.getCategoryIds() != null) {
            List<BlogCategory> categories = request.getCategoryIds().stream()
                .map(id -> {
                    BlogCategory category = new BlogCategory();
                    category.setId(id);
                    return category;
                })
                .collect(Collectors.toList());
            post.setCategories(categories);
        }
        
        // 设置标签
        if (request.getTags() != null) {
            List<BlogTag> tags = request.getTags().stream()
                .map(name -> {
                    BlogTag tag = new BlogTag();
                    tag.setName(name);
                    return tag;
                })
                .collect(Collectors.toList());
            post.setTags(tags);
        }
        
        return ResponseEntity.ok(blogPostService.create(post));
    }

    @Operation(summary = "更新文章")
    @PutMapping("/{id}")
    public ResponseEntity<BlogPost> updatePost(
            @Parameter(description = "文章ID") @PathVariable Integer id,
            @Parameter(description = "文章信息") @RequestBody BlogPost post) {
        post.setId(id);
        return ResponseEntity.ok(blogPostService.update(post));
    }

    @Operation(summary = "删除文章")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePost(
            @Parameter(description = "文章ID") @PathVariable Integer id) {
        blogPostService.delete(id);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "增加文章浏览量")
    @PostMapping("/{id}/view")
    public ResponseEntity<Void> incrementViewCount(
            @Parameter(description = "文章ID") @PathVariable Integer id) {
        blogPostService.incrementViewCount(id);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "点赞文章")
    @PostMapping("/{id}/like")
    public ResponseEntity<Void> incrementLikeCount(
            @Parameter(description = "文章ID") @PathVariable Integer id) {
        blogPostService.incrementLikeCount(id);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "添加文章分类")
    @PostMapping("/{postId}/categories/{categoryId}")
    public ResponseEntity<Void> addCategory(
            @Parameter(description = "文章ID") @PathVariable Integer postId,
            @Parameter(description = "分类ID") @PathVariable Integer categoryId) {
        blogPostService.addCategory(postId, categoryId);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "添加文章标签")
    @PostMapping("/{postId}/tags/{tagId}")
    public ResponseEntity<Void> addTag(
            @Parameter(description = "文章ID") @PathVariable Integer postId,
            @Parameter(description = "标签ID") @PathVariable Integer tagId) {
        blogPostService.addTag(postId, tagId);
        return ResponseEntity.ok().build();
    }
} 
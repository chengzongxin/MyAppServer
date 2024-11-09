package com.example.myappserver.controller;

import com.example.myappserver.model.BlogCategory;
import com.example.myappserver.service.BlogCategoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Parameter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "博客分类", description = "博客分类管理接口")
@RestController
@RequestMapping("/api/categories")
@CrossOrigin
public class BlogCategoryController {

    @Autowired
    private BlogCategoryService blogCategoryService;

    @Operation(summary = "获取所有分类")
    @GetMapping
    public ResponseEntity<List<BlogCategory>> getAllCategories() {
        return ResponseEntity.ok(blogCategoryService.findAll());
    }

    @Operation(summary = "获取分类详情")
    @GetMapping("/{id}")
    public ResponseEntity<BlogCategory> getCategoryById(
            @Parameter(description = "分类ID") @PathVariable Integer id) {
        return ResponseEntity.ok(blogCategoryService.findById(id));
    }

    @Operation(summary = "创建分类")
    @PostMapping
    public ResponseEntity<BlogCategory> createCategory(
            @Parameter(description = "分类信息") @RequestBody BlogCategory category) {
        return ResponseEntity.ok(blogCategoryService.create(category));
    }

    @Operation(summary = "更新分类")
    @PutMapping("/{id}")
    public ResponseEntity<BlogCategory> updateCategory(
            @Parameter(description = "分类ID") @PathVariable Integer id,
            @Parameter(description = "分类信息") @RequestBody BlogCategory category) {
        category.setId(id);
        return ResponseEntity.ok(blogCategoryService.update(category));
    }

    @Operation(summary = "删除分类")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCategory(
            @Parameter(description = "分类ID") @PathVariable Integer id) {
        blogCategoryService.delete(id);
        return ResponseEntity.ok().build();
    }
} 
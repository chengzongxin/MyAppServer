package com.example.myappserver.service;

import com.example.myappserver.model.BlogCategory;
import java.util.List;

public interface BlogCategoryService {
    List<BlogCategory> findAll();
    BlogCategory findById(Integer id);
    BlogCategory create(BlogCategory category);
    BlogCategory update(BlogCategory category);
    void delete(Integer id);
} 
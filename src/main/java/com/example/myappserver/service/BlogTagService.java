package com.example.myappserver.service;

import com.example.myappserver.model.BlogTag;
import java.util.List;

public interface BlogTagService {
    List<BlogTag> findAll();
    BlogTag findById(Integer id);
    BlogTag findByName(String name);
    BlogTag createIfNotExists(String name);
    void delete(Integer id);
    List<BlogTag> findByPostId(Integer postId);
} 
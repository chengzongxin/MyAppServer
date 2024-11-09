package com.example.myappserver.service;

import com.example.myappserver.model.BlogPost;
import java.util.List;

public interface BlogPostService {
    List<BlogPost> findAll(int offset, int size, String search, Integer categoryId);
    BlogPost findById(Integer id);
    BlogPost create(BlogPost post);
    BlogPost update(BlogPost post);
    void delete(Integer id);
    void incrementViewCount(Integer id);
    void incrementLikeCount(Integer id);
    void addCategory(Integer postId, Integer categoryId);
    void addTag(Integer postId, Integer tagId);
    void removeCategory(Integer postId, Integer categoryId);
    void removeTag(Integer postId, Integer tagId);
    List<BlogPost> findByCategoryId(Integer categoryId);
} 
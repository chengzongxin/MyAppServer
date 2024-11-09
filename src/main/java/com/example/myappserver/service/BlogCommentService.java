package com.example.myappserver.service;

import com.example.myappserver.model.BlogComment;
import java.util.List;

public interface BlogCommentService {
    List<BlogComment> findByPostId(Integer postId);
    BlogComment findById(Integer id);
    BlogComment create(BlogComment comment);
    void delete(Integer id);
    List<BlogComment> findReplies(Integer parentId);
    int countByPostId(Integer postId);
} 
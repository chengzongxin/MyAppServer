package com.example.myappserver.service.impl;

import com.example.myappserver.mapper.BlogCommentMapper;
import com.example.myappserver.mapper.BlogPostMapper;
import com.example.myappserver.model.BlogComment;
import com.example.myappserver.service.BlogCommentService;
import com.example.myappserver.exception.BusinessException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class BlogCommentServiceImpl implements BlogCommentService {

    @Autowired
    private BlogCommentMapper blogCommentMapper;
    
    @Autowired
    private BlogPostMapper blogPostMapper;

    @Override
    public List<BlogComment> findByPostId(Integer postId) {
        return blogCommentMapper.findByPostId(postId);
    }

    @Override
    public BlogComment findById(Integer id) {
        return blogCommentMapper.findById(id);
    }

    @Override
    @Transactional
    public BlogComment create(BlogComment comment) {
        // 设置默认状态
        comment.setStatus(1);
        
        // 如果是回复评论，验证父评论是否存在
        if (comment.getParentId() != null) {
            BlogComment parentComment = findById(comment.getParentId());
            if (parentComment == null) {
                throw new BusinessException("父评论不存在");
            }
        }
        
        // 保存评论
        blogCommentMapper.insert(comment);
        
        // 更新文章评论数
        blogPostMapper.incrementCommentCount(comment.getPostId());
        
        return findById(comment.getId());
    }

    @Override
    public void delete(Integer id) {
        BlogComment comment = findById(id);
        if (comment == null) {
            throw new BusinessException("评论不存在");
        }
        blogCommentMapper.delete(id);
    }

    @Override
    public List<BlogComment> findReplies(Integer parentId) {
        return blogCommentMapper.findReplies(parentId);
    }

    @Override
    public int countByPostId(Integer postId) {
        return blogCommentMapper.countByPostId(postId);
    }
} 
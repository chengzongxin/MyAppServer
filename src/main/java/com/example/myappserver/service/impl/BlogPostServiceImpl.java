package com.example.myappserver.service.impl;

import com.example.myappserver.mapper.BlogPostMapper;
import com.example.myappserver.mapper.BlogTagMapper;
import com.example.myappserver.mapper.BlogCategoryMapper;
import com.example.myappserver.model.BlogCategory;
import com.example.myappserver.model.BlogPost;
import com.example.myappserver.model.BlogTag;
import com.example.myappserver.service.BlogPostService;
import com.example.myappserver.exception.BusinessException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class BlogPostServiceImpl implements BlogPostService {

    @Autowired
    private BlogPostMapper blogPostMapper;

    @Autowired
    private BlogTagMapper blogTagMapper;

    @Autowired
    private BlogCategoryMapper blogCategoryMapper;

    @Override
    public List<BlogPost> findAll() {
        return blogPostMapper.findAll();
    }

    @Override
    public BlogPost findById(Integer id) {
        BlogPost post = blogPostMapper.findById(id);
        if (post == null) {
            throw new BusinessException("文章不存在");
        }
        return post;
    }

    @Override
    @Transactional
    public BlogPost create(BlogPost post) {
        // 设置默认值
        post.setStatus(1); // 1-已发布
        post.setViewCount(0);
        post.setLikeCount(0);
        post.setCommentCount(0);
        
        // 验证分类是否存在
        if (post.getCategories() != null) {
            for (BlogCategory category : post.getCategories()) {
                BlogCategory existingCategory = blogCategoryMapper.findById(category.getId());
                if (existingCategory == null) {
                    throw new BusinessException("分类不存在: " + category.getId());
                }
            }
        }
        
        // 保存文章
        blogPostMapper.insert(post);
        
        // 处理分类
        if (post.getCategories() != null) {
            post.getCategories().forEach(category -> 
                blogPostMapper.insertCategory(post.getId(), category.getId()));
        }
        
        // 处理标签
        if (post.getTags() != null) {
            post.getTags().forEach(tag -> {
                // 先查找或创建标签
                BlogTag existingTag = blogTagMapper.findByName(tag.getName());
                if (existingTag == null) {
                    blogTagMapper.insert(tag);
                    existingTag = tag;
                }
                // 关联文章和标签
                blogPostMapper.insertTag(post.getId(), existingTag.getId());
            });
        }
        
        return findById(post.getId());
    }

    @Override
    @Transactional
    public BlogPost update(BlogPost post) {
        BlogPost existingPost = findById(post.getId());
        
        // 更新基本信息
        blogPostMapper.update(post);
        
        // 更新分类
        if (post.getCategories() != null) {
            blogPostMapper.deleteCategories(post.getId());
            post.getCategories().forEach(category -> 
                blogPostMapper.insertCategory(post.getId(), category.getId()));
        }
        
        // 更新标签
        if (post.getTags() != null) {
            blogPostMapper.deleteTags(post.getId());
            post.getTags().forEach(tag -> 
                blogPostMapper.insertTag(post.getId(), tag.getId()));
        }
        
        return findById(post.getId());
    }

    @Override
    @Transactional
    public void delete(Integer id) {
        BlogPost post = findById(id);
        blogPostMapper.delete(id);
    }

    @Override
    public void incrementViewCount(Integer id) {
        blogPostMapper.incrementViewCount(id);
    }

    @Override
    public void incrementLikeCount(Integer id) {
        blogPostMapper.incrementLikeCount(id);
    }

    @Override
    public void addCategory(Integer postId, Integer categoryId) {
        blogPostMapper.insertCategory(postId, categoryId);
    }

    @Override
    public void addTag(Integer postId, Integer tagId) {
        blogPostMapper.insertTag(postId, tagId);
    }

    @Override
    public void removeCategory(Integer postId, Integer categoryId) {
        blogPostMapper.deleteCategories(postId);
    }

    @Override
    public void removeTag(Integer postId, Integer tagId) {
        blogPostMapper.deleteTags(postId);
    }
} 
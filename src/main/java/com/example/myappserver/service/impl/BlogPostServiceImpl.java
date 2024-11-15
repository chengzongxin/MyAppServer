package com.example.myappserver.service.impl;

import com.example.myappserver.mapper.BlogPostMapper;
import com.example.myappserver.mapper.BlogTagMapper;
import com.example.myappserver.mapper.BlogCategoryMapper;
import com.example.myappserver.model.BlogCategory;
import com.example.myappserver.model.BlogPost;
import com.example.myappserver.model.BlogTag;
import com.example.myappserver.service.BlogPostService;
import com.example.myappserver.exception.BusinessException;
import com.example.myappserver.dto.PageResponse;
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
    public List<BlogPost> findAll(int offset, int size, String search, Integer categoryId) {
        return blogPostMapper.findAll(offset, size, search, categoryId);
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
                blogPostMapper.insertTag(post.getId(), existingTag.getId());
            });
        }
        
        return findById(post.getId());
    }

    @Override
    @Transactional
    public BlogPost update(BlogPost post) {
        // 获取原有文章信息
        BlogPost existingPost = findById(post.getId());
        if (existingPost == null) {
            throw new BusinessException("文章不存在");
        }
        
        // 保持原有的作者ID和状态
        post.setAuthorId(existingPost.getAuthorId());
        post.setStatus(existingPost.getStatus());
        
        // 更新基本信息
        blogPostMapper.update(post);
        
        // 更新分类
        if (post.getCategories() != null) {
            // 先删除所有旧分类
            blogPostMapper.deleteCategories(post.getId());
            // 添加新分类
            post.getCategories().forEach(category -> {
                // 验证分类是否存在
                BlogCategory existingCategory = blogCategoryMapper.findById(category.getId());
                if (existingCategory == null) {
                    throw new BusinessException("分类不存在: " + category.getId());
                }
                blogPostMapper.insertCategory(post.getId(), category.getId());
            });
        }
        
        // 更新标签
        if (post.getTags() != null) {
            // 先删除所有旧标签
            blogPostMapper.deleteTags(post.getId());
            // 添加新标签
            post.getTags().forEach(tag -> {
                // 查找或创建标签
                BlogTag existingTag = blogTagMapper.findByName(tag.getName());
                if (existingTag == null) {
                    blogTagMapper.insert(tag);
                    existingTag = tag;
                }
                blogPostMapper.insertTag(post.getId(), existingTag.getId());
            });
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

    @Override
    public List<BlogPost> findByCategoryId(Integer categoryId) {
        // 使用已有的 findAll 方法，传入分类ID，不分页
        return blogPostMapper.findAll(0, Integer.MAX_VALUE, null, categoryId);
    }
} 
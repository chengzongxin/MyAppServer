package com.example.myappserver.service.impl;

import com.example.myappserver.mapper.BlogCategoryMapper;
import com.example.myappserver.model.BlogCategory;
import com.example.myappserver.service.BlogCategoryService;
import com.example.myappserver.exception.BusinessException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BlogCategoryServiceImpl implements BlogCategoryService {

    @Autowired
    private BlogCategoryMapper blogCategoryMapper;

    @Override
    public List<BlogCategory> findAll() {
        return blogCategoryMapper.findAll();
    }

    @Override
    public BlogCategory findById(Integer id) {
        BlogCategory category = blogCategoryMapper.findById(id);
        if (category == null) {
            throw new BusinessException("分类不存在");
        }
        return category;
    }

    @Override
    public BlogCategory create(BlogCategory category) {
        blogCategoryMapper.insert(category);
        return findById(category.getId());
    }

    @Override
    public BlogCategory update(BlogCategory category) {
        // 验证分类是否存在
        findById(category.getId());
        
        blogCategoryMapper.update(category);
        return findById(category.getId());
    }

    @Override
    public void delete(Integer id) {
        // 验证分类是否存在
        findById(id);
        
        // TODO: 可以添加检查是否有文章使用此分类
        blogCategoryMapper.delete(id);
    }
} 
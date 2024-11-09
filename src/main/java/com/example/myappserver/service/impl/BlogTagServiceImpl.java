package com.example.myappserver.service.impl;

import com.example.myappserver.mapper.BlogTagMapper;
import com.example.myappserver.model.BlogTag;
import com.example.myappserver.service.BlogTagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BlogTagServiceImpl implements BlogTagService {

    @Autowired
    private BlogTagMapper blogTagMapper;

    @Override
    public List<BlogTag> findAll() {
        return blogTagMapper.findAll();
    }

    @Override
    public BlogTag findById(Integer id) {
        return blogTagMapper.findById(id);
    }

    @Override
    public BlogTag findByName(String name) {
        return blogTagMapper.findByName(name);
    }

    @Override
    public BlogTag createIfNotExists(String name) {
        // 先查找是否已存在
        BlogTag existingTag = blogTagMapper.findByName(name);
        if (existingTag != null) {
            return existingTag;
        }
        
        // 不存在则创建新标签
        BlogTag newTag = new BlogTag();
        newTag.setName(name);
        blogTagMapper.insert(newTag);
        return newTag;
    }

    @Override
    public void delete(Integer id) {
        blogTagMapper.delete(id);
    }

    @Override
    public List<BlogTag> findByPostId(Integer postId) {
        return blogTagMapper.findByPostId(postId);
    }
} 
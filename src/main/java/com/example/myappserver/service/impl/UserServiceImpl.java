package com.example.myappserver.service.impl;

import com.example.myappserver.mapper.UserMapper;
import com.example.myappserver.model.User;
import com.example.myappserver.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {
    
    @Autowired
    private UserMapper userMapper;
    
    @Override
    public List<User> findAll() {
        return userMapper.findAll();
    }
    
    @Override
    public User findById(Integer id) {
        return userMapper.findById(id);
    }
    
    @Override
    public User create(User user) {
        userMapper.insert(user);
        return user;
    }
    
    @Override
    public User update(User user) {
        userMapper.update(user);
        return user;
    }
    
    @Override
    public void delete(Integer id) {
        userMapper.delete(id);
    }
} 
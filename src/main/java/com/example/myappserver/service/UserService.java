package com.example.myappserver.service;

import com.example.myappserver.model.User;
import java.util.List;

public interface UserService {
    List<User> findAll();
    User findById(Integer id);
    User create(User user);
    User update(User user);
    void delete(Integer id);
} 
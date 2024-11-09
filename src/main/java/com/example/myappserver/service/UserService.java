package com.example.myappserver.service;

import com.example.myappserver.model.User;
import com.example.myappserver.dto.LoginRequest;
import com.example.myappserver.dto.LoginResponse;
import java.util.List;

public interface UserService {
    List<User> findAll();
    User findById(Integer id);
    User create(User user);
    User update(User user);
    void delete(Integer id);
    LoginResponse login(LoginRequest loginRequest);
} 
package com.example.myappserver.service.impl;

import com.example.myappserver.mapper.UserMapper;
import com.example.myappserver.model.User;
import com.example.myappserver.service.UserService;
import com.example.myappserver.dto.LoginRequest;
import com.example.myappserver.dto.LoginResponse;
import com.example.myappserver.exception.BusinessException;
import com.example.myappserver.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class UserServiceImpl implements UserService {
    
    @Autowired
    private UserMapper userMapper;
    
    @Autowired
    private BCryptPasswordEncoder passwordEncoder;
    
    @Autowired
    private JwtUtil jwtUtil;
    
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
        try {
            // 加密密码
            String rawPassword = user.getPassword();
            String encodedPassword = passwordEncoder.encode(rawPassword);
            
            // 打印详细信息
            System.out.println("密码加密过程:");
            System.out.println("原始密码: " + rawPassword);
            System.out.println("加密后的密码: " + encodedPassword);
            System.out.println("加密密码的组成部分:");
            String[] parts = encodedPassword.split("\\$");
            System.out.println("- 算法版本: " + parts[1]);
            System.out.println("- 加密强度: " + parts[2]);
            System.out.println("- salt+hash: " + parts[3]);
            
            user.setPassword(encodedPassword);
            user.setStatus(1);
            userMapper.insert(user);
            return user;
        } catch (Exception e) {
            System.out.println("创建用户失败: " + e.getMessage());
            throw new BusinessException("创建用户失败: " + e.getMessage());
        }
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
    
    @Override
    public LoginResponse login(LoginRequest loginRequest) {
        User user = null;
        
        if (loginRequest.getUsername() != null) {
            user = userMapper.findByUsername(loginRequest.getUsername());
        } else if (loginRequest.getEmail() != null) {
            user = userMapper.findByEmail(loginRequest.getEmail());
        }
        
        if (user == null) {
            throw new BusinessException("用户不存在");
        }
        
        if (user.getStatus() == 0) {
            throw new BusinessException("用户已被禁用");
        }
        
        // 验证密码（这里假设密码是明文存储，实际项目中应该加密）
        if (!loginRequest.getPassword().equals(user.getPassword())) {
            throw new BusinessException("密码错误");
        }
        
        // 生成 JWT token
        String token = jwtUtil.generateToken(user);
        
        // 更新最后登录时间
        userMapper.updateLoginTime(user.getId());
        
        return LoginResponse.builder()
                .userId(user.getId())
                .username(user.getUsername())
                .name(user.getName())
                .email(user.getEmail())
                .token(token)
                .lastLoginTime(LocalDateTime.now())
                .build();
    }
} 
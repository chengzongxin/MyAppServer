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
            // 1. 加密密码
            String rawPassword = user.getPassword();  // 假设是 "123456"
            String encodedPassword = passwordEncoder.encode(rawPassword);
            
            // 打印调试信息
            System.out.println("用户创建密码信息：");
            System.out.println("用户名：" + user.getUsername());
            System.out.println("原始密码：" + rawPassword);
            System.out.println("加密后密码：" + encodedPassword);
            
            // 2. 设置加密后的密码
            user.setPassword(encodedPassword);
            
            // 3. 设置其他默认值
            user.setStatus(1);
            
            // 4. 保存用户
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
        
        // 1. 根据用户名或邮箱查找用户
        if (loginRequest.getUsername() != null) {
            user = userMapper.findByUsername(loginRequest.getUsername());
        } else if (loginRequest.getEmail() != null) {
            user = userMapper.findByEmail(loginRequest.getEmail());
        }
        
        if (user == null) {
            throw new BusinessException("用户不存在");
        }
        
        // 2. 验证用户状态（添加空值检查）
        Integer status = user.getStatus();
        if (status == null) {
            // 如果状态为空，设置默认值
            status = 1;
            user.setStatus(status);
        }
        
        if (status == 0) {
            throw new BusinessException("用户已被禁用");
        }
        
        // 3. 验证密码
        String rawPassword = loginRequest.getPassword();
        String encodedPassword = user.getPassword();
        
        // 打印调试信息
        System.out.println("登录验证信息：");
        System.out.println("用户名：" + user.getUsername());
        System.out.println("输入的原始密码：" + rawPassword);
        System.out.println("数据库中的密码：" + encodedPassword);
        
        if (!passwordEncoder.matches(rawPassword, encodedPassword)) {
            System.out.println("密码验证失败");
            throw new BusinessException("密码错误");
        }
        
        System.out.println("密码验证成功");
        
        // 4. 生成 JWT token
        String token = jwtUtil.generateToken(user);
        
        // 5. 更新最后登录时间
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
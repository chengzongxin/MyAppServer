package com.example.myappserver.service.impl;

import com.example.myappserver.mapper.UserMapper;
import com.example.myappserver.model.User;
import com.example.myappserver.service.UserService;
import com.example.myappserver.service.FileService;
import com.example.myappserver.dto.LoginRequest;
import com.example.myappserver.dto.LoginResponse;
import com.example.myappserver.exception.BusinessException;
import com.example.myappserver.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Service
public class UserServiceImpl implements UserService {
    
    @Autowired
    private UserMapper userMapper;
    
    @Autowired
    private BCryptPasswordEncoder passwordEncoder;
    
    @Autowired
    private JwtUtil jwtUtil;
    
    @Autowired
    private FileService fileService;
    
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
        // 1. 加密密码
        String rawPassword = user.getPassword();
        String encodedPassword = passwordEncoder.encode(rawPassword);
        user.setPassword(encodedPassword);
        
        // 2. 设置默认值
        user.setStatus(1);
        
        // 3. 保存用户
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
    
    @Override
    public LoginResponse login(LoginRequest loginRequest) {
        // 1. 查找用户
        User user = null;
        if (loginRequest.getUsername() != null) {
            user = userMapper.findByUsername(loginRequest.getUsername());
        } else if (loginRequest.getEmail() != null) {
            user = userMapper.findByEmail(loginRequest.getEmail());
        }
        
        if (user == null) {
            throw new BusinessException("用户不存在");
        }
        
        // 2. 验证状态
        if (user.getStatus() == 0) {
            throw new BusinessException("用户已被禁用");
        }
        
        // 3. 验证密码
        if (!passwordEncoder.matches(loginRequest.getPassword(), user.getPassword())) {
            throw new BusinessException("密码错误");
        }
        
        // 4. 生成token
        String token = jwtUtil.generateToken(user);
        
        // 5. 更新登录时间
        userMapper.updateLoginTime(user.getId());
        
        // 6. 返回登录响应
        return LoginResponse.builder()
                .userId(user.getId())
                .username(user.getUsername())
                .name(user.getName())
                .email(user.getEmail())
                .token(token)
                .lastLoginTime(LocalDateTime.now())
                .build();
    }
    
    @Override
    public User updateAvatar(Integer userId, MultipartFile file) {
        // 1. 验证用户
        User user = findById(userId);
        if (user == null) {
            throw new BusinessException("用户不存在");
        }
        
        try {
            // 2. 上传文件
            Map<String, String> uploadResult = fileService.uploadFile(file, "avatars");
            String avatarUrl = uploadResult.get("url");
            
            // 3. 删除旧头像
            String oldAvatarUrl = user.getAvatarUrl();
            if (oldAvatarUrl != null && !oldAvatarUrl.isEmpty()) {
                String oldObjectKey = oldAvatarUrl.substring(oldAvatarUrl.lastIndexOf("/") + 1);
                fileService.deleteFile("avatars/" + oldObjectKey);
            }
            
            // 4. 更新头像URL
            userMapper.updateAvatar(userId, avatarUrl);
            user.setAvatarUrl(avatarUrl);
            
            return user;
        } catch (Exception e) {
            throw new BusinessException("更新头像失败: " + e.getMessage());
        }
    }
} 
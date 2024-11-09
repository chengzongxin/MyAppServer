package com.example.myappserver.controller;

import com.example.myappserver.model.User;
import com.example.myappserver.service.UserService;
import com.example.myappserver.dto.LoginRequest;
import com.example.myappserver.dto.LoginResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Tag(name = "用户管理", description = "用户管理相关接口")
@RestController
@RequestMapping("/api/users")
@CrossOrigin
public class UserController {
    
    @Autowired
    private UserService userService;
    
    @Operation(summary = "获取所有用户")
    @GetMapping
    public ResponseEntity<List<User>> getAllUsers() {
        return ResponseEntity.ok(userService.findAll());
    }
    
    @Operation(summary = "根据ID获取用户")
    @GetMapping("/{id}")
    public ResponseEntity<User> getUserById(@PathVariable Integer id) {
        User user = userService.findById(id);
        if (user != null) {
            return ResponseEntity.ok(user);
        }
        return ResponseEntity.notFound().build();
    }
    
    @Operation(summary = "创建用户")
    @PostMapping
    public ResponseEntity<User> createUser(@RequestBody User user) {
        return ResponseEntity.ok(userService.create(user));
    }
    
    @Operation(summary = "更新用户")
    @PutMapping("/{id}")
    public ResponseEntity<User> updateUser(
            @PathVariable Integer id,
            @RequestBody User user) {
        user.setId(id);
        return ResponseEntity.ok(userService.update(user));
    }
    
    @Operation(summary = "删除用户")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Integer id) {
        userService.delete(id);
        return ResponseEntity.ok().build();
    }
    
    @Operation(summary = "用户登录")
    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest loginRequest) {
        return ResponseEntity.ok(userService.login(loginRequest));
    }
    
    @Operation(summary = "更新用户头像")
    @PostMapping("/{id}/avatar")
    public ResponseEntity<User> updateAvatar(
            @PathVariable Integer id,
            @RequestParam("file") MultipartFile file) {
        return ResponseEntity.ok(userService.updateAvatar(id, file));
    }
} 
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

import java.util.List;

@Tag(name = "用户管理", description = "用户管理相关接口")
@RestController
@RequestMapping("/api/users")
@CrossOrigin
public class UserController {
    
    @Autowired
    private UserService userService;
    
    @Operation(summary = "获取所有用户")
    @ApiResponse(responseCode = "200", description = "成功获取用户列表")
    @GetMapping
    public ResponseEntity<List<User>> getAllUsers() {
        return ResponseEntity.ok(userService.findAll());
    }
    
    @Operation(summary = "根据ID获取用户")
    @ApiResponse(responseCode = "200", description = "成功获取用户信息")
    @ApiResponse(responseCode = "404", description = "用户不存在")
    @GetMapping("/{id}")
    public ResponseEntity<User> getUserById(
            @Parameter(description = "用户ID") @PathVariable Integer id) {
        User user = userService.findById(id);
        if (user != null) {
            return ResponseEntity.ok(user);
        }
        return ResponseEntity.notFound().build();
    }
    
    @Operation(summary = "创建新用户")
    @ApiResponse(responseCode = "200", description = "用户创建成功")
    @PostMapping
    public ResponseEntity<User> createUser(
            @Parameter(description = "用户信息") @RequestBody User user) {
        return ResponseEntity.ok(userService.create(user));
    }
    
    @Operation(summary = "更新用户信息")
    @ApiResponse(responseCode = "200", description = "用户信息更新成功")
    @PutMapping("/{id}")
    public ResponseEntity<User> updateUser(
            @Parameter(description = "用户ID") @PathVariable Integer id,
            @Parameter(description = "用户信息") @RequestBody User user) {
        user.setId(id);
        return ResponseEntity.ok(userService.update(user));
    }
    
    @Operation(summary = "删除用户")
    @ApiResponse(responseCode = "200", description = "用户删除成功")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(
            @Parameter(description = "用户ID") @PathVariable Integer id) {
        userService.delete(id);
        return ResponseEntity.ok().build();
    }
    
    @Operation(summary = "用户登录")
    @ApiResponse(responseCode = "200", description = "登录成功")
    @ApiResponse(responseCode = "400", description = "登录失败")
    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(
            @Parameter(description = "登录信息") @RequestBody LoginRequest loginRequest) {
        return ResponseEntity.ok(userService.login(loginRequest));
    }
} 
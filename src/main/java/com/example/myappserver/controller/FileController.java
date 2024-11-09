package com.example.myappserver.controller;

import com.example.myappserver.service.FileService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

@Tag(name = "文件管理", description = "文件上传下载相关接口")
@RestController
@RequestMapping("/api/files")
@CrossOrigin
public class FileController {

    @Autowired
    private FileService fileService;
    
    @Operation(summary = "上传文件")
    @PostMapping("/upload")
    public ResponseEntity<Map<String, String>> uploadFile(
            @RequestParam("file") MultipartFile file,
            @RequestParam(value = "directory", defaultValue = "images") String directory) {
        return ResponseEntity.ok(fileService.uploadFile(file, directory));
    }
    
    @Operation(summary = "删除文件")
    @DeleteMapping("/{objectKey}")
    public ResponseEntity<Void> deleteFile(@PathVariable String objectKey) {
        fileService.deleteFile(objectKey);
        return ResponseEntity.ok().build();
    }
    
    @Operation(summary = "获取文件访问链接")
    @GetMapping("/{objectKey}/url")
    public ResponseEntity<String> getFileUrl(@PathVariable String objectKey) {
        return ResponseEntity.ok(fileService.getFileUrl(objectKey));
    }
} 
package com.example.myappserver.service.impl;

import com.example.myappserver.service.FileService;
import com.example.myappserver.exception.BusinessException;
import com.obs.services.ObsClient;
import com.obs.services.model.PutObjectRequest;
import com.obs.services.model.PutObjectResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Service
public class FileServiceImpl implements FileService {

    @Autowired
    private ObsClient obsClient;
    
    @Value("${huaweicloud.obs.bucketName}")
    private String bucketName;
    
    @Value("${huaweicloud.obs.endpoint}")
    private String endpoint;

    @Override
    public Map<String, String> uploadFile(MultipartFile file, String directory) {
        try {
            // 生成唯一的文件名
            String originalFilename = file.getOriginalFilename();
            String extension = originalFilename.substring(originalFilename.lastIndexOf("."));
            String objectKey = directory + "/" + UUID.randomUUID().toString() + extension;
            
            // 创建上传请求
            PutObjectRequest request = new PutObjectRequest();
            request.setBucketName(bucketName);
            request.setObjectKey(objectKey);
            request.setInput(file.getInputStream());
            
            // 上传文件
            PutObjectResult result = obsClient.putObject(request);
            
            // 构建永久访问链接
            String url = String.format("https://%s.%s/%s", bucketName, endpoint.substring(8), objectKey);
            
            // 返回结果
            Map<String, String> response = new HashMap<>();
            response.put("objectKey", objectKey);
            response.put("url", url);
            return response;
            
        } catch (IOException e) {
            throw new BusinessException("文件上传失败: " + e.getMessage());
        }
    }
    
    @Override
    public void deleteFile(String objectKey) {
        obsClient.deleteObject(bucketName, objectKey);
    }
    
    @Override
    public String getFileUrl(String objectKey) {
        // 返回永久访问链接
        return String.format("https://%s.%s/%s", bucketName, endpoint.substring(8), objectKey);
    }
} 
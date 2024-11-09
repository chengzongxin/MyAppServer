package com.example.myappserver.service;

import org.springframework.web.multipart.MultipartFile;
import java.util.Map;

public interface FileService {
    Map<String, String> uploadFile(MultipartFile file, String directory);
    void deleteFile(String objectKey);
    String getFileUrl(String objectKey);
} 
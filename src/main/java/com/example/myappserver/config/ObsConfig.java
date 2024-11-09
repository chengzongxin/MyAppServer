package com.example.myappserver.config;

import com.obs.services.ObsClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ObsConfig {
    
    @Value("${huaweicloud.obs.ak}")
    private String accessKey;
    
    @Value("${huaweicloud.obs.sk}")
    private String secretKey;
    
    @Value("${huaweicloud.obs.endpoint}")
    private String endPoint;
    
    @Bean
    public ObsClient obsClient() {
        return new ObsClient(accessKey, secretKey, endPoint);
    }
} 
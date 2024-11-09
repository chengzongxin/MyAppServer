package com.example.myappserver.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.Contact;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {
    
    @Bean
    public OpenAPI openAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("用户管理系统 API 文档")
                        .description("这是一个用户管理系统的 API 文档")
                        .version("v1.0.0")
                        .contact(new Contact()
                                .name("开发者")
                                .email("developer@example.com")));
    }
} 
package com.example.myappserver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.mybatis.spring.annotation.MapperScan;

@SpringBootApplication
@MapperScan("com.example.myappserver.mapper")
public class MyAppServerApplication {

	public static void main(String[] args) {
		SpringApplication.run(MyAppServerApplication.class, args);
	}

}

package com.example.backend_notion_api;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@MapperScan("com.example.backend_notion_api.mapper")
@SpringBootApplication
public class BackendNotionApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(BackendNotionApiApplication.class, args);
	}

}

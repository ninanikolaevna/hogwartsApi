package com.example.pro.sky.hogwartsApi.service;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@OpenAPIDefinition
public class SchoolHogwartsApplication {

    public static void main(String[] args) {
        SpringApplication.run(SchoolHogwartsApplication.class, args);
    }

}

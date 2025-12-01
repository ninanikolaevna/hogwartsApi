package com.example.pro.sky.hogwartsApi.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/info")
@Tag(name = "Application Info", description = "API for getting application information")
public class InfoController {

    @Value("${server.port}")
    private int serverPort;

    @GetMapping("/port")
    @Operation(summary = "Get server port", description = "Returns the port on which the application is running")
    public int getPort() {
        return serverPort;
    }
}
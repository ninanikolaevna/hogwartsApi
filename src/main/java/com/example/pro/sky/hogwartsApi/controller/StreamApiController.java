package com.example.pro.sky.hogwartsApi.controller;

import com.example.pro.sky.hogwartsApi.service.StreamApiService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/stream")
@Tag(name = "Stream API Operations", description = "APIs demonstrating Stream API optimizations")
public class StreamApiController {

    private final StreamApiService streamApiService;

    public StreamApiController(StreamApiService streamApiService) {
        this.streamApiService = streamApiService;
    }

    @GetMapping("/sum/slow")
    @Operation(summary = "Calculate sum slow (1 to 1,000,000)",
            description = "Calculates sum from 1 to 1,000,000 using slow iterative method")
    public long calculateSumSlow() {
        return streamApiService.calculateSumSlow();
    }

    @GetMapping("/sum/fast")
    @Operation(summary = "Calculate sum fast (1 to 1,000,000)",
            description = "Calculates sum from 1 to 1,000,000 using optimized method")
    public long calculateSumFast() {
        return streamApiService.calculateSumFast();
    }

    @GetMapping("/sum/fast-stream")
    @Operation(summary = "Calculate sum fast with parallel stream",
            description = "Calculates sum from 1 to 1,000,000 using parallel LongStream")
    public long calculateSumFastStream() {
        return streamApiService.calculateSumFastStream();
    }

    @GetMapping("/performance/comparison")
    @Operation(summary = "Compare performance of sum calculation methods",
            description = "Compares execution time of slow vs fast sum calculation methods")
    public StreamApiService.PerformanceComparison comparePerformance() {
        return streamApiService.comparePerformance();
    }
}
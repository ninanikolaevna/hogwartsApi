package com.example.pro.sky.hogwartsApi.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.stream.LongStream;

@Service
public class StreamApiService {

    private static final Logger logger = LoggerFactory.getLogger(StreamApiService.class);

    // Константы для оптимизации
    private static final long LIMIT = 1_000_000L;
    private static final String SLOW_METHOD_NAME = "calculateSumSlow";
    private static final String FAST_METHOD_NAME = "calculateSumFast";

    /**
     * Медленный способ вычисления суммы (из задания)
     */
    public long calculateSumSlow() {
        logger.info("Was invoked method: {}", SLOW_METHOD_NAME);
        long startTime = System.currentTimeMillis();

        long sum = java.util.stream.Stream.iterate(1L, a -> a + 1)
                .limit(LIMIT)
                .reduce(0L, Long::sum);

        long endTime = System.currentTimeMillis();
        long duration = endTime - startTime;

        logger.info("{} completed in {} ms. Result: {}", SLOW_METHOD_NAME, duration, sum);
        return sum;
    }

    /**
     * Быстрый способ вычисления суммы через формулу Гаусса
     */
    public long calculateSumFast() {
        logger.info("Was invoked method: {}", FAST_METHOD_NAME);
        long startTime = System.currentTimeMillis();

        // Формула суммы арифметической прогрессии: n * (n + 1) / 2
        long sum = LIMIT * (LIMIT + 1) / 2;

        long endTime = System.currentTimeMillis();
        long duration = endTime - startTime;

        logger.info("{} completed in {} ms. Result: {}", FAST_METHOD_NAME, duration, sum);
        return sum;
    }

    /**
     * Альтернативный быстрый способ через LongStream
     */
    public long calculateSumFastStream() {
        logger.info("Was invoked method: calculateSumFastStream");
        long startTime = System.currentTimeMillis();

        long sum = LongStream.rangeClosed(1, LIMIT)
                .parallel()  // Параллельное вычисление для больших диапазонов
                .sum();

        long endTime = System.currentTimeMillis();
        long duration = endTime - startTime;

        logger.info("calculateSumFastStream completed in {} ms. Result: {}", duration, sum);
        return sum;
    }

    /**
     * Метод для сравнения производительности
     */
    public PerformanceComparison comparePerformance() {
        logger.info("Was invoked method for compare performance");

        long slowStart = System.currentTimeMillis();
        long slowResult = calculateSumSlow();
        long slowEnd = System.currentTimeMillis();
        long slowTime = slowEnd - slowStart;

        long fastStart = System.currentTimeMillis();
        long fastResult = calculateSumFast();
        long fastEnd = System.currentTimeMillis();
        long fastTime = fastEnd - fastStart;

        PerformanceComparison comparison = new PerformanceComparison(
                slowResult, fastResult, slowTime, fastTime
        );

        logger.info("Performance comparison: slow={}ms, fast={}ms, improvement={}x",
                slowTime, fastTime, (double) slowTime / fastTime);

        return comparison;
    }

    /**
     * Вспомогательный класс для сравнения производительности
     */
    public static class PerformanceComparison {
        private final long slowResult;
        private final long fastResult;
        private final long slowTimeMs;
        private final long fastTimeMs;
        private final double improvement;

        public PerformanceComparison(long slowResult, long fastResult,
                                     long slowTimeMs, long fastTimeMs) {
            this.slowResult = slowResult;
            this.fastResult = fastResult;
            this.slowTimeMs = slowTimeMs;
            this.fastTimeMs = fastTimeMs;
            this.improvement = slowTimeMs > 0 ? (double) slowTimeMs / fastTimeMs : 0;
        }

        // Геттеры
        public long getSlowResult() {
            return slowResult;
        }

        public long getFastResult() {
            return fastResult;
        }

        public long getSlowTimeMs() {
            return slowTimeMs;
        }

        public long getFastTimeMs() {
            return fastTimeMs;
        }

        public double getImprovement() {
            return improvement;
        }
    }
}
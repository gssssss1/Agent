package com.framework.engine.api;

import java.time.Duration;

/**
 * Strongly typed configuration class for the engine.
 * Defines parameters such as thread pool size, queue capacity, and timeout strategies.
 */
public record EngineConfiguration(
        int virtualThreadPoolSize,
        int queueCapacity,
        Duration shutdownTimeout
) {
    
    // Default configuration
    public static EngineConfiguration defaultConfig() {
        return new EngineConfiguration(
                Runtime.getRuntime().availableProcessors() * 2,
                1024,
                Duration.ofSeconds(30)
        );
    }
}

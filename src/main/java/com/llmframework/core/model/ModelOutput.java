package com.llmframework.core.model;

import com.llmframework.core.usage.Usage;
import java.time.Duration;

/**
 * Base interface for all model outputs
 */
public sealed interface ModelOutput
        permits com.llmframework.chat.ChatOutput,
                com.llmframework.embedding.EmbeddingOutput,
                com.llmframework.image.ImageOutput {

    /**
     * Get request ID
     */
    String requestId();

    /**
     * Get usage statistics
     */
    Usage usage();

    /**
     * Get model version
     */
    String modelVersion();

    /**
     * Get duration
     */
    Duration duration();

    /**
     * Check if from cache
     */
    boolean fromCache();
}

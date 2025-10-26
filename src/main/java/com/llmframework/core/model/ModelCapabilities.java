package com.llmframework.core.model;

import java.util.List;
import java.util.Set;

/**
 * Model capabilities interface
 */
public interface ModelCapabilities {

    /**
     * Supports streaming output
     */
    boolean supportsStreaming();

    /**
     * Supports function calling
     */
    boolean supportsFunctionCalling();

    /**
     * Supports vision (image input)
     */
    boolean supportsVision();

    /**
     * Supports audio
     */
    boolean supportsAudio();

    /**
     * Supports JSON mode
     */
    boolean supportsJsonMode();

    /**
     * Supports JSON Schema
     */
    boolean supportsJsonSchema();

    /**
     * Maximum context length
     */
    int getMaxContextTokens();

    /**
     * Maximum output length
     */
    int getMaxOutputTokens();

    /**
     * Supported languages
     */
    List<String> getSupportedLanguages();

    /**
     * Supported features
     */
    Set<String> getSupportedFeatures();

    /**
     * Check if feature is supported
     */
    default boolean supports(String feature) {
        return getSupportedFeatures().contains(feature);
    }
}

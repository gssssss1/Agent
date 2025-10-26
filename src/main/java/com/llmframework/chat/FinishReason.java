package com.llmframework.chat;

/**
 * Finish reason
 */
public enum FinishReason {
    STOP,           // Normal completion
    LENGTH,         // Reached max length
    TOOL_CALLS,     // Need to call tools
    CONTENT_FILTER, // Content filtered
    ERROR           // Error occurred
}

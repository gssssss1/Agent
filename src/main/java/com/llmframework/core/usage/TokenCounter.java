package com.llmframework.core.usage;

/**
 * Simple token counter utility
 */
public class TokenCounter {

    /**
     * Estimate token count for text
     * Simple heuristic: ~4 characters per token
     */
    public static int estimate(String text) {
        if (text == null || text.isEmpty()) {
            return 0;
        }
        // Simple estimation: 1 token ≈ 4 characters
        return (int) Math.ceil(text.length() / 4.0);
    }
}

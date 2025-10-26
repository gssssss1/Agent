package com.llmframework.core.usage;

/**
 * Usage statistics
 */
public record Usage(
        int promptTokens,
        int completionTokens,
        int totalTokens
) {
    public static Usage of(int prompt, int completion) {
        return new Usage(prompt, completion, prompt + completion);
    }

    public static Usage zero() {
        return new Usage(0, 0, 0);
    }
}

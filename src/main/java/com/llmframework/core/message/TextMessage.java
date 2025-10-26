package com.llmframework.core.message;

import com.llmframework.core.usage.TokenCounter;

/**
 * Text message
 */
public record TextMessage(
        MessageRole role,
        String content,
        String name
) implements Message {

    @Override
    public int estimateTokens() {
        return TokenCounter.estimate(content);
    }
}

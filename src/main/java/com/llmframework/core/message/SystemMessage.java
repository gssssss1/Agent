package com.llmframework.core.message;

import com.llmframework.core.usage.TokenCounter;

/**
 * System message
 */
public record SystemMessage(String content) implements Message {

    @Override
    public MessageRole role() {
        return MessageRole.SYSTEM;
    }

    @Override
    public String name() {
        return null;
    }

    @Override
    public int estimateTokens() {
        return TokenCounter.estimate(content);
    }
}

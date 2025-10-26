package com.llmframework.core.message;

import com.llmframework.core.usage.TokenCounter;

/**
 * Tool message
 */
public record ToolMessage(
        String toolCallId,
        String content
) implements Message {

    @Override
    public MessageRole role() {
        return MessageRole.TOOL;
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

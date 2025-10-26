package com.llmframework.core.message;

import com.llmframework.core.usage.TokenCounter;
import com.llmframework.tool.ToolCall;
import java.util.List;

/**
 * Assistant message
 */
public record AssistantMessage(
        String content,
        List<ToolCall> toolCalls
) implements Message {

    @Override
    public MessageRole role() {
        return MessageRole.ASSISTANT;
    }

    @Override
    public String name() {
        return null;
    }

    @Override
    public int estimateTokens() {
        return TokenCounter.estimate(content);
    }

    public boolean hasToolCalls() {
        return toolCalls != null && !toolCalls.isEmpty();
    }
}

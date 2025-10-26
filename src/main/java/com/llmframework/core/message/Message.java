package com.llmframework.core.message;

import com.llmframework.tool.ToolCall;
import java.util.List;

/**
 * Message interface - supports multimodal
 */
public sealed interface Message
        permits TextMessage, MultiModalMessage, ToolMessage, SystemMessage, AssistantMessage {

    MessageRole role();
    String content();
    String name();
    int estimateTokens();

    // Convenience factory methods
    static Message system(String content) {
        return new SystemMessage(content);
    }

    static Message user(String content) {
        return new TextMessage(MessageRole.USER, content, null);
    }

    static Message assistant(String content) {
        return new AssistantMessage(content, null);
    }

    static Message assistant(String content, List<ToolCall> toolCalls) {
        return new AssistantMessage(content, toolCalls);
    }

    static Message tool(String toolCallId, String result) {
        return new ToolMessage(toolCallId, result);
    }

    static MultiModalMessage.Builder multiModal() {
        return new MultiModalMessage.Builder();
    }
}

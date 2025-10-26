package com.llmframework.chat;

import com.llmframework.core.model.Model;
import com.llmframework.prompt.Message;
import java.util.List;

public interface ChatModel extends Model<ChatModelRequest, ChatModelResponse> {
    /**
     * 对话调用（便捷方法）
     */
    default ChatModelResponse chat(List<Message> messages) {
        return call(ChatModelRequest.builder()
            .messages(messages)
            .build());
    }
}

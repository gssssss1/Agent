package com.llmframework.memory;

import com.llmframework.prompt.Message;
import java.util.List;

public interface ConversationMemory {
    /**
     * 添加消息
     */
    void add(String conversationId, Message message);
    
    /**
     * 获取对话历史
     */
    List<Message> get(String conversationId);
    
    /**
     * 获取对话历史（带窗口限制）
     */
    List<Message> get(String conversationId, int maxTokens);
    
    /**
     * 清除对话历史
     */
    void clear(String conversationId);
}

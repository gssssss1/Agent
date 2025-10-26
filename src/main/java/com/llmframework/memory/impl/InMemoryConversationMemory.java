package com.llmframework.memory.impl;

import com.llmframework.memory.ConversationMemory;
import com.llmframework.prompt.Message;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class InMemoryConversationMemory implements ConversationMemory {
    private final Map<String, List<Message>> conversations = new ConcurrentHashMap<>();
    
    @Override
    public void add(String conversationId, Message message) {
        conversations.computeIfAbsent(conversationId, k -> new ArrayList<>()).add(message);
    }
    
    @Override
    public List<Message> get(String conversationId) {
        return new ArrayList<>(conversations.getOrDefault(conversationId, new ArrayList<>()));
    }
    
    @Override
    public List<Message> get(String conversationId, int maxTokens) {
        List<Message> messages = conversations.getOrDefault(conversationId, new ArrayList<>());
        
        int estimatedTokens = 0;
        List<Message> result = new ArrayList<>();
        
        for (int i = messages.size() - 1; i >= 0; i--) {
            Message message = messages.get(i);
            int messageTokens = estimateTokens(message);
            
            if (estimatedTokens + messageTokens > maxTokens && !result.isEmpty()) {
                break;
            }
            
            result.add(0, message);
            estimatedTokens += messageTokens;
        }
        
        return result;
    }
    
    @Override
    public void clear(String conversationId) {
        conversations.remove(conversationId);
    }
    
    private int estimateTokens(Message message) {
        int tokens = 0;
        if (message.getContents() != null) {
            for (var content : message.getContents()) {
                if (content.getValue() instanceof String text) {
                    tokens += text.length() / 4;
                }
            }
        }
        return tokens;
    }
}

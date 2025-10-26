package com.llmframework.prompt;

import java.util.List;

public interface Message {
    /**
     * 获取消息角色
     */
    MessageRole getRole();
    
    /**
     * 获取消息内容
     */
    List<Content> getContents();
    
    /**
     * 获取消息名称（可选）
     */
    String getName();
}

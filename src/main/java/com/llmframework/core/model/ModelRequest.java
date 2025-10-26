package com.llmframework.core.model;

public interface ModelRequest {
    /**
     * 获取指令内容
     */
    String getInstructions();
    
    /**
     * 获取模型选项
     */
    ModelOptions getOptions();
    
    /**
     * 获取请求 ID（用于追踪）
     */
    String getRequestId();
}

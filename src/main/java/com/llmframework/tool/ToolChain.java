package com.llmframework.tool;

import com.llmframework.chat.ChatModelRequest;

public interface ToolChain {
    /**
     * 执行工具链
     */
    ToolChainResult execute(ChatModelRequest request);
    
    /**
     * 设置最大迭代次数
     */
    void setMaxIterations(int maxIterations);
}

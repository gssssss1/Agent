package com.llmframework.tool;

import java.util.Map;

public interface ToolCall {
    /**
     * 获取工具名称
     */
    String getName();
    
    /**
     * 获取工具参数
     */
    Map<String, Object> getArguments();
    
    /**
     * 获取调用 ID
     */
    String getId();
}

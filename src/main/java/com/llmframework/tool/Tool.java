package com.llmframework.tool;

import java.util.Map;

public interface Tool {
    /**
     * 执行工具
     */
    Object execute(Map<String, Object> arguments);
    
    /**
     * 获取工具定义
     */
    FunctionDefinition getDefinition();
}

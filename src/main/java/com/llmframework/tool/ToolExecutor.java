package com.llmframework.tool;

import java.util.List;

public interface ToolExecutor {
    /**
     * 执行函数调用
     */
    ToolExecutionResult execute(FunctionCall functionCall) throws ToolExecutionException;
    
    /**
     * 注册工具
     */
    void registerTool(String name, Tool tool);
    
    /**
     * 获取所有工具定义
     */
    List<FunctionDefinition> getToolDefinitions();
}

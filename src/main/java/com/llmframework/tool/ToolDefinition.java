package com.llmframework.tool;

import java.util.Map;

public interface ToolDefinition {
    /**
     * 获取工具名称
     */
    String getName();
    
    /**
     * 获取工具描述
     */
    String getDescription();
    
    /**
     * 获取参数 Schema（JSON Schema）
     */
    Map<String, Object> getParametersSchema();
}

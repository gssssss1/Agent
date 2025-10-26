package com.llmframework.tool;

import java.util.Map;

public interface FunctionDefinition {
    /**
     * 获取函数名称
     */
    String getName();
    
    /**
     * 获取函数描述
     */
    String getDescription();
    
    /**
     * 获取参数 Schema（JSON Schema）
     */
    Map<String, Object> getParametersSchema();
}

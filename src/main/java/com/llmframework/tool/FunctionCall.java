package com.llmframework.tool;

import java.util.Map;

public interface FunctionCall {
    /**
     * 获取函数名称
     */
    String getName();
    
    /**
     * 获取函数参数
     */
    Map<String, Object> getArguments();
    
    /**
     * 获取调用 ID
     */
    String getId();
}

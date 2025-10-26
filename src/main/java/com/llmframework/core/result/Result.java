package com.llmframework.core.result;

import java.util.Map;

public interface Result {
    /**
     * 获取输出内容
     */
    Object getOutput();
    
    /**
     * 获取结果类型
     */
    ResultType getType();
    
    /**
     * 获取生成信息（如 finish_reason）
     */
    Map<String, Object> getGenerationInfo();
}

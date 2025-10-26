package com.llmframework.prompt;

import java.util.Map;

public interface PromptTemplate {
    /**
     * 创建 Prompt 实例
     */
    Prompt create(Map<String, Object> variables);
    
    /**
     * 获取模板内容
     */
    String getTemplate();
    
    /**
     * 验证变量
     */
    boolean validate(Map<String, Object> variables);
}

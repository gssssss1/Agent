package com.llmframework.prompt;

import java.util.Map;
import java.util.Set;

public interface Prompt {
    /**
     * 渲染提示词
     */
    String render(Map<String, Object> variables);
    
    /**
     * 获取提示词内容
     */
    String getContent();
    
    /**
     * 获取变量列表
     */
    Set<String> getVariables();
}

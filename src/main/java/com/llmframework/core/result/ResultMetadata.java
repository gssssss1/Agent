package com.llmframework.core.result;

import java.util.Map;

public interface ResultMetadata {
    /**
     * Token 使用量
     */
    TokenUsage getTokenUsage();
    
    /**
     * 调用耗时（毫秒）
     */
    long getDuration();
    
    /**
     * 模型版本
     */
    String getModelVersion();
    
    /**
     * 其他元数据
     */
    Map<String, Object> getAdditionalMetadata();
}

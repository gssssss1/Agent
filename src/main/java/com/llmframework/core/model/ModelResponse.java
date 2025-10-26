package com.llmframework.core.model;

import com.llmframework.core.result.Result;
import com.llmframework.core.result.ResultMetadata;

public interface ModelResponse<T extends Result> {
    /**
     * 获取结果内容
     */
    T getResult();
    
    /**
     * 获取元数据
     */
    ResultMetadata getMetadata();
    
    /**
     * 是否为流式响应的最后一块
     */
    boolean isLast();
}

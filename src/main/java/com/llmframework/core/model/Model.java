package com.llmframework.core.model;

import com.llmframework.core.exception.ModelException;
import reactor.core.publisher.Flux;

public interface Model<REQ extends ModelRequest, RESP extends ModelResponse> {
    /**
     * 同步调用模型
     */
    RESP call(REQ request) throws ModelException;
    
    /**
     * 流式调用模型（使用 Reactor Flux）
     */
    Flux<RESP> stream(REQ request) throws ModelException;
    
    /**
     * 获取模型元信息
     */
    ModelMetadata getMetadata();
}

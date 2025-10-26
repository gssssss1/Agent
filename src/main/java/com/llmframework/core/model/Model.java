package com.llmframework.core.model;

import com.llmframework.core.exception.ModelException;
import java.util.stream.Stream;

public interface Model<REQ extends ModelRequest, RESP extends ModelResponse> {
    /**
     * 同步调用模型
     */
    RESP call(REQ request) throws ModelException;
    
    /**
     * 流式调用模型
     */
    Stream<RESP> stream(REQ request) throws ModelException;
    
    /**
     * 获取模型元信息
     */
    ModelMetadata getMetadata();
}

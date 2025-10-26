package com.llmframework.client;

import com.llmframework.core.exception.ModelException;
import com.llmframework.core.model.ModelRequest;
import com.llmframework.core.model.ModelResponse;
import java.util.stream.Stream;

public interface ModelClient {
    /**
     * 执行请求
     */
    <REQ extends ModelRequest, RESP extends ModelResponse> 
        RESP execute(REQ request, Class<RESP> responseClass) throws ModelException;
    
    /**
     * 执行流式请求
     */
    <REQ extends ModelRequest, RESP extends ModelResponse> 
        Stream<RESP> executeStream(REQ request, Class<RESP> responseClass) throws ModelException;
}

package com.llmframework.observability;

import com.llmframework.core.model.ModelRequest;
import com.llmframework.core.model.ModelResponse;

public interface ModelObserver {
    /**
     * 调用开始
     */
    void onCallStart(ModelRequest request);
    
    /**
     * 调用成功
     */
    void onCallSuccess(ModelRequest request, ModelResponse response);
    
    /**
     * 调用失败
     */
    void onCallError(ModelRequest request, Throwable error);
    
    /**
     * 流式调用开始
     */
    void onStreamStart(ModelRequest request);
    
    /**
     * 流式调用接收数据块
     */
    void onStreamChunk(ModelRequest request, ModelResponse chunk);
    
    /**
     * 流式调用完成
     */
    void onStreamComplete(ModelRequest request);
}

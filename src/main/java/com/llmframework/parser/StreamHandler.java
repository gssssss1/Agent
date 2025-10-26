package com.llmframework.parser;

public interface StreamHandler<T> {
    /**
     * 流开始
     */
    void onStart();
    
    /**
     * 接收到数据块
     */
    void onChunk(T chunk);
    
    /**
     * 流完成
     */
    void onComplete();
    
    /**
     * 流错误
     */
    void onError(Throwable error);
}

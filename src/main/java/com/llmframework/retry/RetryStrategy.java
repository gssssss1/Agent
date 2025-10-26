package com.llmframework.retry;

import com.llmframework.core.exception.ModelException;

public interface RetryStrategy {
    /**
     * 是否应该重试
     */
    boolean shouldRetry(ModelException exception, int attemptNumber);
    
    /**
     * 计算下次重试的延迟时间（毫秒）
     */
    long getRetryDelay(int attemptNumber);
    
    /**
     * 获取最大重试次数
     */
    int getMaxAttempts();
}

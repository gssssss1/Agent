package com.llmframework.retry.impl;

import com.llmframework.core.exception.ModelException;
import com.llmframework.retry.RetryStrategy;

public class NoRetryStrategy implements RetryStrategy {
    
    @Override
    public boolean shouldRetry(ModelException exception, int attemptNumber) {
        return false;
    }
    
    @Override
    public long getRetryDelay(int attemptNumber) {
        return 0;
    }
    
    @Override
    public int getMaxAttempts() {
        return 1;
    }
}

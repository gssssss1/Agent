package com.llmframework.retry.impl;

import com.llmframework.core.exception.ModelException;
import com.llmframework.retry.RetryStrategy;

public class FixedDelayRetryStrategy implements RetryStrategy {
    private final int maxAttempts;
    private final long delay;
    
    public FixedDelayRetryStrategy() {
        this(3, 1000);
    }
    
    public FixedDelayRetryStrategy(int maxAttempts, long delay) {
        this.maxAttempts = maxAttempts;
        this.delay = delay;
    }
    
    @Override
    public boolean shouldRetry(ModelException exception, int attemptNumber) {
        return exception.isRetryable() && attemptNumber < maxAttempts;
    }
    
    @Override
    public long getRetryDelay(int attemptNumber) {
        return delay;
    }
    
    @Override
    public int getMaxAttempts() {
        return maxAttempts;
    }
}

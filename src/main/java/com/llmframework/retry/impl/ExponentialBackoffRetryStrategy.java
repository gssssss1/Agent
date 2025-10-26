package com.llmframework.retry.impl;

import com.llmframework.core.exception.ModelException;
import com.llmframework.retry.RetryStrategy;

public class ExponentialBackoffRetryStrategy implements RetryStrategy {
    private final int maxAttempts;
    private final long initialDelay;
    private final double multiplier;
    private final long maxDelay;
    
    public ExponentialBackoffRetryStrategy() {
        this(3, 1000, 2.0, 60000);
    }
    
    public ExponentialBackoffRetryStrategy(int maxAttempts, long initialDelay, double multiplier, long maxDelay) {
        this.maxAttempts = maxAttempts;
        this.initialDelay = initialDelay;
        this.multiplier = multiplier;
        this.maxDelay = maxDelay;
    }
    
    @Override
    public boolean shouldRetry(ModelException exception, int attemptNumber) {
        return exception.isRetryable() && attemptNumber < maxAttempts;
    }
    
    @Override
    public long getRetryDelay(int attemptNumber) {
        long delay = (long) (initialDelay * Math.pow(multiplier, attemptNumber - 1));
        return Math.min(delay, maxDelay);
    }
    
    @Override
    public int getMaxAttempts() {
        return maxAttempts;
    }
}

package com.llmframework.core.exception;

public class RateLimitException extends ModelException {
    private final long retryAfterSeconds;
    
    public RateLimitException(String message, String requestId, String modelName, long retryAfterSeconds) {
        super(message, requestId, modelName);
        this.retryAfterSeconds = retryAfterSeconds;
    }
    
    public RateLimitException(String message, Throwable cause, String requestId, String modelName, long retryAfterSeconds) {
        super(message, cause, requestId, modelName);
        this.retryAfterSeconds = retryAfterSeconds;
    }
    
    @Override
    public boolean isRetryable() {
        return true;
    }
    
    public long getRetryAfterSeconds() {
        return retryAfterSeconds;
    }
}

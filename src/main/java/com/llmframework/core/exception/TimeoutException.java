package com.llmframework.core.exception;

public class TimeoutException extends ModelException {
    
    public TimeoutException(String message, String requestId, String modelName) {
        super(message, requestId, modelName);
    }
    
    public TimeoutException(String message, Throwable cause, String requestId, String modelName) {
        super(message, cause, requestId, modelName);
    }
    
    @Override
    public boolean isRetryable() {
        return true;
    }
}

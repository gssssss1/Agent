package com.llmframework.core.exception;

public class AuthException extends ModelException {
    
    public AuthException(String message, String requestId, String modelName) {
        super(message, requestId, modelName);
    }
    
    public AuthException(String message, Throwable cause, String requestId, String modelName) {
        super(message, cause, requestId, modelName);
    }
    
    @Override
    public boolean isRetryable() {
        return false;
    }
}

package com.llmframework.core.exception;

public class InvalidRequestException extends ModelException {
    
    public InvalidRequestException(String message, String requestId, String modelName) {
        super(message, requestId, modelName);
    }
    
    public InvalidRequestException(String message, Throwable cause, String requestId, String modelName) {
        super(message, cause, requestId, modelName);
    }
    
    @Override
    public boolean isRetryable() {
        return false;
    }
}

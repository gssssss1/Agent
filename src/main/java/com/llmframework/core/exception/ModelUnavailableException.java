package com.llmframework.core.exception;

public class ModelUnavailableException extends ModelException {
    
    public ModelUnavailableException(String message, String requestId, String modelName) {
        super(message, requestId, modelName);
    }
    
    public ModelUnavailableException(String message, Throwable cause, String requestId, String modelName) {
        super(message, cause, requestId, modelName);
    }
    
    @Override
    public boolean isRetryable() {
        return true;
    }
}

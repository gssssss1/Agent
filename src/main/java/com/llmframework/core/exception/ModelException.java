package com.llmframework.core.exception;

public abstract class ModelException extends RuntimeException {
    private final String requestId;
    private final String modelName;
    
    public ModelException(String message, String requestId, String modelName) {
        super(message);
        this.requestId = requestId;
        this.modelName = modelName;
    }
    
    public ModelException(String message, Throwable cause, String requestId, String modelName) {
        super(message, cause);
        this.requestId = requestId;
        this.modelName = modelName;
    }
    
    public abstract boolean isRetryable();
    
    public String getRequestId() {
        return requestId;
    }
    
    public String getModelName() {
        return modelName;
    }
}

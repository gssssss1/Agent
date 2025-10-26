package com.llmframework.tool;

public class ToolExecutionResult {
    private Object result;
    private boolean success;
    private String error;
    
    public ToolExecutionResult() {
    }
    
    public ToolExecutionResult(Object result, boolean success) {
        this.result = result;
        this.success = success;
    }
    
    public ToolExecutionResult(Object result, boolean success, String error) {
        this.result = result;
        this.success = success;
        this.error = error;
    }
    
    public Object getResult() {
        return result;
    }
    
    public void setResult(Object result) {
        this.result = result;
    }
    
    public boolean isSuccess() {
        return success;
    }
    
    public void setSuccess(boolean success) {
        this.success = success;
    }
    
    public String getError() {
        return error;
    }
    
    public void setError(String error) {
        this.error = error;
    }
}

package com.llmframework.tool;

import java.util.List;

public class ToolChainResult {
    private Object finalResult;
    private List<ToolExecutionResult> executionHistory;
    private int iterations;
    
    public ToolChainResult() {
    }
    
    public ToolChainResult(Object finalResult, List<ToolExecutionResult> executionHistory, int iterations) {
        this.finalResult = finalResult;
        this.executionHistory = executionHistory;
        this.iterations = iterations;
    }
    
    public Object getFinalResult() {
        return finalResult;
    }
    
    public void setFinalResult(Object finalResult) {
        this.finalResult = finalResult;
    }
    
    public List<ToolExecutionResult> getExecutionHistory() {
        return executionHistory;
    }
    
    public void setExecutionHistory(List<ToolExecutionResult> executionHistory) {
        this.executionHistory = executionHistory;
    }
    
    public int getIterations() {
        return iterations;
    }
    
    public void setIterations(int iterations) {
        this.iterations = iterations;
    }
}

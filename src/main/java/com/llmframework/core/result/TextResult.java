package com.llmframework.core.result;

import java.util.HashMap;
import java.util.Map;

public class TextResult implements Result {
    private String text;
    private Map<String, Object> generationInfo;
    
    public TextResult() {
        this.generationInfo = new HashMap<>();
    }
    
    public TextResult(String text) {
        this.text = text;
        this.generationInfo = new HashMap<>();
    }
    
    public TextResult(String text, Map<String, Object> generationInfo) {
        this.text = text;
        this.generationInfo = generationInfo != null ? generationInfo : new HashMap<>();
    }
    
    @Override
    public Object getOutput() {
        return text;
    }
    
    @Override
    public ResultType getType() {
        return ResultType.TEXT;
    }
    
    @Override
    public Map<String, Object> getGenerationInfo() {
        return generationInfo;
    }
    
    public String getText() {
        return text;
    }
    
    public void setText(String text) {
        this.text = text;
    }
    
    public void setGenerationInfo(Map<String, Object> generationInfo) {
        this.generationInfo = generationInfo;
    }
}

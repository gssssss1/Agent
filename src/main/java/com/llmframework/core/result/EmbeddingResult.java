package com.llmframework.core.result;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EmbeddingResult implements Result {
    private List<Float> embedding;
    private Map<String, Object> generationInfo;
    
    public EmbeddingResult() {
        this.generationInfo = new HashMap<>();
    }
    
    public EmbeddingResult(List<Float> embedding) {
        this.embedding = embedding;
        this.generationInfo = new HashMap<>();
    }
    
    public EmbeddingResult(List<Float> embedding, Map<String, Object> generationInfo) {
        this.embedding = embedding;
        this.generationInfo = generationInfo != null ? generationInfo : new HashMap<>();
    }
    
    @Override
    public Object getOutput() {
        return embedding;
    }
    
    @Override
    public ResultType getType() {
        return ResultType.EMBEDDING;
    }
    
    @Override
    public Map<String, Object> getGenerationInfo() {
        return generationInfo;
    }
    
    public List<Float> getEmbedding() {
        return embedding;
    }
    
    public void setEmbedding(List<Float> embedding) {
        this.embedding = embedding;
    }
    
    public void setGenerationInfo(Map<String, Object> generationInfo) {
        this.generationInfo = generationInfo;
    }
}

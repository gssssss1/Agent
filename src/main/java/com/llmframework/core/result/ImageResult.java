package com.llmframework.core.result;

import java.util.HashMap;
import java.util.Map;

public class ImageResult implements Result {
    private String imageUrl;
    private String imageBase64;
    private Map<String, Object> generationInfo;
    
    public ImageResult() {
        this.generationInfo = new HashMap<>();
    }
    
    public ImageResult(String imageUrl, String imageBase64) {
        this.imageUrl = imageUrl;
        this.imageBase64 = imageBase64;
        this.generationInfo = new HashMap<>();
    }
    
    public ImageResult(String imageUrl, String imageBase64, Map<String, Object> generationInfo) {
        this.imageUrl = imageUrl;
        this.imageBase64 = imageBase64;
        this.generationInfo = generationInfo != null ? generationInfo : new HashMap<>();
    }
    
    @Override
    public Object getOutput() {
        return imageUrl != null ? imageUrl : imageBase64;
    }
    
    @Override
    public ResultType getType() {
        return ResultType.IMAGE;
    }
    
    @Override
    public Map<String, Object> getGenerationInfo() {
        return generationInfo;
    }
    
    public String getImageUrl() {
        return imageUrl;
    }
    
    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
    
    public String getImageBase64() {
        return imageBase64;
    }
    
    public void setImageBase64(String imageBase64) {
        this.imageBase64 = imageBase64;
    }
    
    public void setGenerationInfo(Map<String, Object> generationInfo) {
        this.generationInfo = generationInfo;
    }
}

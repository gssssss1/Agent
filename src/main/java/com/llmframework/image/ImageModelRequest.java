package com.llmframework.image;

import com.llmframework.core.model.ModelRequest;

public interface ImageModelRequest extends ModelRequest {
    /**
     * 获取图片生成提示词
     */
    String getPrompt();
    
    /**
     * 获取负面提示词
     */
    String getNegativePrompt();
    
    /**
     * 获取图片数量
     */
    Integer getN();
    
    /**
     * 获取图片尺寸
     */
    String getSize();
    
    /**
     * 获取图片质量
     */
    String getQuality();
    
    static Builder builder() {
        return new Builder();
    }
    
    class Builder {
        private String prompt;
        private String negativePrompt;
        private Integer n;
        private String size;
        private String quality;
        
        public Builder prompt(String prompt) {
            this.prompt = prompt;
            return this;
        }
        
        public Builder negativePrompt(String negativePrompt) {
            this.negativePrompt = negativePrompt;
            return this;
        }
        
        public Builder n(Integer n) {
            this.n = n;
            return this;
        }
        
        public Builder size(String size) {
            this.size = size;
            return this;
        }
        
        public Builder quality(String quality) {
            this.quality = quality;
            return this;
        }
        
        public ImageModelRequest build() {
            return new ImageModelRequest() {
                @Override
                public String getPrompt() {
                    return prompt;
                }
                
                @Override
                public String getNegativePrompt() {
                    return negativePrompt;
                }
                
                @Override
                public Integer getN() {
                    return n;
                }
                
                @Override
                public String getSize() {
                    return size;
                }
                
                @Override
                public String getQuality() {
                    return quality;
                }
                
                @Override
                public String getInstructions() {
                    return prompt;
                }
                
                @Override
                public com.llmframework.core.model.ModelOptions getOptions() {
                    return null;
                }
                
                @Override
                public String getRequestId() {
                    return null;
                }
            };
        }
    }
}

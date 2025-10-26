package com.llmframework.embedding;

import com.llmframework.core.model.ModelRequest;
import java.util.List;

public interface EmbeddingModelRequest extends ModelRequest {
    /**
     * 获取待嵌入的文本列表
     */
    List<String> getTexts();
    
    /**
     * 获取嵌入维度（可选）
     */
    Integer getDimensions();
    
    static Builder builder() {
        return new Builder();
    }
    
    class Builder {
        private List<String> texts;
        private Integer dimensions;
        
        public Builder texts(List<String> texts) {
            this.texts = texts;
            return this;
        }
        
        public Builder dimensions(Integer dimensions) {
            this.dimensions = dimensions;
            return this;
        }
        
        public EmbeddingModelRequest build() {
            return new EmbeddingModelRequest() {
                @Override
                public List<String> getTexts() {
                    return texts;
                }
                
                @Override
                public Integer getDimensions() {
                    return dimensions;
                }
                
                @Override
                public String getInstructions() {
                    return null;
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

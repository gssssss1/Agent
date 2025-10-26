package com.llmframework.embedding;

import com.llmframework.core.model.Model;
import java.util.Collections;
import java.util.List;

public interface EmbeddingModel extends Model<EmbeddingModelRequest, EmbeddingModelResponse> {
    /**
     * 嵌入单个文本（便捷方法）
     */
    default List<Float> embed(String text) {
        EmbeddingModelResponse response = call(EmbeddingModelRequest.builder()
            .texts(Collections.singletonList(text))
            .build());
        return response.getEmbeddings().get(0);
    }
}

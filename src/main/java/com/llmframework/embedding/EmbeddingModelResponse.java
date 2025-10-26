package com.llmframework.embedding;

import com.llmframework.core.model.ModelResponse;
import com.llmframework.core.result.EmbeddingResult;
import java.util.List;

public interface EmbeddingModelResponse extends ModelResponse<EmbeddingResult> {
    /**
     * 获取向量列表
     */
    List<List<Float>> getEmbeddings();
}

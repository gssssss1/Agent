package com.llmframework.image;

import com.llmframework.core.model.ModelResponse;
import com.llmframework.core.result.ImageResult;
import java.util.List;

public interface ImageModelResponse extends ModelResponse<ImageResult> {
    /**
     * 获取图片 URL 列表
     */
    List<String> getImageUrls();
    
    /**
     * 获取 Base64 编码的图片列表
     */
    List<String> getImageBase64List();
}

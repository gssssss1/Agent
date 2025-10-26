package com.llmframework.image;

import com.llmframework.core.model.Model;
import java.util.List;

public interface ImageModel extends Model<ImageModelRequest, ImageModelResponse> {
    /**
     * 生成图片（便捷方法）
     */
    default List<String> generate(String promptValue) {
        ImageModelResponse response = call(ImageModelRequest.builder()
            .prompt(promptValue)
            .build());
        return response.getImageUrls();
    }
}

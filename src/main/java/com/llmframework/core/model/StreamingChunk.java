package com.llmframework.core.model;

/**
 * Streaming data chunk
 */
public record StreamingChunk<O extends ModelOutput>(
        O output,
        boolean isLast,
        int chunkIndex
) {

    public static <O extends ModelOutput> StreamingChunk<O> of(O output, boolean isLast, int index) {
        return new StreamingChunk<>(output, isLast, index);
    }
}

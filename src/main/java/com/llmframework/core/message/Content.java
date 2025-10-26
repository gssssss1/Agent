package com.llmframework.core.message;

import com.llmframework.core.usage.TokenCounter;

/**
 * Content types for multi-modal messages
 */
public sealed interface Content
        permits TextContent, ImageContent, AudioContent {

    ContentType type();
    int estimateTokens();
}

enum ContentType {
    TEXT,
    IMAGE,
    AUDIO
}

record TextContent(String text) implements Content {
    @Override
    public ContentType type() {
        return ContentType.TEXT;
    }

    @Override
    public int estimateTokens() {
        return TokenCounter.estimate(text);
    }
}

record ImageContent(
        String source,  // URL or base64
        String mimeType,
        ImageDetail detail
) implements Content {

    public ImageContent(String url, ImageDetail detail) {
        this(url, null, detail);
    }

    @Override
    public ContentType type() {
        return ContentType.IMAGE;
    }

    @Override
    public int estimateTokens() {
        // Image token consumption depends on resolution
        return switch (detail) {
            case LOW -> 85;
            case HIGH -> 765;
            case AUTO -> 425; // average
        };
    }
}

record AudioContent(
        byte[] audioData,
        AudioFormat format
) implements Content {

    @Override
    public ContentType type() {
        return ContentType.AUDIO;
    }

    @Override
    public int estimateTokens() {
        // Audio token estimation (simplified)
        return audioData.length / 1000;
    }
}

enum ImageDetail {
    LOW,
    HIGH,
    AUTO
}

enum AudioFormat {
    MP3,
    WAV,
    FLAC,
    OGG
}

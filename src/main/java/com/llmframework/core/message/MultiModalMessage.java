package com.llmframework.core.message;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Multi-modal message
 */
public record MultiModalMessage(
        MessageRole role,
        List<Content> contents,
        String name
) implements Message {

    @Override
    public String content() {
        return contents.stream()
                .filter(c -> c instanceof TextContent)
                .map(c -> ((TextContent) c).text())
                .collect(Collectors.joining("\n"));
    }

    @Override
    public int estimateTokens() {
        return contents.stream()
                .mapToInt(Content::estimateTokens)
                .sum();
    }

    public static class Builder {
        private MessageRole role = MessageRole.USER;
        private List<Content> contents = new ArrayList<>();
        private String name;

        public Builder role(MessageRole role) {
            this.role = role;
            return this;
        }

        public Builder text(String text) {
            contents.add(new TextContent(text));
            return this;
        }

        public Builder image(String url) {
            contents.add(new ImageContent(url, null, ImageDetail.AUTO));
            return this;
        }

        public Builder image(String url, ImageDetail detail) {
            contents.add(new ImageContent(url, null, detail));
            return this;
        }

        public Builder imageBase64(String base64, String mimeType) {
            contents.add(new ImageContent(base64, mimeType, ImageDetail.AUTO));
            return this;
        }

        public Builder audio(byte[] audioData, AudioFormat format) {
            contents.add(new AudioContent(audioData, format));
            return this;
        }

        public Builder name(String name) {
            this.name = name;
            return this;
        }

        public MultiModalMessage build() {
            return new MultiModalMessage(role, List.copyOf(contents), name);
        }
    }
}

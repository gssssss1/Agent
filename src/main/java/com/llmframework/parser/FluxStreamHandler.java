package com.llmframework.parser;

import reactor.core.publisher.Flux;

public class FluxStreamHandler {
    
    /**
     * 使用 StreamHandler 订阅 Flux
     */
    public static <T> void subscribe(Flux<T> flux, StreamHandler<T> handler) {
        handler.onStart();
        flux.subscribe(
            handler::onChunk,
            error -> {
                handler.onError(error);
            },
            handler::onComplete
        );
    }
    
    /**
     * 同步阻塞等待 Flux 完成并使用 StreamHandler
     */
    public static <T> void subscribeBlocking(Flux<T> flux, StreamHandler<T> handler) {
        handler.onStart();
        flux.doOnNext(handler::onChunk)
            .doOnError(handler::onError)
            .doOnComplete(handler::onComplete)
            .blockLast();
    }
}

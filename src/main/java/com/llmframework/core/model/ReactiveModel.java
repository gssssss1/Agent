package com.llmframework.core.model;

import org.reactivestreams.Publisher;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Core reactive model interface
 *
 * @param <I> Input type
 * @param <O> Output type
 */
public interface ReactiveModel<I extends ModelInput, O extends ModelOutput> {

    /**
     * Async call (recommended)
     *
     * @return Mono wrapped single response
     */
    Mono<O> call(I input);

    /**
     * Streaming call
     *
     * @return Flux wrapped streaming response
     */
    Flux<StreamingChunk<O>> stream(I input);

    /**
     * Batch call (auto-optimized)
     *
     * @return Flux wrapped multiple responses
     */
    Flux<O> callBatch(Publisher<I> inputs);

    /**
     * Sync call (convenience method, blocks internally)
     */
    default O callSync(I input) {
        return call(input).block();
    }

    /**
     * Get model capabilities
     */
    ModelCapabilities capabilities();

    /**
     * Get model metadata
     */
    ModelMetadata metadata();
}

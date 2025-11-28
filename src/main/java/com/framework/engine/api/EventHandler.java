package com.framework.engine.api;

/**
 * Functional interface for handling events.
 *
 * @param <T> The type of the payload in the event envelope.
 */
@FunctionalInterface
public interface EventHandler<T> {

    /**
     * Handles the incoming event.
     *
     * @param event The event envelope containing metadata and payload.
     */
    void handle(EventEnvelope<T> event);
}

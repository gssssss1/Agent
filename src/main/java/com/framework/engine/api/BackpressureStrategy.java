package com.framework.engine.api;

/**
 * Strategy to handle backpressure when the event queue is full.
 */
public enum BackpressureStrategy {
    /**
     * Blocks the producer thread until space becomes available.
     */
    BLOCK_PRODUCER,

    /**
     * Drops the oldest event in the queue to make room for the new one.
     */
    DROP_OLDEST,

    /**
     * Throws an exception immediately.
     */
    THROW_EXCEPTION
}

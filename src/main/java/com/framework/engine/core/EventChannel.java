package com.framework.engine.core;

import com.framework.engine.api.EventEnvelope;

/**
 * Interface for the event communication channel.
 */
public interface EventChannel {
    
    /**
     * Sends an event to the channel.
     * Behavior when the channel is full depends on implementation.
     *
     * @param event The event to send.
     * @return true if sent, false otherwise (depending on policy).
     * @throws InterruptedException if interrupted while waiting.
     */
    boolean send(EventEnvelope<?> event) throws InterruptedException;

    /**
     * Takes an event from the channel, blocking if necessary.
     *
     * @return The event.
     * @throws InterruptedException if interrupted.
     */
    EventEnvelope<?> take() throws InterruptedException;
}

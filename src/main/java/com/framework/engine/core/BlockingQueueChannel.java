package com.framework.engine.core;

import com.framework.engine.api.BackpressureStrategy;
import com.framework.engine.api.EngineConfiguration;
import com.framework.engine.api.EventEnvelope;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

/**
 * Implementation of EventChannel using a BlockingQueue.
 * Supports configured backpressure strategies.
 */
public class BlockingQueueChannel implements EventChannel {
    
    private final BlockingQueue<EventEnvelope<?>> queue;
    private final BackpressureStrategy strategy;

    public BlockingQueueChannel(EngineConfiguration config) {
        this.queue = new ArrayBlockingQueue<>(config.queueCapacity());
        this.strategy = config.backpressureStrategy();
    }

    @Override
    public boolean send(EventEnvelope<?> event) throws InterruptedException {
        switch (strategy) {
            case BLOCK_PRODUCER:
                queue.put(event);
                return true;
            case THROW_EXCEPTION:
                if (!queue.offer(event)) {
                    throw new IllegalStateException("Event queue is full. Event ID: " + event.id());
                }
                return true;
            case DROP_OLDEST:
                // Attempt to offer, if full, drop oldest and retry.
                // Loop is needed to handle race conditions where another producer fills the slot.
                while (!queue.offer(event)) {
                    queue.poll(); // Drop the oldest event
                }
                return true;
            default:
                throw new IllegalArgumentException("Unknown backpressure strategy: " + strategy);
        }
    }

    @Override
    public EventEnvelope<?> take() throws InterruptedException {
        return queue.take();
    }
}

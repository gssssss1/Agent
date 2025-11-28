package com.framework.engine.core;

import com.framework.engine.api.EventEnvelope;
import com.framework.engine.api.EventHandler;
import com.framework.engine.api.EventRouter;
import com.framework.engine.api.Lifecycle;

import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * The main event loop that consumes events from the channel and dispatches them to handlers.
 * Runs in a dedicated virtual thread.
 */
public class EventDispatcher implements Lifecycle, Runnable {

    private final EventChannel channel;
    private final VirtualThreadExecutor executor;
    private final EventRouter router;
    private final AtomicBoolean running = new AtomicBoolean(false);
    private Thread dispatcherThread;

    public EventDispatcher(EventChannel channel, VirtualThreadExecutor executor, EventRouter router) {
        this.channel = channel;
        this.executor = executor;
        this.router = router;
    }

    @Override
    public void start() {
        if (running.compareAndSet(false, true)) {
            dispatcherThread = Thread.ofVirtual()
                    .name("vt-engine-dispatcher")
                    .start(this);
        }
    }

    @Override
    public void stop() {
        if (running.compareAndSet(true, false)) {
            if (dispatcherThread != null) {
                dispatcherThread.interrupt();
            }
        }
    }

    @Override
    public boolean isRunning() {
        return running.get();
    }

    @Override
    public void run() {
        while (running.get()) {
            try {
                EventEnvelope<?> event = channel.take();
                dispatch(event);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                // If interrupted, we check running status again
                if (!running.get()) {
                    break;
                }
            } catch (Exception e) {
                // TODO: Replace with proper ErrorHandler strategy
                System.err.println("Error in EventDispatcher loop: " + e.getMessage());
                e.printStackTrace();
            }
        }
    }

    private void dispatch(EventEnvelope<?> event) {
        List<EventHandler<?>> handlers = router.route(event);
        if (handlers == null || handlers.isEmpty()) {
            return;
        }

        for (EventHandler<?> handler : handlers) {
            executor.execute(() -> {
                try {
                    // Suppress warnings as we assume Router returns compatible handlers
                    @SuppressWarnings("unchecked")
                    EventHandler<Object> typedHandler = (EventHandler<Object>) handler;
                    @SuppressWarnings("unchecked")
                    EventEnvelope<Object> typedEvent = (EventEnvelope<Object>) event;
                    
                    typedHandler.handle(typedEvent);
                } catch (Exception e) {
                    // TODO: Replace with proper ErrorHandler strategy
                    System.err.println("Error processing event " + event.id() + ": " + e.getMessage());
                    e.printStackTrace();
                }
            });
        }
    }
}

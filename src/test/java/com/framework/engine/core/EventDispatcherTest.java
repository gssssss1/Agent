package com.framework.engine.core;

import com.framework.engine.api.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.assertTrue;

class EventDispatcherTest {

    private VirtualThreadExecutor executor;
    private BlockingQueueChannel channel;
    private EventDispatcher dispatcher;
    private EventRouter router;

    @BeforeEach
    void setUp() {
        executor = new VirtualThreadExecutor("test-worker");
        EngineConfiguration config = EngineConfiguration.defaultConfig();
        channel = new BlockingQueueChannel(config);
    }

    @AfterEach
    void tearDown() {
        if (dispatcher != null) {
            dispatcher.stop();
        }
        executor.shutdown();
    }

    @Test
    void testDispatching() throws InterruptedException {
        CountDownLatch latch = new CountDownLatch(1);
        
        EventHandler<String> handler = event -> {
            System.out.println("Handled event: " + event.payload());
            latch.countDown();
        };

        // Router that returns the handler for any event
        router = event -> List.of(handler);
        
        dispatcher = new EventDispatcher(channel, executor, router);
        dispatcher.start();
        assertTrue(dispatcher.isRunning());

        EventEnvelope<String> event = new EventEnvelope<>(
                "1", System.currentTimeMillis(), Map.of(), "Hello World"
        );

        channel.send(event);

        boolean completed = latch.await(5, TimeUnit.SECONDS);
        assertTrue(completed, "Handler should have been called");
    }
}

package com.framework.engine.core;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Executor service based on Java 21 Virtual Threads.
 * Encapsulates the creation and management of the virtual thread executor.
 */
public class VirtualThreadExecutor implements Executor {

    private final java.util.concurrent.ExecutorService delegate;

    public VirtualThreadExecutor(String namePrefix) {
        ThreadFactory factory = new EngineThreadFactory(namePrefix);
        this.delegate = Executors.newThreadPerTaskExecutor(factory);
    }

    @Override
    public void execute(Runnable command) {
        delegate.execute(command);
    }

    public void shutdown() {
        delegate.shutdown();
    }
    
    public java.util.concurrent.ExecutorService getDelegate() {
        return delegate;
    }

    private static class EngineThreadFactory implements ThreadFactory {
        private final String namePrefix;
        private final AtomicInteger counter = new AtomicInteger(0);
        
        public EngineThreadFactory(String namePrefix) {
            this.namePrefix = namePrefix;
        }

        @Override
        public Thread newThread(Runnable r) {
            return Thread.ofVirtual()
                    .name(namePrefix + "-" + counter.getAndIncrement())
                    .unstarted(r);
        }
    }
}

package com.framework.engine.api;

/**
 * Provides runtime context to the EventHandler.
 * Allows handlers to interact with the engine, such as sending new events
 * or accessing configuration.
 */
public interface HandlerContext {

    /**
     * Publishes a new event to the engine.
     *
     * @param event The event to publish.
     */
    void publish(IEvent event);

    /**
     * Retrieves the engine configuration.
     *
     * @return the engine configuration.
     */
    EngineConfiguration getConfiguration();
}

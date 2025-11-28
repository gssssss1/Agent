package com.framework.engine.api;

/**
 * Standard interface for component lifecycle management.
 */
public interface Lifecycle {

    /**
     * Starts the component.
     */
    void start();

    /**
     * Stops the component.
     */
    void stop();

    /**
     * Checks if the component is currently running.
     *
     * @return true if running, false otherwise.
     */
    boolean isRunning();
}

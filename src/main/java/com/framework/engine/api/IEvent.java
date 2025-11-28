package com.framework.engine.api;

import java.util.Map;

/**
 * Marker interface defining the basic contract of an event.
 * <p>
 * Defines the core attributes that every event must possess:
 * an ID, a timestamp, and metadata.
 */
public interface IEvent {

    /**
     * Returns the unique identifier for the event.
     *
     * @return the event ID
     */
    String id();

    /**
     * Returns the timestamp when the event occurred.
     *
     * @return the timestamp in epoch milliseconds
     */
    long timestamp();

    /**
     * Returns the metadata associated with the event.
     *
     * @return a map of metadata headers
     */
    Map<String, Object> metadata();
}

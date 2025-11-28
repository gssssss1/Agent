package com.framework.engine.api;

import java.util.Collections;
import java.util.Map;
import java.util.Objects;

/**
 * Standard event envelope containing Header (metadata) and Payload (business data).
 *
 * @param <T> The type of the payload.
 */
public class EventEnvelope<T> implements IEvent {

    private final String id;
    private final long timestamp;
    private final Map<String, Object> header;
    private final T payload;

    /**
     * Constructs a new EventEnvelope.
     *
     * @param id        Unique identifier for the event.
     * @param timestamp Timestamp when the event occurred.
     * @param header    Metadata associated with the event.
     * @param payload   The business data.
     */
    public EventEnvelope(String id, long timestamp, Map<String, Object> header, T payload) {
        this.id = Objects.requireNonNull(id, "Event ID must not be null");
        this.timestamp = timestamp;
        this.header = header != null ? Map.copyOf(header) : Map.of();
        this.payload = payload;
    }

    @Override
    public String id() {
        return id;
    }

    @Override
    public long timestamp() {
        return timestamp;
    }

    @Override
    public Map<String, Object> metadata() {
        return header;
    }

    /**
     * Returns the payload (business data) of the event.
     *
     * @return the payload
     */
    public T payload() {
        return payload;
    }

    @Override
    public String toString() {
        return "EventEnvelope{" +
                "id='" + id + '\'' +
                ", timestamp=" + timestamp +
                ", header=" + header +
                ", payload=" + payload +
                '}';
    }
}

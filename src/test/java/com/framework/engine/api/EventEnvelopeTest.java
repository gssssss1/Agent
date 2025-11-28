package com.framework.engine.api;

import org.junit.jupiter.api.Test;
import java.util.Map;
import static org.junit.jupiter.api.Assertions.*;

class EventEnvelopeTest {

    @Test
    void testEventEnvelopeCreation() {
        String id = "test-1";
        long timestamp = System.currentTimeMillis();
        Map<String, Object> header = Map.of("key", "value");
        String payload = "payload";

        EventEnvelope<String> envelope = new EventEnvelope<>(id, timestamp, header, payload);

        assertEquals(id, envelope.id());
        assertEquals(timestamp, envelope.timestamp());
        assertEquals(header, envelope.metadata());
        assertEquals(payload, envelope.payload());
    }

    @Test
    void testEventEnvelopeImmutability() {
        String id = "test-2";
        long timestamp = System.currentTimeMillis();
        Map<String, Object> header = Map.of("key", "value");
        String payload = "payload";

        EventEnvelope<String> envelope = new EventEnvelope<>(id, timestamp, header, payload);

        // Verify that returned map is unmodifiable or at least distinct if we were using a mutable map source
        // Since Map.of is already immutable, let's try to modify the returned metadata
        assertThrows(UnsupportedOperationException.class, () -> {
            envelope.metadata().put("new", "val");
        });
    }

    @Test
    void testNullIdThrowsException() {
        assertThrows(NullPointerException.class, () -> {
            new EventEnvelope<>(null, System.currentTimeMillis(), Map.of(), "payload");
        });
    }

    @Test
    void testNullHeaderCreatesEmptyMap() {
        EventEnvelope<String> envelope = new EventEnvelope<>("id", System.currentTimeMillis(), null, "payload");
        assertNotNull(envelope.metadata());
        assertTrue(envelope.metadata().isEmpty());
    }
}

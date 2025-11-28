package com.framework.engine.api;

import java.util.List;

/**
 * Interface for routing events to their appropriate handlers.
 */
public interface EventRouter {
    
    /**
     * Determines which handlers should process the given event.
     *
     * @param event The event to route.
     * @return A list of handlers subscribed to this event.
     */
    List<EventHandler<?>> route(EventEnvelope<?> event);
}

package com.llmframework.observability.impl;

import com.llmframework.core.model.ModelRequest;
import com.llmframework.core.model.ModelResponse;
import com.llmframework.observability.ModelObserver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LoggingObserver implements ModelObserver {
    private static final Logger logger = LoggerFactory.getLogger(LoggingObserver.class);
    
    @Override
    public void onCallStart(ModelRequest request) {
        logger.info("Model call started - Request ID: {}", request.getRequestId());
    }
    
    @Override
    public void onCallSuccess(ModelRequest request, ModelResponse response) {
        logger.info("Model call succeeded - Request ID: {}, Duration: {}ms", 
            request.getRequestId(), 
            response.getMetadata().getDuration());
    }
    
    @Override
    public void onCallError(ModelRequest request, Throwable error) {
        logger.error("Model call failed - Request ID: {}, Error: {}", 
            request.getRequestId(), 
            error.getMessage(), 
            error);
    }
    
    @Override
    public void onStreamStart(ModelRequest request) {
        logger.info("Streaming call started - Request ID: {}", request.getRequestId());
    }
    
    @Override
    public void onStreamChunk(ModelRequest request, ModelResponse chunk) {
        logger.debug("Streaming chunk received - Request ID: {}", request.getRequestId());
    }
    
    @Override
    public void onStreamComplete(ModelRequest request) {
        logger.info("Streaming call completed - Request ID: {}", request.getRequestId());
    }
}

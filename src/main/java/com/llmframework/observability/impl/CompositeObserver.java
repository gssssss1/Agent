package com.llmframework.observability.impl;

import com.llmframework.core.model.ModelRequest;
import com.llmframework.core.model.ModelResponse;
import com.llmframework.observability.ModelObserver;
import java.util.ArrayList;
import java.util.List;

public class CompositeObserver implements ModelObserver {
    private final List<ModelObserver> observers = new ArrayList<>();
    
    public void addObserver(ModelObserver observer) {
        observers.add(observer);
    }
    
    public void removeObserver(ModelObserver observer) {
        observers.remove(observer);
    }
    
    @Override
    public void onCallStart(ModelRequest request) {
        observers.forEach(observer -> observer.onCallStart(request));
    }
    
    @Override
    public void onCallSuccess(ModelRequest request, ModelResponse response) {
        observers.forEach(observer -> observer.onCallSuccess(request, response));
    }
    
    @Override
    public void onCallError(ModelRequest request, Throwable error) {
        observers.forEach(observer -> observer.onCallError(request, error));
    }
    
    @Override
    public void onStreamStart(ModelRequest request) {
        observers.forEach(observer -> observer.onStreamStart(request));
    }
    
    @Override
    public void onStreamChunk(ModelRequest request, ModelResponse chunk) {
        observers.forEach(observer -> observer.onStreamChunk(request, chunk));
    }
    
    @Override
    public void onStreamComplete(ModelRequest request) {
        observers.forEach(observer -> observer.onStreamComplete(request));
    }
}

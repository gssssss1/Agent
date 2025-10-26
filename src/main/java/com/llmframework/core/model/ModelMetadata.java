package com.llmframework.core.model;

import java.util.Map;

public class ModelMetadata {
    private String name;
    private String version;
    private String provider;
    private Map<String, Object> capabilities;
    
    public ModelMetadata() {
    }
    
    public ModelMetadata(String name, String version, String provider, Map<String, Object> capabilities) {
        this.name = name;
        this.version = version;
        this.provider = provider;
        this.capabilities = capabilities;
    }
    
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public String getVersion() {
        return version;
    }
    
    public void setVersion(String version) {
        this.version = version;
    }
    
    public String getProvider() {
        return provider;
    }
    
    public void setProvider(String provider) {
        this.provider = provider;
    }
    
    public Map<String, Object> getCapabilities() {
        return capabilities;
    }
    
    public void setCapabilities(Map<String, Object> capabilities) {
        this.capabilities = capabilities;
    }
}

package com.llmframework.client;

import com.llmframework.core.model.Model;
import com.llmframework.core.model.ModelRequest;
import java.util.Set;

public interface ModelRegistry {
    /**
     * 注册模型
     */
    void register(String name, Model<?, ?> model);
    
    /**
     * 获取模型
     */
    <M extends Model<?, ?>> M getModel(String name, Class<M> modelClass);
    
    /**
     * 获取所有模型名称
     */
    Set<String> getModelNames();
    
    /**
     * 路由到合适的模型
     */
    <M extends Model<?, ?>> M route(ModelRequest request, Class<M> modelClass);
}

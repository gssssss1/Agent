package com.llmframework.core.model;

import java.util.List;
import java.util.Map;

public interface ModelOptions {
    /**
     * 温度参数（0.0 - 2.0）
     */
    Double getTemperature();
    
    /**
     * Top P 参数
     */
    Double getTopP();
    
    /**
     * 最大 Token 数
     */
    Integer getMaxTokens();
    
    /**
     * 停止序列
     */
    List<String> getStopSequences();
    
    /**
     * 其他扩展选项
     */
    Map<String, Object> getAdditionalOptions();
}

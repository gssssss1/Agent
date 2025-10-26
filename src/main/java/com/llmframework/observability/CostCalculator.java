package com.llmframework.observability;

import com.llmframework.core.result.ResultMetadata;
import java.math.BigDecimal;

public interface CostCalculator {
    /**
     * 计算单次调用成本
     */
    BigDecimal calculateCost(ResultMetadata metadata, String modelName);
    
    /**
     * 获取模型定价信息
     */
    PricingInfo getPricingInfo(String modelName);
}

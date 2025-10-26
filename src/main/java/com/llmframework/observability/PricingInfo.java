package com.llmframework.observability;

import java.math.BigDecimal;

public class PricingInfo {
    private BigDecimal inputTokenPrice;
    private BigDecimal outputTokenPrice;
    private String currency;
    
    public PricingInfo() {
        this.currency = "USD";
    }
    
    public PricingInfo(BigDecimal inputTokenPrice, BigDecimal outputTokenPrice, String currency) {
        this.inputTokenPrice = inputTokenPrice;
        this.outputTokenPrice = outputTokenPrice;
        this.currency = currency;
    }
    
    public BigDecimal getInputTokenPrice() {
        return inputTokenPrice;
    }
    
    public void setInputTokenPrice(BigDecimal inputTokenPrice) {
        this.inputTokenPrice = inputTokenPrice;
    }
    
    public BigDecimal getOutputTokenPrice() {
        return outputTokenPrice;
    }
    
    public void setOutputTokenPrice(BigDecimal outputTokenPrice) {
        this.outputTokenPrice = outputTokenPrice;
    }
    
    public String getCurrency() {
        return currency;
    }
    
    public void setCurrency(String currency) {
        this.currency = currency;
    }
}

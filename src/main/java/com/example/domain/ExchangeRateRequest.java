// src/main/java/com/example/domain/ExchangeRateRequest.java
package com.example.domain;

import io.quarkus.runtime.annotations.RegisterForReflection;

import java.math.BigDecimal;

@RegisterForReflection
public class ExchangeRateRequest {
    private String sourceCurrency;
    private String targetCurrency;
    private BigDecimal amount;

    public ExchangeRateRequest() {
        // Constructor vacío requerido por Jackson
    }

    public ExchangeRateRequest(String sourceCurrency, String targetCurrency, BigDecimal amount) {
        this.sourceCurrency = sourceCurrency;
        this.targetCurrency = targetCurrency;
        this.amount = amount;
    }

    public String getSourceCurrency() {
        return sourceCurrency;
    }

    public void setSourceCurrency(String sourceCurrency) {
        this.sourceCurrency = sourceCurrency;
    }

    public String getTargetCurrency() {
        return targetCurrency;
    }

    public void setTargetCurrency(String targetCurrency) {
        this.targetCurrency = targetCurrency;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }
}
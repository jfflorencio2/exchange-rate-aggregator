// src/main/java/com/example/domain/ExchangeRateGateway.java
package com.example.domain;

import io.smallrye.mutiny.Uni;

import java.math.BigDecimal;

public interface ExchangeRateGateway {
    Uni<BigDecimal> getExchangeRate(String sourceCurrency, String targetCurrency, BigDecimal amount);
}
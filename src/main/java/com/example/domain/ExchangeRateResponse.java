// src/main/java/com/example/domain/ExchangeRateResponse.java
package com.example.domain;

import java.math.BigDecimal;

public class ExchangeRateResponse {
    private BigDecimal convertedAmount;
    private String apiSource;

    public ExchangeRateResponse(BigDecimal convertedAmount, String apiSource) {
        this.convertedAmount = convertedAmount;
        this.apiSource = apiSource;
    }

    public BigDecimal getConvertedAmount() {
        return convertedAmount;
    }

    public String getApiSource() {
        return apiSource;
    }

    @Override
    public String toString() {
        return "ExchangeRateResponse{" +
                "convertedAmount=" + convertedAmount +
                ", apiSource='" + apiSource + '\'' +
                '}';
    }
}
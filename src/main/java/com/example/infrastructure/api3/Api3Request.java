package com.example.infrastructure.api3;

import java.math.BigDecimal;

public class Api3Request {
    public Exchange exchange;

    public Api3Request(String sourceCurrency, String targetCurrency, BigDecimal quantity) {
        this.exchange = new Exchange(sourceCurrency, targetCurrency, quantity);
    }

    public static class Exchange {
        public String sourceCurrency;
        public String targetCurrency;
        public BigDecimal quantity;

        public Exchange(String sourceCurrency, String targetCurrency, BigDecimal quantity) {
            this.sourceCurrency = sourceCurrency;
            this.targetCurrency = targetCurrency;
            this.quantity = quantity;
        }
    }
}
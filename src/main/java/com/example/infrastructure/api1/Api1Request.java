package com.example.infrastructure.api1;

import java.math.BigDecimal;

public class Api1Request {
    public String from;
    public String to;
    public BigDecimal value;

    public Api1Request(String from, String to, BigDecimal value) {
        this.from = from;
        this.to = to;
        this.value = value;
    }
}
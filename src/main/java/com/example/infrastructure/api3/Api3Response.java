package com.example.infrastructure.api3;

import java.math.BigDecimal;

public class Api3Response {
    public int statusCode;
    public String message;
    public Data data;

    public static class Data {
        public BigDecimal total;
    }
}





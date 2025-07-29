package com.example.infrastructure.api2;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;

import java.math.BigDecimal;

@XmlRootElement(name = "XML")
@XmlAccessorType(XmlAccessType.FIELD)
public class Api2Request {
    @XmlElement(name = "From")
    public String from;
    @XmlElement(name = "To")
    public String to;
    @XmlElement(name = "Amount")
    public BigDecimal amount;

    public Api2Request() {}

    public Api2Request(String from, String to, BigDecimal amount) {
        this.from = from;
        this.to = to;
        this.amount = amount;
    }
}






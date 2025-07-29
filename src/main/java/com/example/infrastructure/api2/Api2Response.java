package com.example.infrastructure.api2;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;

import java.math.BigDecimal;

@XmlRootElement(name = "XML")
@XmlAccessorType(XmlAccessType.FIELD)
public class Api2Response {
    @XmlElement(name = "Result")
    public BigDecimal result;
}
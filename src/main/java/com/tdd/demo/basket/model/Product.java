package com.tdd.demo.basket.model;

import lombok.AllArgsConstructor;

import java.math.BigDecimal;
import java.util.UUID;

@AllArgsConstructor
public class Product {
    private UUID productId;
    private String productName;
    private BigDecimal price;
    private Currency currency;
}


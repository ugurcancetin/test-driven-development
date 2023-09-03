package com.tdd.demo.basket.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.math.BigDecimal;
import java.util.UUID;

@AllArgsConstructor
@Getter
public class Product {
    private UUID productId;
    private String productName;
    private BigDecimal price;
    private Currency currency;
}


package com.tdd.demo.basket.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class BasketItem {
    private Product product;
    private int quantity;
}

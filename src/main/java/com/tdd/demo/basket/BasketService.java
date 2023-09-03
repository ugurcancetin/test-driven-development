package com.tdd.demo.basket;

import com.tdd.demo.basket.model.BasketItem;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@AllArgsConstructor
public class BasketService {
    Map<UUID, Set<BasketItem>> basketCollection;

    public UUID createBasket() {
        var newBasketId = UUID.randomUUID();
        basketCollection.put(newBasketId, new HashSet<>());
        return newBasketId;
    }

    public Set<BasketItem> addItem(UUID basketId, BasketItem item) {
        var basket = basketCollection.get(basketId);
        if (basket == null) {
            throw new RuntimeException("Basket Not Found");
        }
        basket.add(item);
        return basket;
    }

    public Set<BasketItem> addItems(UUID basketId, Set<BasketItem> basketItems){
        var basket = basketCollection.get(basketId);
        basket.addAll(basketItems);
        return basket;
    }
}

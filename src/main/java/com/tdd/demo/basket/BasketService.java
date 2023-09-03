package com.tdd.demo.basket;

import com.tdd.demo.basket.model.BasketItem;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@AllArgsConstructor
public class BasketService {
    Map<UUID, Map<UUID, BasketItem>> basketCollection;

    public UUID createBasket() {
        var newBasketId = UUID.randomUUID();
        basketCollection.put(newBasketId, new HashMap<>());
        return newBasketId;
    }

    public Map<UUID, BasketItem> addItem(UUID basketId, BasketItem item) {
        var basket = basketCollection.get(basketId);
        if (basket == null) {
            throw new RuntimeException("Basket Not Found");
        }
        basket.put(item.getProduct().getProductId(), item);
        return basket;
    }

    public Map<UUID, BasketItem> addItems(UUID basketId, Map<UUID, BasketItem> basketItems){
        var basket = basketCollection.get(basketId);
        basket.putAll(basketItems);
        return basket;
    }
}

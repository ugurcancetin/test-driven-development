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

    public Map<UUID, BasketItem> getBasket(UUID basketId) {
        return copyBasket(basketCollection.get(basketId));
    }

    public UUID deleteBasket(UUID basketId){
        if(!basketCollection.containsKey(basketId)) {
            throw new RuntimeException("Basket Not Found");
        }

        basketCollection.remove(basketId);
        return basketId;
    }

    public Map<UUID, BasketItem> addItem(UUID basketId, BasketItem item) {
        var basket = basketCollection.get(basketId);
        if (basket == null) {
            throw new RuntimeException("Basket Not Found");
        }
        var productId = item.getProduct().getProductId();
        basket.computeIfPresent(productId, (key, val) -> new BasketItem(val.getProduct(), val.getQuantity() + item.getQuantity()));
        basket.putIfAbsent(productId, item);

        return copyBasket(basket);
    }

    public Map<UUID, BasketItem> addItems(UUID basketId, Map<UUID, BasketItem> basketItems) {
        var basket = basketCollection.get(basketId);
        basketItems.keySet().forEach(basketItemId -> aggregateBasketItems(basketItems, basket, basketItemId));
        return copyBasket(basket);
    }

    private Map<UUID, BasketItem> copyBasket(Map<UUID, BasketItem> currentBasket) {
        return new HashMap<>(currentBasket);
    }

    private void aggregateBasketItems(Map<UUID, BasketItem> basketItems, Map<UUID, BasketItem> basket, UUID basketItemId) {
        if (basket.containsKey(basketItemId)) {
            var newQuantity = basket.get(basketItemId).getQuantity() + basketItems.get(basketItemId).getQuantity();
            var newBasketItem = new BasketItem(basket.get(basketItemId).getProduct(), newQuantity);
            basket.put(basketItemId, newBasketItem);
        } else {
            basket.put(basketItemId, basketItems.get(basketItemId));
        }
    }
}

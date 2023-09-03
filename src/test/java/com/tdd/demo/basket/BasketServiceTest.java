package com.tdd.demo.basket;

import com.tdd.demo.basket.model.BasketItem;
import com.tdd.demo.basket.model.Currency;
import com.tdd.demo.basket.model.Product;
import org.junit.jupiter.api.*;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class BasketServiceTest {

    BasketService basketService = new BasketService(new HashMap<>());

    @DisplayName("Create a new basket")
    @Test
    @Order(1)
    void when_createNewBasket_thenReturn_aNewBasketId() {
        assertNotNull(basketService.createBasket());
    }

    @DisplayName("Add an item into the basket")
    @Test
    @Order(2)
    void when_addNewItemIntoTheBasket_thenReturn_newBasketContent(){
        var basketItem = createSingleBasketItem();
        var basketId = basketService.createBasket();
        var basketContent = basketService.addItem(basketId,  basketItem);
        assertFalse(basketContent.isEmpty());
        assertTrue(basketContent.contains(basketItem));
    }

    @DisplayName("Add an item into the basket that doesn't exist")
    @Test
    @Order(3)
    void when_addNewItemIntoTheBasketThatDoesntExist_thenThrow_BasketNotFoundException(){
        assertThrows(RuntimeException.class,
                () ->  basketService.addItem(UUID.randomUUID(), createSingleBasketItem()));
    }

    @DisplayName("Add Multiple Products into the basket at once")
    @Test
    @Order(4)
    void when_addingMultipleItemIntoTheBasket_thenReturn_updatedBasketContent() {
        var basketId = basketService.createBasket();
        var basketItems = createMultipleBasketItems();
        var basketContent = basketService.addItems(basketId, basketItems);
        assertTrue(basketContent.containsAll(basketItems));
    }

    private BasketItem createSingleBasketItem() {
        Product macBookPro = new Product(UUID.randomUUID(), "MacBook Pro", new BigDecimal(1500), Currency.DOLLAR);
        return new BasketItem(macBookPro, (short) 1);
    }

    private Set<BasketItem> createMultipleBasketItems() {
        var macBookPro = new Product(UUID.randomUUID(), "MacBook Pro", new BigDecimal(2500), Currency.DOLLAR);
        var iPhonePro = new Product(UUID.randomUUID(), "iPhone Pro", new BigDecimal(2000), Currency.DOLLAR);
        var macBookProBasketItem = new BasketItem(macBookPro, (short) 2);
        var iPhoneBasketItem = new BasketItem(iPhonePro, (short) 1);

        var basketItems = new HashSet<BasketItem>();
        basketItems.add(macBookProBasketItem);
        basketItems.add(iPhoneBasketItem);
        return basketItems;
    }
}

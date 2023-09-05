package com.tdd.demo.basket;

import com.tdd.demo.basket.model.BasketItem;
import com.tdd.demo.basket.model.Currency;
import com.tdd.demo.basket.model.Product;
import org.junit.jupiter.api.*;

import java.math.BigDecimal;
import java.util.*;

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
    void when_addNewItemIntoTheBasket_thenReturn_newBasketContent() {
        var basketItem = createSingleBasketItem();
        var basketId = basketService.createBasket();
        var basketContent = basketService.addItem(basketId, basketItem);
        assertFalse(basketContent.isEmpty());
        assertTrue(basketContent.containsValue(basketItem));
    }

    @DisplayName("Add an item into the basket that doesn't exist")
    @Test
    @Order(3)
    void when_addNewItemIntoTheBasketThatDoesntExist_thenThrow_BasketNotFoundException() {
        assertThrows(RuntimeException.class,
                () -> basketService.addItem(UUID.randomUUID(), createSingleBasketItem()));
    }

    @DisplayName("Add Multiple Products into the basket at once")
    @Test
    @Order(4)
    void when_addingMultipleItemIntoTheBasket_thenReturn_updatedBasketContent() {
        var basketId = basketService.createBasket();
        var basketItems = createMultipleBasketItems();
        var basketContent = basketService.addItems(basketId, basketItems);
        assertFalse(basketContent.isEmpty());
        basketContent.values()
                .forEach(basketItem -> assertTrue(basketItems.containsValue(basketItem)));
    }

    @DisplayName("Add the same product twice")
    @Test
    @Order(5)
    void when_addingTheSameProductTwice_thenReturn_updatedBasketWithIncreaseQuantityOfTheProduct() {
        var basketId = basketService.createBasket();
        var basketItem = createSingleBasketItem();
        basketService.addItem(basketId, basketItem);
        var secondContent = basketService.addItem(basketId, basketItem);
        assertEquals(2, secondContent.get(basketItem.getProduct().getProductId()).getQuantity());
    }

    @DisplayName("Add multiple products to the basket twice")
    @Test
    @Order(6)
    void when_addingMultipleProductsTwice_thenReturn_updatedBasketWithCorrectlyAggregatedContent() {
        var basketId = basketService.createBasket();
        var basketItems = createMultipleBasketItems();
        basketService.addItems(basketId, basketItems);
        var updatedBasketContent = basketService.addItems(basketId, basketItems);
        assertEquals(Optional.of(6), updatedBasketContent.values().stream()
                .map(BasketItem::getQuantity)
                .reduce(Integer::sum));
    }

    @DisplayName("Get the basket content to be displayed")
    @Test
    @Order(7)
    void when_getBasketCalled_thenReturn_theCurrentBasketContent() {
        var basketId = basketService.createBasket();
        var basketItems = createMultipleBasketItems();
        basketService.addItems(basketId, basketItems);
        var basketContent = basketService.getBasket(basketId);
        assertTrue(basketContent.values().stream().allMatch(basketItems::containsValue));

        //The reason we don't use the returned basket content is to test the getBasket() method.
        basketService.addItem(basketId, createSingleBasketItem());
        var updatedBasket = basketService.getBasket(basketId);
        assertFalse(updatedBasket.values().stream().allMatch(basketContent::containsValue));
    }

    @DisplayName("Delete an existing basket")
    @Test
    @Order(8)
    void when_deleteBasketCalledWithExistingBasketId_thenReturn_deletedBasketId(){
        var basketId = basketService.createBasket();
        assertEquals(basketId, basketService.deleteBasket(basketId));
    }

    @DisplayName("Delete non-existing basket")
    @Test
    @Order(9)
    void when_deleteBasketCalledWithNonExistingBasketId_thenThrow_BasketNotFoundException(){
        assertThrows(RuntimeException.class, () -> basketService.deleteBasket(UUID.randomUUID()), "Basket Not Found");
    }



    private BasketItem createSingleBasketItem() {
        Product macBookPro = new Product(UUID.randomUUID(), "MacBook Pro", new BigDecimal(1500), Currency.DOLLAR);
        return new BasketItem(macBookPro, (short) 1);
    }

    private Map<UUID, BasketItem> createMultipleBasketItems() {
        var macBookPro = new Product(UUID.randomUUID(), "MacBook Pro", new BigDecimal(2500), Currency.DOLLAR);
        var iPhonePro = new Product(UUID.randomUUID(), "iPhone Pro", new BigDecimal(2000), Currency.DOLLAR);
        var macBookProBasketItem = new BasketItem(macBookPro, (short) 2);
        var iPhoneBasketItem = new BasketItem(iPhonePro, (short) 1);

        var basketItems = new HashMap<UUID, BasketItem>();
        basketItems.put(macBookPro.getProductId(), macBookProBasketItem);
        basketItems.put(iPhonePro.getProductId(), iPhoneBasketItem);
        return basketItems;
    }
}

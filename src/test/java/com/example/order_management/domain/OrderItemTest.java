package com.example.order_management.domain;

import com.example.order_management.domain.exception.InvalidItemException;
import com.example.order_management.domain.model.Money;
import com.example.order_management.domain.model.OrderItem;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class OrderItemTest {

    @Test
    void creatingOrderItemWithNonPositiveQuantityShouldThrowException() {
        UUID productId = UUID.randomUUID();
        Money unitPrice = new Money(new BigDecimal("5.00"), "USD");

        assertThrows(InvalidItemException.class, () -> new OrderItem(productId, 0, unitPrice));
        assertThrows(InvalidItemException.class, () -> new OrderItem(productId, -1, unitPrice));
    }

    @Test
    void creatingOrderItemWithNegativePriceShouldThrowException() {
        UUID productId = UUID.randomUUID();
        Money unitPrice = new Money(new BigDecimal("-1.00"), "USD");

        assertThrows(InvalidItemException.class, () -> new OrderItem(productId, 1, unitPrice));
    }

    @Test
    void subtotalShouldBeUnitPriceTimesQuantity() {
        UUID productId = UUID.randomUUID();
        Money unitPrice = new Money(new BigDecimal("2.50"), "USD");

        OrderItem item = new OrderItem(productId, 4, unitPrice);

        assertEquals(new BigDecimal("10.00"), item.subTotal().amount());
        assertEquals("USD", item.subTotal().currency());
    }
}


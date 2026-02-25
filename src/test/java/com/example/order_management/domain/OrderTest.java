package com.example.order_management.domain;

import com.example.order_management.domain.exception.InvalidItemException;
import com.example.order_management.domain.exception.InvalidOrderStateException;
import com.example.order_management.domain.model.Money;
import com.example.order_management.domain.model.Order;
import com.example.order_management.domain.model.OrderItem;
import com.example.order_management.domain.model.OrderStatus;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class OrderTest {

    @Test
    void creatingOrderWithoutItemsShouldThrowInvalidItemException() {
        UUID customerId = UUID.randomUUID();

        assertThrows(InvalidItemException.class, () -> Order.createNewOrder(customerId, List.of()));
    }

    @Test
    void orderTotalShouldBeSumOfItemSubtotals() {
        UUID customerId = UUID.randomUUID();
        OrderItem item1 = new OrderItem(UUID.randomUUID(), 2, new Money(new BigDecimal("5.00"), "USD"));
        OrderItem item2 = new OrderItem(UUID.randomUUID(), 1, new Money(new BigDecimal("12.50"), "USD"));

        Order order = Order.createNewOrder(customerId, List.of(item1, item2));

        assertEquals(new BigDecimal("22.50"), order.getTotalAmount().amount());
        assertEquals("USD", order.getTotalAmount().currency());
    }

    @Test
    void cannotMarkOrderAsPaidWhenTotalIsBelowMinimumThreshold() {
        UUID customerId = UUID.randomUUID();
        OrderItem item = new OrderItem(UUID.randomUUID(), 1, new Money(new BigDecimal("5.00"), "USD"));

        Order order = Order.createNewOrder(customerId, List.of(item));

        assertThrows(InvalidOrderStateException.class, order::markAsPaid);
    }

    @Test
    void canMarkOrderAsPaidWhenTotalMeetsMinimumThreshold() {
        UUID customerId = UUID.randomUUID();
        OrderItem item = new OrderItem(UUID.randomUUID(), 2, new Money(new BigDecimal("5.00"), "USD"));

        Order order = Order.createNewOrder(customerId, List.of(item));

        order.markAsPaid();

        assertEquals(OrderStatus.PAID, order.getStatus());
    }

    @Test
    void cancellingPendingOrPaidOrderShouldSucceed() {
        UUID customerId = UUID.randomUUID();
        OrderItem item = new OrderItem(UUID.randomUUID(), 2, new Money(new BigDecimal("5.00"), "USD"));

        Order order = Order.createNewOrder(customerId, List.of(item));
        order.markAsPaid();

        order.cancel();

        assertEquals(OrderStatus.CANCELLED, order.getStatus());
    }

    @Test
    void cancellingShippedOrderShouldFail() {
        UUID customerId = UUID.randomUUID();
        OrderItem item = new OrderItem(UUID.randomUUID(), 2, new Money(new BigDecimal("5.00"), "USD"));

        Order order = Order.createNewOrder(customerId, List.of(item));
        order.markAsPaid();
        order.ship();

        assertThrows(InvalidOrderStateException.class, order::cancel);
    }

    @Test
    void shippingOrderShouldOnlyBeAllowedFromPaidStatus() {
        UUID customerId = UUID.randomUUID();
        OrderItem item = new OrderItem(UUID.randomUUID(), 2, new Money(new BigDecimal("5.00"), "USD"));

        Order order = Order.createNewOrder(customerId, List.of(item));

        assertThrows(InvalidOrderStateException.class, order::ship);
    }
}


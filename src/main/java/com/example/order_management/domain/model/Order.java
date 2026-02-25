package com.example.order_management.domain.model;

import com.example.order_management.domain.exception.InvalidItemException;
import com.example.order_management.domain.exception.InvalidOrderStateException;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

public final class Order {

    private static final BigDecimal MIN_PAID_THRESHOLD = new BigDecimal("10.00");

    private final UUID id;
    private final UUID customerId;
    private OrderStatus status;
    private final List<OrderItem> items;
    private final Instant createdAt;

    private Order(UUID id, UUID customerId, List<OrderItem> items, Instant createdAt) {
        this.id = id;
        this.customerId = customerId;
        this.status = OrderStatus.PENDING;
        this.items = new ArrayList<>(items);
        this.createdAt = createdAt != null ? createdAt : Instant.now();
    }

    public static Order createNewOrder(UUID customerId, List<OrderItem> items) {
        if (customerId == null) {
            throw new InvalidItemException("Customer ID cannot be null");
        }
        if (items == null || items.isEmpty()) {
            throw new InvalidItemException("Order must have at least one item");
        }
        return new Order(UUID.randomUUID(), customerId, items, Instant.now());
    }

    public static Order reconstitute(UUID id, UUID customerId, OrderStatus status,
                                    List<OrderItem> items, Money totalAmount, String currency,
                                    Instant createdAt) {
        Order order = new Order(id, customerId, items, createdAt);
        order.status = status;
        return order;
    }

    public UUID getId() {
        return id;
    }

    public UUID getCustomerId() {
        return customerId;
    }

    public OrderStatus getStatus() {
        return status;
    }

    public List<OrderItem> getItems() {
        return Collections.unmodifiableList(items);
    }

    public Money getTotalAmount() {
        if (items.isEmpty()) {
            return new Money(BigDecimal.ZERO, "USD");
        }
        Money total = items.get(0).subTotal();
        for (int i = 1; i < items.size(); i++) {
            total = total.add(items.get(i).subTotal());
        }
        return total;
    }

    public String getCurrency() {
        return items.isEmpty() ? "USD" : items.get(0).unitPrice().currency();
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void markAsPaid() {
        if (status != OrderStatus.PENDING) {
            throw new InvalidOrderStateException("Only PENDING orders can be marked as paid");
        }
        if (getTotalAmount().amount().compareTo(MIN_PAID_THRESHOLD) < 0) {
            throw new InvalidOrderStateException("Order total is below minimum threshold for payment");
        }
        this.status = OrderStatus.PAID;
    }

    public void ship() {
        if (status != OrderStatus.PAID) {
            throw new InvalidOrderStateException("Only PAID orders can be shipped");
        }
        this.status = OrderStatus.SHIPPED;
    }

    public void cancel() {
        if (status == OrderStatus.SHIPPED) {
            throw new InvalidOrderStateException("Shipped orders cannot be cancelled");
        }
        this.status = OrderStatus.CANCELLED;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Order order = (Order) o;
        return Objects.equals(id, order.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}

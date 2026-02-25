package com.example.order_management.domain.model;

import com.example.order_management.domain.exception.InvalidItemException;

import java.util.Objects;
import java.util.UUID;

public final class OrderItem {

    private final UUID productId;
    private final int quantity;
    private final Money unitPrice;

    public OrderItem(UUID productId, int quantity, Money unitPrice) {
        if (productId == null) {
            throw new InvalidItemException("Product ID cannot be null");
        }
        if (quantity <= 0) {
            throw new InvalidItemException("Quantity must be positive");
        }
        if (unitPrice == null || unitPrice.amount().compareTo(java.math.BigDecimal.ZERO) < 0) {
            throw new InvalidItemException("Unit price must be non-negative");
        }
        this.productId = productId;
        this.quantity = quantity;
        this.unitPrice = unitPrice;
    }

    public UUID productId() {
        return productId;
    }

    public int quantity() {
        return quantity;
    }

    public Money unitPrice() {
        return unitPrice;
    }

    public Money subTotal() {
        return unitPrice.multiply(quantity);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OrderItem orderItem = (OrderItem) o;
        return quantity == orderItem.quantity
            && Objects.equals(productId, orderItem.productId)
            && Objects.equals(unitPrice.amount(), orderItem.unitPrice.amount())
            && Objects.equals(unitPrice.currency(), orderItem.unitPrice.currency());
    }

    @Override
    public int hashCode() {
        return Objects.hash(productId, quantity, unitPrice.amount(), unitPrice.currency());
    }
}

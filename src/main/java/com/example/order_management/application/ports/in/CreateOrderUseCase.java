package com.example.order_management.application.ports.in;

import com.example.order_management.domain.model.Order;

import java.util.List;
import java.util.UUID;

public interface CreateOrderUseCase {

    /**
     * DTO for creating an order item from API.
     */
    record CreateOrderItemRequest(UUID productId, int quantity, java.math.BigDecimal unitPrice) {}

    /**
     * Result of order creation.
     */
    record CreateOrderResult(UUID orderId, String status, java.math.BigDecimal totalAmount, java.time.Instant createdAt) {}

    CreateOrderResult create(UUID customerId, List<CreateOrderItemRequest> items);
}

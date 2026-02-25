package com.example.order_management.application.ports.in;

import com.example.order_management.domain.model.Order;
import com.example.order_management.domain.model.OrderItem;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

public interface GetOrderUseCase {

    record OrderItemDto(UUID productId, int quantity, BigDecimal unitPrice) {}

    record OrderDetailResult(UUID orderId, UUID customerId, String status, List<OrderItemDto> items,
                             BigDecimal totalAmount, String currency) {}

    OrderDetailResult getById(UUID orderId);
}

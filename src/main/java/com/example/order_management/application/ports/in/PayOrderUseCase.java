package com.example.order_management.application.ports.in;

import java.util.UUID;

public interface PayOrderUseCase {

    record PayOrderResult(UUID orderId, String status) {}

    PayOrderResult pay(UUID orderId);
}

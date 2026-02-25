package com.example.order_management.application.ports.out;

import com.example.order_management.domain.model.Order;

import java.util.Optional;
import java.util.UUID;

public interface LoadOrderPort {

    Optional<Order> findById(UUID orderId);
}

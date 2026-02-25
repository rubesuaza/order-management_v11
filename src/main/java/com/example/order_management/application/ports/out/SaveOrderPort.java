package com.example.order_management.application.ports.out;

import com.example.order_management.domain.model.Order;

public interface SaveOrderPort {

    Order save(Order order);
}

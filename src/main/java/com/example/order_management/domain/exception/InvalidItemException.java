package com.example.order_management.domain.exception;

public class InvalidItemException extends RuntimeException {

    public InvalidItemException(String message) {
        super(message);
    }
}

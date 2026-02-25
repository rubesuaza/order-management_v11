package com.example.order_management.domain.model;

import com.example.order_management.domain.exception.CurrencyMismatchException;

import java.math.BigDecimal;
import java.util.Objects;

public final class Money {

    private static final String DEFAULT_CURRENCY = "USD";

    private final BigDecimal amount;
    private final String currency;

    public Money(BigDecimal amount, String currency) {
        this.amount = amount != null ? amount : BigDecimal.ZERO;
        this.currency = currency != null && !currency.isBlank() ? currency : DEFAULT_CURRENCY;
    }

    public BigDecimal amount() {
        return amount;
    }

    public String currency() {
        return currency;
    }

    public Money add(Money other) {
        if (!Objects.equals(this.currency, other.currency)) {
            throw new CurrencyMismatchException(
                "Cannot add amounts with different currencies: " + currency + " and " + other.currency);
        }
        return new Money(this.amount.add(other.amount), this.currency);
    }

    public Money multiply(int factor) {
        return new Money(this.amount.multiply(BigDecimal.valueOf(factor)), this.currency);
    }
}

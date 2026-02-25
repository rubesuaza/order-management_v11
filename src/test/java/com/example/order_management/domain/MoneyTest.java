package com.example.order_management.domain;

import com.example.order_management.domain.exception.CurrencyMismatchException;
import com.example.order_management.domain.model.Money;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class MoneyTest {

    @Test
    void addingMoneyWithDifferentCurrenciesShouldThrowCurrencyMismatchException() {
        Money usd = new Money(new BigDecimal("10.00"), "USD");
        Money eur = new Money(new BigDecimal("5.00"), "EUR");

        assertThrows(CurrencyMismatchException.class, () -> usd.add(eur));
    }

    @Test
    void addingMoneyWithSameCurrencyShouldSucceed() {
        Money first = new Money(new BigDecimal("10.00"), "USD");
        Money second = new Money(new BigDecimal("5.50"), "USD");

        Money result = first.add(second);

        assertEquals(new BigDecimal("15.50"), result.amount());
        assertEquals("USD", result.currency());
    }

    @Test
    void multiplyingMoneyByIntegerShouldKeepCurrencyAndScale() {
        Money unitPrice = new Money(new BigDecimal("2.50"), "USD");

        Money total = unitPrice.multiply(3);

        assertEquals(new BigDecimal("7.50"), total.amount());
        assertEquals("USD", total.currency());
    }

    @Test
    void defaultCurrencyShouldBeUsdWhenNotProvided() {
        Money amount = new Money(new BigDecimal("3.00"), null);

        assertEquals("USD", amount.currency());
    }
}


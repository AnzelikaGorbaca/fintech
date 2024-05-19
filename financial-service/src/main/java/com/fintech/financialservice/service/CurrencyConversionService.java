package com.fintech.financialservice.service;

import com.fintech.financialservice.util.enums.Currency;

import java.math.BigDecimal;

/**
 * Service to handle currency conversion
 */
public interface CurrencyConversionService {

    /**
     * Converts a given amount from one currency to another using the exchange rates.
     *
     * @param fromCurrency The currency from which to convert.
     * @param toCurrency   The currency to which to convert.
     * @param amount       The amount to be converted.
     * @return The converted amount.
     */
    BigDecimal convert(Currency fromCurrency, Currency toCurrency, BigDecimal amount);
}

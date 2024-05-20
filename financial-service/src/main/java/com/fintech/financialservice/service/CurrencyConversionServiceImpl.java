package com.fintech.financialservice.service;

import com.fintech.financialservice.error.ErrorClassification;
import com.fintech.financialservice.error.exception.FinancialServiceException;
import com.fintech.financialservice.external.ExchangeRatesApi;
import com.fintech.financialservice.util.enums.Currency;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Map;

/**
 * Service to handle currency conversion using external exchange rate data.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class CurrencyConversionServiceImpl implements CurrencyConversionService {

    private final ExchangeRatesApi exchangeRatesApi;

    @Override
    public BigDecimal convert(Currency fromCurrency, Currency toCurrency, BigDecimal amount) {
        Map<String, BigDecimal> exchangeRates = getExchangeRates(fromCurrency);
        BigDecimal rate = exchangeRates.get(toCurrency.getSymbol());

        if (rate == null) {
            throw new FinancialServiceException(ErrorClassification.FINANCIALSERVICE_INTERNAL_SERVER_ERROR,
                    String.format("Exchange rate not found from %s to %s", fromCurrency, toCurrency));
        }

        BigDecimal convertedAmount = amount.multiply(rate).setScale(2, RoundingMode.HALF_EVEN);
        log.info("Converted amount from {} {} to {} is {}", amount, fromCurrency, toCurrency, convertedAmount);
        return convertedAmount;
    }

    /**
     * Helper method to retrieve exchange rates from the external API.
     *
     * @param fromCurrency The base currency for which to fetch exchange rates.
     * @return A map of currency symbols to their corresponding exchange rates.
     * @throws FinancialServiceException If the exchange rates data is empty or unavailable.
     */

    private Map<String, BigDecimal> getExchangeRates(Currency fromCurrency) {
        Map<String, BigDecimal> exchangeRates = exchangeRatesApi.getExchangeRates(fromCurrency.getSymbol());

        if (exchangeRates.isEmpty()) {
            throw new FinancialServiceException(
                    ErrorClassification.FINANCIALSERVICE_INTERNAL_SERVER_ERROR,
                    "Exchange rates data is empty or not available.");
        }
        return exchangeRates;
    }
}

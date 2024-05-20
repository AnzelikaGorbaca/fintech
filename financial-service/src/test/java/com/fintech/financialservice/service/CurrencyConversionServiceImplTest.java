package com.fintech.financialservice.service;

import com.fintech.financialservice.error.exception.FinancialServiceException;
import com.fintech.financialservice.external.ExchangeRatesApi;
import com.fintech.financialservice.util.enums.Currency;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.HashMap;
import java.util.Map;

import static com.fintech.financialservice.util.enums.Currency.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CurrencyConversionServiceImplTest {

    @Mock
    private ExchangeRatesApi exchangeRatesApi;

    @InjectMocks
    private CurrencyConversionServiceImpl currencyConversionService;

    private Map<String, BigDecimal> mockExchangeRates;

    @BeforeEach
    void setUp() {
        mockExchangeRates = new HashMap<>();
        mockExchangeRates.put(Currency.EUR.getSymbol(), new BigDecimal("0.85"));
        mockExchangeRates.put(USD.getSymbol(), BigDecimal.ONE);
    }

    @Test
    @DisplayName("Convert amounts successfully from USD to EUR")
    void testConvertSuccessFromUSDToEUR() {
        BigDecimal amount = BigDecimal.valueOf(100);

        when(exchangeRatesApi.getExchangeRates(USD.getSymbol())).thenReturn(mockExchangeRates);

        BigDecimal result = currencyConversionService.convert(USD, EUR, amount);
        BigDecimal expectedConvertedAmount = amount.multiply(new BigDecimal("0.85"))
                .setScale(2, RoundingMode.HALF_EVEN);

        assertNotNull(result);
        assertEquals(expectedConvertedAmount, result);
    }

    @Test
    @DisplayName("Throw exception when exchange rate is missing")
    void testConvertThrowsWhenRateMissing() {
        BigDecimal amount = BigDecimal.valueOf(50);

        when(exchangeRatesApi.getExchangeRates(USD.getSymbol())).thenReturn(mockExchangeRates);

        Exception exception = assertThrows(FinancialServiceException.class, () -> currencyConversionService.convert(USD, JPY, amount));

        assertEquals("Exchange rate not found from USD to JPY", exception.getMessage());
    }

    @Test
    @DisplayName("Throw exception when exchange rates data is empty or null")
    void testConvertThrowsWhenExchangeRatesDataIsEmpty() {
        BigDecimal amount = BigDecimal.valueOf(100);

        when(exchangeRatesApi.getExchangeRates(anyString())).thenReturn(new HashMap<>());

        Exception exception = assertThrows(FinancialServiceException.class, () -> currencyConversionService.convert(USD, EUR, amount));

        assertEquals("Exchange rates data is empty or not available.", exception.getMessage());
    }
}

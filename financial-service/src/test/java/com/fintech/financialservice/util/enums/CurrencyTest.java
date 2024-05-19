package com.fintech.financialservice.util.enums;

import com.fintech.financialservice.error.exception.FinancialServiceException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static com.fintech.financialservice.util.enums.Currency.USD;
import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class CurrencyTest {

    @Test
    void testGetCurrencyValidCode() {
        assertEquals(USD, Currency.getCurrency("USD"));
    }


    @ParameterizedTest
    @CsvSource({
            "eur, EUR",
            "eUr, EUR",
            "gBp, GBP"
    })
    @DisplayName("Test getCurrency with case insensitivity")
    void testGetCurrencyCaseInsensitivity(String inputCurrency, Currency expectedCurrency) {
        assertEquals(expectedCurrency, Currency.getCurrency(inputCurrency));
    }

    @Test
    void testGetCurrencyInvalidCode() {
        Exception exception = assertThrows(FinancialServiceException.class, () -> Currency.getCurrency("XYZ"));
        assertTrue(exception.getMessage().contains("Currency is invalid or not supported"));
    }
}

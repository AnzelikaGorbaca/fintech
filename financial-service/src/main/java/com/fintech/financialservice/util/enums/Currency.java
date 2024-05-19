package com.fintech.financialservice.util.enums;

import com.fintech.financialservice.error.exception.FinancialServiceException;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.fintech.financialservice.error.ErrorClassification.FINANCIALSERVICE_INVALID_REQUEST_ERROR;

@Getter
@AllArgsConstructor
public enum Currency {

    USD("USD"),
    EUR("EUR"),
    GBP("GBP"),
    JPY("JPY"),
    AED("AED"),
    AUD("AUD"),
    CAD("CAD"),
    CHF("CHF"),
    CNY("CNY"),
    SEK("SEK"),
    NZD("NZD");

    private final String symbol;

    public static String getSymbols() {
        return Stream.of(Currency.values())
                .map(e -> e.symbol)
                .collect(Collectors.joining(","));
    }

    public static Currency getCurrency(String currency) {
        return Stream.of(Currency.values())
                .filter(c -> c.name().equalsIgnoreCase(currency))
                .findFirst()
                .orElseThrow(() ->
                        new FinancialServiceException(
                                FINANCIALSERVICE_INVALID_REQUEST_ERROR,
                                "Currency is invalid or not supported: " + currency));
    }
}

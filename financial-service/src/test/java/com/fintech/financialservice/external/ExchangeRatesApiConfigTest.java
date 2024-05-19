package com.fintech.financialservice.external;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(classes = ExchangeRatesApiConfig.class)
@TestPropertySource(properties = {
        "exchange.rate.api.url=http://example.com/api/rates",
        "exchange.rate.api.key=test-api-key"
})
class ExchangeRatesApiConfigTest {

    @Autowired
    private ExchangeRatesApiConfig exchangeRatesApiConfig;

    @Test
    void propertiesAreMappedCorrectly() {
        assertAll("Check all properties of ExchangeRatesApiConfig",
                () -> assertEquals("http://example.com/api/rates", exchangeRatesApiConfig.getApiUrl(), "API URL mismatch"),
                () -> assertEquals("test-api-key", exchangeRatesApiConfig.getApiKey(), "API Key mismatch")
        );
    }
}

package com.fintech.financialservice.external;

import com.fintech.financialservice.error.exception.ExchangeRatesApiException;
import com.fintech.financialservice.external.response.ExchangeRateResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.concurrent.ConcurrentMapCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.retry.annotation.EnableRetry;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import static com.fintech.financialservice.util.enums.Currency.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.http.HttpMethod.GET;

@ExtendWith(MockitoExtension.class)
@SpringBootTest
@ContextConfiguration(classes = {ExchangeRatesApiTest.CacheConfig.class, ExchangeRatesApi.class})
@EnableCaching
@EnableRetry
class ExchangeRatesApiTest {

    @MockBean
    private RestTemplate restTemplate;

    @Autowired
    private ExchangeRatesApi exchangeRatesApi;

    @MockBean
    private ExchangeRatesApiConfig exchangeRatesApiConfig;

    @Autowired
    private CacheManager cacheManager;

    @BeforeEach
    void setUp() {
        when(exchangeRatesApiConfig.getApiKey()).thenReturn("test-api-key");
        when(exchangeRatesApiConfig.getApiUrl()).thenReturn("https://api.exchangerate.host/latest");
        cacheManager.getCacheNames()
                .forEach(cacheName -> Objects.requireNonNull(cacheManager.getCache(cacheName)).clear());
    }

    @Configuration
    public static class CacheConfig {
        @Bean
        public CacheManager cacheManager() {
            return new ConcurrentMapCacheManager("exchangeRates");
        }
    }

    @Test
    @DisplayName("Test successful exchange rates fetch")
    void testSuccessfulExchangeRatesFetch() {
        Map<String, BigDecimal> rates = new HashMap<>();
        rates.put(EUR.getSymbol(), BigDecimal.valueOf(0.85));
        rates.put(GBP.getSymbol(), BigDecimal.valueOf(0.75));

        ExchangeRateResponse mockResponse = new ExchangeRateResponse();
        mockResponse.setRates(rates);
        mockResponse.setSuccess(true);

        when(restTemplate.exchange(
                any(String.class),
                any(),
                any(),
                eq(ExchangeRateResponse.class))
        ).thenReturn(new ResponseEntity<>(mockResponse, HttpStatus.OK));

        Map<String, BigDecimal> fetchedRates = exchangeRatesApi.getExchangeRates(USD.getSymbol());

        assertNotNull(fetchedRates);
        assertEquals(BigDecimal.valueOf(0.85), fetchedRates.get(EUR.getSymbol()));
        assertEquals(BigDecimal.valueOf(0.75), fetchedRates.get(GBP.getSymbol()));
    }

    @Test
    @DisplayName("Test failed exchange rates fetch")
    void testFailedExchangeRatesFetch() {
        ExchangeRateResponse mockResponse = new ExchangeRateResponse();
        mockResponse.setSuccess(false);

        when(restTemplate.exchange(
                any(String.class),
                any(),
                any(),
                eq(ExchangeRateResponse.class))
        ).thenReturn(new ResponseEntity<>(mockResponse, HttpStatus.BAD_REQUEST));

        assertThrows(ExchangeRatesApiException.class, () -> exchangeRatesApi.getExchangeRates(USD.getSymbol()));
    }

    @Test
    @DisplayName("Test exception on external API call failure")
    void testExceptionOnApiCallFailure() {
        when(restTemplate.exchange(
                any(String.class),
                any(),
                any(),
                eq(ExchangeRateResponse.class))
        ).thenThrow(new RuntimeException("Failed to call external API"));

        assertThrows(ExchangeRatesApiException.class, () -> exchangeRatesApi.getExchangeRates(USD.getSymbol()));
    }

    @Test
    @DisplayName("Test caching behavior")
    void testCachingBehavior() {
        Map<String, BigDecimal> rates = new HashMap<>();
        rates.put("EUR", BigDecimal.valueOf(0.85));

        ExchangeRateResponse mockResponse = new ExchangeRateResponse();
        mockResponse.setRates(rates);
        mockResponse.setSuccess(true);

        when(restTemplate.exchange(
                any(String.class),
                any(),
                any(),
                eq(ExchangeRateResponse.class))
        ).thenReturn(new ResponseEntity<>(mockResponse, HttpStatus.OK));

        Map<String, BigDecimal> fetchedRates1 = exchangeRatesApi.getExchangeRates(USD.getSymbol());
        assertNotNull(fetchedRates1);

        Map<String, BigDecimal> fetchedRates2 = exchangeRatesApi.getExchangeRates(USD.getSymbol());
        assertNotNull(fetchedRates2);

        verify(restTemplate, times(1)).exchange(
                any(String.class),
                any(),
                any(),
                eq(ExchangeRateResponse.class)
        );
    }

    @Test
    void testRetryMechanism() {
        ExchangeRateResponse failedResponse = new ExchangeRateResponse();
        failedResponse.setSuccess(false);

        ExchangeRateResponse successfulResponse = new ExchangeRateResponse();
        successfulResponse.setSuccess(true);

        successfulResponse.setRates(Map.of(EUR.getSymbol(), BigDecimal.valueOf(0.85),
                GBP.getSymbol(), BigDecimal.valueOf(0.75)));

        when(restTemplate.exchange(
                any(String.class),
                eq(GET),
                any(HttpEntity.class),
                eq(ExchangeRateResponse.class)))
                .thenReturn(new ResponseEntity<>(failedResponse, HttpStatus.OK))
                .thenReturn(new ResponseEntity<>(failedResponse, HttpStatus.OK))
                .thenReturn(new ResponseEntity<>(successfulResponse, HttpStatus.OK));

        Map<String, BigDecimal> rates = exchangeRatesApi.getExchangeRates(USD.getSymbol());

        assertNotNull(rates);
        assertFalse(rates.isEmpty());
        assertEquals(BigDecimal.valueOf(0.85), rates.get(EUR.getSymbol()));

        verify(restTemplate, times(3)).exchange(
                any(String.class),
                eq(GET),
                any(HttpEntity.class),
                eq(ExchangeRateResponse.class));
    }
}

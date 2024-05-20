package com.fintech.financialservice.external;

import com.fintech.financialservice.error.exception.ExchangeRatesApiException;
import com.fintech.financialservice.external.response.ExchangeRateResponse;
import com.fintech.financialservice.util.enums.Currency;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.math.BigDecimal;
import java.util.Map;

import static org.springframework.http.HttpMethod.GET;

@Component
@RequiredArgsConstructor
@Slf4j
public class ExchangeRatesApi {

    private final ExchangeRatesApiConfig exchangeRatesApiConfig;
    private final RestTemplate restTemplate;

    @Cacheable(value = "exchangeRates", key = "#baseCurrency")
    @Retryable(value = {ExchangeRatesApiException.class}, maxAttempts = 4, backoff = @Backoff(delay = 2000))
    public Map<String, BigDecimal> getExchangeRates(String baseCurrency) {
        log.info("Fetching exchange rates from external API. Base currency: {}", baseCurrency);

        final HttpEntity<String> entity = generateHeaders();
        final String url = buildUrl(baseCurrency);

        try {
            ExchangeRateResponse exchangeRateResponse = restTemplate
                    .exchange(url, GET, entity, ExchangeRateResponse.class).getBody();

            if (exchangeRatesResponseIsValid(exchangeRateResponse)) {
                return exchangeRateResponse.getRates();
            } else {
                log.error("Failed to fetch exchange rates, response: {}", exchangeRateResponse);
                throw new ExchangeRatesApiException("Failed to fetch exchange rates from external service.");
            }
        } catch (Exception e) {
            throw new ExchangeRatesApiException(e.getMessage());
        }
    }

    private String buildUrl(String baseCurrency) {
        return UriComponentsBuilder.fromHttpUrl(exchangeRatesApiConfig.getApiUrl())
                .queryParam("base", baseCurrency)
                .queryParam("symbols", String.join(",", Currency.getSymbols()))
                .toUriString();
    }

    private HttpEntity<String> generateHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.set("apikey", exchangeRatesApiConfig.getApiKey());
        return new HttpEntity<>(headers);
    }

    private boolean exchangeRatesResponseIsValid(ExchangeRateResponse exchangeRateResponse) {
        return (exchangeRateResponse != null && exchangeRateResponse.isSuccess()
                && exchangeRateResponse.getRates() != null
                && !exchangeRateResponse.getRates().isEmpty());
    }
}

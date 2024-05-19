package com.fintech.financialservice.external.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.Map;

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class ExchangeRateResponse {

    private String base;
    private String date;

    @JsonProperty("rates")
    private Map<String, BigDecimal> rates;
    private boolean success;
    private long timestamp;
}

package com.fintech.financialservice.error.exception;


import lombok.Getter;

import static com.fintech.financialservice.error.ErrorClassification.EXCHANGERATES_API_RESPONSE_ERROR;

@Getter
public class ExchangeRatesApiException extends ApiException {

    public ExchangeRatesApiException(String message) {
        super(EXCHANGERATES_API_RESPONSE_ERROR, message);
    }
}

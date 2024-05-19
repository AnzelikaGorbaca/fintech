package com.fintech.financialservice.error.exception;

import com.fintech.financialservice.error.ErrorClassification;
import lombok.Getter;

@Getter
public abstract class ApiException extends RuntimeException {

    private final ErrorClassification errorClassification;

    protected ApiException(ErrorClassification errorClassification, String message) {
        super(message);
        this.errorClassification = errorClassification;
    }
}

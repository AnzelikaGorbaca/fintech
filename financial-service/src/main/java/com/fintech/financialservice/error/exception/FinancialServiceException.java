package com.fintech.financialservice.error.exception;

import com.fintech.financialservice.error.ErrorClassification;
import lombok.Getter;

@Getter
public class FinancialServiceException extends ApiException {

    public FinancialServiceException(ErrorClassification errorClassification, String message) {
        super(errorClassification, message);
    }

    public FinancialServiceException(ErrorClassification errorClassification) {
        super(errorClassification, errorClassification.getMessage());
    }
}

package com.fintech.financialservice.error;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

import static org.springframework.http.HttpStatus.*;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public enum ErrorClassification {
    FINANCIALSERVICE_INTERNAL_SERVER_ERROR(1, INTERNAL_SERVER_ERROR, "Internal Server Error"),
    FINANCIALSERVICE_INVALID_REQUEST_ERROR(2, BAD_REQUEST, "Missing Required or wrong parameter values"),
    FINANCIALSERVICE_PATH_NOT_FOUND_ERROR(3, NOT_FOUND, "Path Not Found"),
    FINANCIALSERVICE_DB_ERROR(5, INTERNAL_SERVER_ERROR, "Database Error"),
    FINANCIALSERVICE_CLIENT_NOT_FOUND(6, NOT_FOUND, "Client with provided Id not found"),
    FINANCIALSERVICE_FAILED_TO_EXECUTE_REQUEST(7, BAD_REQUEST, "Request to external API failed"),

    EXCHANGERATES_API_RESPONSE_ERROR(8, INTERNAL_SERVER_ERROR, "Error from External Exchange Rates API");


    public final int errorCode;
    public final HttpStatus status;
    public final String message;
}

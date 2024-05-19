package com.fintech.financialservice.error;

import com.fintech.financialservice.error.exception.ApiException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.NoHandlerFoundException;

import static com.fintech.financialservice.error.ErrorClassification.*;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
import static org.springframework.http.HttpStatus.NOT_FOUND;

@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(ApiException.class)
    public ResponseEntity<ErrorResponse> handleCustomException(ApiException ex) {
        return ResponseEntity.status(ex.getErrorClassification().getStatus())
                .body(new ErrorResponse(
                        ex.getErrorClassification().getErrorCode(),
                        ex.getMessage()));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleUnknownException(Exception ex) {
        return ResponseEntity.status(INTERNAL_SERVER_ERROR)
                .body(new ErrorResponse(
                        FINANCIALSERVICE_INTERNAL_SERVER_ERROR.errorCode, !ex.getMessage().isBlank()
                        ? ex.getMessage()
                        : FINANCIALSERVICE_INTERNAL_SERVER_ERROR.message));
    }

    @ExceptionHandler(NoHandlerFoundException.class)
    public ResponseEntity<ErrorResponse> handleNoHandlerFoundException() {
        return ResponseEntity.status(NOT_FOUND)
                .body(new ErrorResponse(
                        FINANCIALSERVICE_PATH_NOT_FOUND_ERROR.errorCode,
                        FINANCIALSERVICE_PATH_NOT_FOUND_ERROR.message));
    }

    @ExceptionHandler(DataAccessException.class)
    public ResponseEntity<ErrorResponse> handleDataAccessException(DataAccessException ex) {
        log.error("Database Error Occurred: {}", ex.getMessage(), ex);
        return ResponseEntity.status(INTERNAL_SERVER_ERROR)
                .body(new ErrorResponse(
                        FINANCIALSERVICE_DB_ERROR.errorCode,
                        FINANCIALSERVICE_DB_ERROR.message));
    }
}

package com.fintech.financialservice.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fintech.financialservice.dto.request.TransferRequest;
import com.fintech.financialservice.dto.response.TransferResponse;
import com.fintech.financialservice.error.exception.ExchangeRatesApiException;
import com.fintech.financialservice.error.exception.FinancialServiceException;
import com.fintech.financialservice.rest.controller.TransferController;
import com.fintech.financialservice.service.TransferService;
import com.fintech.financialservice.util.enums.Currency;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.Objects;

import static com.fintech.financialservice.FinancialServiceTestHelper.*;
import static com.fintech.financialservice.error.ErrorClassification.FINANCIALSERVICE_INVALID_REQUEST_ERROR;
import static com.fintech.financialservice.util.enums.Currency.USD;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest(TransferController.class)
class TransferControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TransferService transferService;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    public void setUp() {
        reset(transferService);
    }

    @Test
    @DisplayName("POST /accounts/transfer - Success")
    void testTransferFundsSuccess() throws Exception {
        TransferResponse expectedResponse = new TransferResponse(
                buildTransactionDTO(
                        ACCOUNT_1_ID,
                        ACCOUNT_2_ID,
                        100,
                        USD
                )
        );

        when(transferService.transferFunds(ACCOUNT_1_ID, ACCOUNT_2_ID, BigDecimal.valueOf(100.00), USD))
                .thenReturn(expectedResponse);

        mockMvc.perform(post("/api/accounts/transfer")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(buildValidTransferRequest())))
                .andExpect(status().isCreated());

        verify(transferService, times(1)).transferFunds(
                anyLong(), anyLong(), any(BigDecimal.class), any(Currency.class));
    }

    @Test
    @DisplayName("POST /accounts/transfer - Service mandatory field validation failure as Internal Server error")
    void testTransferFundsValidationFailureMissingMandatoryField() throws Exception {
        performPostRequestAndExpectInternalServerError(TransferRequest.builder()
                .fromAccountId(ACCOUNT_1_ID)
                .toAccountId(ACCOUNT_2_ID)
                .amount(BigDecimal.valueOf(-100))
                .transferCurrency(USD.getSymbol())
                .build());
    }

    @Test
    @DisplayName("POST /accounts/transfer - Service handle amount validation failure as Internal Server error")
    void testNegativeAmount() throws Exception {
        performPostRequestAndExpectInternalServerError(TransferRequest.builder()
                .fromAccountId(ACCOUNT_1_ID)
                .toAccountId(ACCOUNT_2_ID)
                .amount(BigDecimal.valueOf(-100))
                .transferCurrency(USD.getSymbol())
                .build());
    }
    @Test
    @DisplayName("POST /accounts/transfer - Custom FinancialService Exception Returned from Service")
    void testCustomApiFinancialServiceException() throws Exception {
        when(transferService.transferFunds(anyLong(), anyLong(), any(BigDecimal.class), any(Currency.class)))
                .thenThrow(new FinancialServiceException(FINANCIALSERVICE_INVALID_REQUEST_ERROR));

        mockMvc.perform(post("/api/accounts/transfer")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(buildValidTransferRequest())))
                .andExpect(status().isBadRequest())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof FinancialServiceException))
                .andExpect(result -> assertEquals(FINANCIALSERVICE_INVALID_REQUEST_ERROR.message,
                        Objects.requireNonNull(result.getResolvedException()).getMessage()));
    }

    @Test
    @DisplayName("POST /accounts/transfer - Custom ExchangeRatesApi Exception Returned from Service")
    void testCustomApiExchangeRatesException() throws Exception {
        String errorMessage = "External Api Error Message";

        when(transferService.transferFunds(anyLong(), anyLong(), any(BigDecimal.class), any(Currency.class)))
                .thenThrow(new ExchangeRatesApiException(errorMessage));

        mockMvc.perform(post("/api/accounts/transfer")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(buildValidTransferRequest())))
                .andExpect(status().isInternalServerError())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof ExchangeRatesApiException))
                .andExpect(result -> assertEquals(errorMessage, Objects.requireNonNull(result.getResolvedException()).getMessage()));
    }

    private void performPostRequestAndExpectInternalServerError(TransferRequest request) throws Exception {
        mockMvc.perform(post("/api/accounts/transfer")
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isInternalServerError())
                .andExpect(result -> assertNotNull(Objects.requireNonNull(result.getResolvedException()).getMessage()));
    }

    private TransferRequest buildValidTransferRequest() {
        return TransferRequest.builder()
                .fromAccountId(ACCOUNT_1_ID)
                .toAccountId(ACCOUNT_2_ID)
                .amount(BigDecimal.valueOf(100))
                .transferCurrency(USD.getSymbol())
                .build();
    }
}

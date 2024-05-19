package com.fintech.financialservice.controller;

import com.fintech.financialservice.dto.AccountDTO;
import com.fintech.financialservice.dto.TransactionDTO;
import com.fintech.financialservice.rest.controller.AccountController;
import com.fintech.financialservice.service.AccountService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static com.fintech.financialservice.FinancialServiceTestHelper.*;
import static com.fintech.financialservice.error.ErrorClassification.FINANCIALSERVICE_INTERNAL_SERVER_ERROR;
import static com.fintech.financialservice.util.enums.Currency.EUR;
import static com.fintech.financialservice.util.enums.Currency.USD;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AccountController.class)
@AutoConfigureMockMvc
class AccountControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AccountService accountService;

    @Captor
    private ArgumentCaptor<Integer> offsetCaptor;

    @Captor
    private ArgumentCaptor<Integer> limitCaptor;

    @BeforeEach
    public void setUp() {
        reset(accountService);
    }

    @Test
    @DisplayName("GET /clients/{clientId}/accounts - Success with accounts")
    void testGetAccountsSuccess() throws Exception {
        Long clientId = CLIENT_1_ID;

        List<AccountDTO> accounts = Arrays.asList(
                buildAccountDTO(ACCOUNT_1_ID, 1000, USD.getSymbol()),
                buildAccountDTO(ACCOUNT_2_ID, 2000, EUR.getSymbol())
        );

        when(accountService.getAccountsByClientId(clientId)).thenReturn(accounts);

        mockMvc.perform(get("/api/clients/{clientId}/accounts", clientId))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.accounts", hasSize(2)))
                .andExpect(jsonPath("$.accounts[0].userAccountId").value(ACCOUNT_1_ID))
                .andExpect(jsonPath("$.accounts[0].currency").value(USD.getSymbol()))
                .andExpect(jsonPath("$.accounts[0].balance").value(1000.00))
                .andExpect(jsonPath("$.accounts[1].userAccountId").value(ACCOUNT_2_ID))
                .andExpect(jsonPath("$.accounts[1].currency").value(EUR.getSymbol()))
                .andExpect(jsonPath("$.accounts[1].balance").value(2000.00))
                .andDo(print());

        verify(accountService).getAccountsByClientId(clientId);
    }

    @Test
    @DisplayName("GET /clients/{clientId}/accounts - Success without accounts")
    void testGetAccountsEmpty() throws Exception {
        Long clientId = CLIENT_1_ID;

        when(accountService.getAccountsByClientId(clientId)).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/api/clients/{clientId}/accounts", clientId))
                .andExpect(status().isNoContent())
                .andDo(print());

        verify(accountService).getAccountsByClientId(clientId);
    }

    @Test
    @DisplayName("GET /accounts/{accountId}/transactions - Success with transactions")
    void testGetTransactionsSuccess() throws Exception {
        Long accountId = ACCOUNT_1_ID;

        List<TransactionDTO> transactions = Arrays.asList(
                buildTransactionDTO(ACCOUNT_1_ID, ACCOUNT_2_ID, 100, USD),
                buildTransactionDTO(ACCOUNT_1_ID, ACCOUNT_3_ID, 200, EUR)
        );

        when(accountService.getTransactionsByAccountId(accountId, DEFAULT_OFFSET, DEFAULT_LIMIT))
                .thenReturn(transactions);

        mockMvc.perform(get("/api/accounts/{accountId}/transactions", accountId)
                        .param(PATH_PARAM_OFFSET, String.valueOf(DEFAULT_OFFSET))
                        .param(PATH_PARAM_LIMIT, String.valueOf(DEFAULT_LIMIT)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.transactions", hasSize(2)))
                .andExpect(jsonPath("$.transactions[0].userAccountId").value(ACCOUNT_1_ID))
                .andExpect(jsonPath("$.transactions[0].transactionAccountId").value(ACCOUNT_2_ID))
                .andExpect(jsonPath("$.transactions[0].amount").value(100.00))
                .andExpect(jsonPath("$.transactions[0].transactionCurrency").value(USD.getSymbol()))
                .andExpect(jsonPath("$.transactions[1].userAccountId").value(ACCOUNT_1_ID))
                .andExpect(jsonPath("$.transactions[1].transactionAccountId").value(ACCOUNT_3_ID))
                .andExpect(jsonPath("$.transactions[1].amount").value(200.00))
                .andExpect(jsonPath("$.transactions[1].transactionCurrency").value(EUR.getSymbol()))
                .andDo(print());

        verify(accountService).getTransactionsByAccountId(accountId, DEFAULT_OFFSET, DEFAULT_LIMIT);
    }

    @Test
    @DisplayName("GET /accounts/{accountId}/transactions - Success without transactions")
    void testGetTransactionsEmpty() throws Exception {
        Long accountId = ACCOUNT_1_ID;

        when(accountService.getTransactionsByAccountId(accountId, DEFAULT_OFFSET, DEFAULT_LIMIT))
                .thenReturn(Collections.emptyList());

        mockMvc.perform(get("/api/accounts/{accountId}/transactions", accountId)
                        .param(PATH_PARAM_OFFSET, String.valueOf(DEFAULT_OFFSET))
                        .param(PATH_PARAM_LIMIT, String.valueOf(DEFAULT_LIMIT)))
                .andExpect(status().isNoContent())
                .andDo(print());

        verify(accountService).getTransactionsByAccountId(accountId, DEFAULT_OFFSET, DEFAULT_LIMIT);
    }

    @Test
    @DisplayName("GET /clients/{clientId}/accounts - Error returned from service")
    void testGetAccountsServiceThrowsException() throws Exception {
        String errorMessage = "Unexpected Error";
        when(accountService.getAccountsByClientId(CLIENT_1_ID))
                .thenThrow(new RuntimeException(errorMessage));

        mockMvc.perform(get("/api/clients/{clientId}/accounts", CLIENT_1_ID))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.errorCode").value(FINANCIALSERVICE_INTERNAL_SERVER_ERROR.errorCode))
                .andExpect(jsonPath("$.errorMessage").value(errorMessage))
                .andDo(print());
    }

    @ParameterizedTest
    @CsvSource({
            "'', '', 0, 10",      // Both 'offset' and 'limit' are missing, expecting defaults
            "2, 20, 2, 20"        // Both 'offset' and 'limit' are provided
    })
    @DisplayName("GET /accounts/{accountId}/transactions - Default and Explicit Parameters")
    void testGetTransactionsUsingDefaultOrProvidedParams(String providedOffset, String providedLimit, int expectedOffset, int expectedLimit) throws Exception {
        when(accountService.getTransactionsByAccountId(eq(ACCOUNT_1_ID), anyInt(), anyInt())).thenReturn(Collections.emptyList());

        String requestPath = String.format("/api/accounts/%d/transactions%s%s",
                ACCOUNT_1_ID,
                providedOffset.isEmpty() ? "" : "?offset=" + providedOffset,
                providedLimit.isEmpty() ? "" : (providedOffset.isEmpty() ? "?limit=" : "&limit=") + providedLimit);

        mockMvc.perform(get(requestPath))
                .andExpect(status().isNoContent())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andDo(print());

        verify(accountService).getTransactionsByAccountId(eq(ACCOUNT_1_ID), offsetCaptor.capture(), limitCaptor.capture());

        assertEquals(expectedOffset, offsetCaptor.getValue());
        assertEquals(expectedLimit, limitCaptor.getValue());
    }
}

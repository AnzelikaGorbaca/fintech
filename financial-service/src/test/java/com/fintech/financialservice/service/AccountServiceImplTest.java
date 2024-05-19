package com.fintech.financialservice.service;

import com.fintech.financialservice.dto.AccountDTO;
import com.fintech.financialservice.dto.TransactionDTO;
import com.fintech.financialservice.error.exception.FinancialServiceException;
import com.fintech.financialservice.persistance.model.Transaction;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static com.fintech.financialservice.FinancialServiceTestHelper.*;
import static com.fintech.financialservice.error.ErrorClassification.FINANCIALSERVICE_CLIENT_NOT_FOUND;
import static com.fintech.financialservice.util.enums.Currency.USD;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AccountServiceImplTest extends ServiceTestBase {

    @InjectMocks
    private AccountServiceImpl accountService;

    @Test
    @DisplayName("Test getAccountsByClientId returns valid account data")
    void testGetAccountsByClientIdReturnsValidData() {
        when(clientRepository.existsById(CLIENT_1_ID)).thenReturn(true);
        when(accountRepository.findByClientId(CLIENT_1_ID)).thenReturn(Arrays.asList(account1, account2));

        List<AccountDTO> result = accountService.getAccountsByClientId(CLIENT_1_ID);

        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals(USD.getSymbol(), result.get(0).getCurrency());
        assertEquals(BigDecimal.valueOf(1000), result.get(0).getBalance());

        verify(clientRepository).existsById(CLIENT_1_ID);
        verify(accountRepository).findByClientId(CLIENT_1_ID);
    }

    @Test
    @DisplayName("Test getAccountsByClientId returns empty list when no accounts found")
    void testGetAccountsByClientIdReturnsEmptyWhenNoAccounts() {
        when(clientRepository.existsById(CLIENT_1_ID)).thenReturn(true);
        when(accountRepository.findByClientId(CLIENT_1_ID)).thenReturn(Collections.emptyList());

        List<AccountDTO> result = accountService.getAccountsByClientId(CLIENT_1_ID);

        assertNotNull(result);
        assertTrue(result.isEmpty());

        verify(clientRepository).existsById(CLIENT_1_ID);
        verify(accountRepository).findByClientId(CLIENT_1_ID);
    }

    @Test
    @DisplayName("Test getAccountsByClientId throws exception for non-existing client")
    void testGetAccountsByClientIdThrowsExceptionForNonExistingClient() {
        Long nonExistingClientId = 10L;

        when(clientRepository.existsById(nonExistingClientId)).thenReturn(false);

        Exception exception = assertThrows(FinancialServiceException.class, () -> accountService.getAccountsByClientId(nonExistingClientId));

        assertEquals(FINANCIALSERVICE_CLIENT_NOT_FOUND.message, exception.getMessage());
    }

    @ParameterizedTest
    @ValueSource(ints = {0, 1, 5})
    @DisplayName("Test getTransactionsByAccountId with different offsets")
    void testGetTransactionsByAccountIdWithDifferentOffsets(int offset) {
        Page<Transaction> page = new PageImpl<>(Arrays.asList(transaction1, transaction2));
        PageRequest pageRequest = PageRequest.of(offset, DEFAULT_LIMIT, Sort.by(Sort.Direction.DESC, "timestamp"));

        when(transactionRepository.findByAccountId(ACCOUNT_1_ID, pageRequest)).thenReturn(page);

        List<TransactionDTO> result = accountService.getTransactionsByAccountId(ACCOUNT_1_ID, offset, DEFAULT_LIMIT);

        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals(BigDecimal.valueOf(100), result.get(0).getAmount());

        verify(transactionRepository).findByAccountId(ACCOUNT_1_ID, pageRequest);
    }

    @Test
    @DisplayName("Test getTransactionsByAccountId returns empty list when no transactions found")
    void testGetTransactionsByAccountIdReturnsEmptyWhenNoTransactions() {
        int offset = 0;

        PageRequest pageRequest = PageRequest.of(offset, DEFAULT_LIMIT, Sort.by(Sort.Direction.DESC, "timestamp"));

        when(transactionRepository.findByAccountId(ACCOUNT_1_ID, pageRequest)).thenReturn(Page.empty());

        List<TransactionDTO> result = accountService.getTransactionsByAccountId(ACCOUNT_1_ID, offset, DEFAULT_LIMIT);

        assertNotNull(result);
        assertTrue(result.isEmpty());

        verify(transactionRepository).findByAccountId(ACCOUNT_1_ID, pageRequest);
    }
}

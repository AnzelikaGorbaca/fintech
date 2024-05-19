package com.fintech.financialservice.service;

import com.fintech.financialservice.dto.response.TransferResponse;
import com.fintech.financialservice.error.exception.FinancialServiceException;
import com.fintech.financialservice.persistance.model.Account;
import com.fintech.financialservice.persistance.model.Transaction;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Optional;

import static com.fintech.financialservice.FinancialServiceTestHelper.ACCOUNT_1_ID;
import static com.fintech.financialservice.FinancialServiceTestHelper.ACCOUNT_2_ID;
import static com.fintech.financialservice.util.enums.Currency.EUR;
import static com.fintech.financialservice.util.enums.Currency.USD;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TransferServiceImplTest extends ServiceTestBase {

    @Mock
    private CurrencyConversionService currencyConversionService;

    @InjectMocks
    private TransferServiceImpl transferService;

    @Test
    @DisplayName("Same currency, valid transfer")
    void testSuccessfulTransferSameCurrency() {
        mockAccountRetrieval();
        account2.setCurrency(USD);

        BigDecimal transferAmount = BigDecimal.valueOf(100);
        BigDecimal initialFromBalance = account1.getBalance();
        BigDecimal initialToBalance = account2.getBalance();

        TransferResponse result = transferService.transferFunds(ACCOUNT_1_ID,
                ACCOUNT_2_ID,
                transferAmount,
                USD);

        assertNotNull(result);
        verifyBalanceUpdates(initialFromBalance, initialToBalance, transferAmount);
        verifyRepositoryOperations();
    }

    @Test
    @DisplayName("Different currency, valid transfer with conversion")
    void testSuccessfulTransferWithConversion() {
        BigDecimal transferAmount = BigDecimal.valueOf(100);
        BigDecimal initialFromBalance = account1.getBalance();
        BigDecimal initialToBalance = account2.getBalance();
        BigDecimal conversionRate = new BigDecimal("0.85");
        BigDecimal convertedAmount = transferAmount.multiply(conversionRate)
                .setScale(2, RoundingMode.HALF_EVEN);

        mockAccountRetrieval();
        when(currencyConversionService.convert(USD, EUR, transferAmount))
                .thenReturn(convertedAmount);

        TransferResponse result = transferService.transferFunds(ACCOUNT_1_ID,
                ACCOUNT_2_ID,
                transferAmount,
                EUR);

        assertNotNull(result);
        verifyBalanceUpdates(initialFromBalance, initialToBalance, convertedAmount);
        verifyRepositoryOperations();
    }

    @Test
    @DisplayName("Throw exception when transfer amount is negative or zero")
    void testTransferWithInvalidAmount() {
        Exception exception = assertThrows(FinancialServiceException.class, () -> transferService.transferFunds(
                ACCOUNT_1_ID,
                ACCOUNT_2_ID,
                BigDecimal.ZERO, USD));

        assertEquals("Transfer amount must be positive.", exception.getMessage());
        verifyNoRepositoryOperations();
    }

    @Test
    @DisplayName("Throw exception for insufficient funds")
    void testInsufficientFunds() {
        BigDecimal amount = BigDecimal.valueOf(1500);
        account2.setCurrency(USD);

        when(accountRepository.findById(ACCOUNT_1_ID))
                .thenReturn(Optional.of(account1));
        when(accountRepository.findById(ACCOUNT_2_ID))
                .thenReturn(Optional.of(account2));

        Exception exception = assertThrows(FinancialServiceException.class, () -> transferService.transferFunds(ACCOUNT_1_ID, ACCOUNT_2_ID, amount, USD));

        assertEquals("Insufficient funds in the source account.", exception.getMessage());
        verifyNoRepositoryOperations();
    }

    @Test
    @DisplayName("Throw exception when account not found")
    void testAccountNotFound() {
        BigDecimal transferAmount = BigDecimal.valueOf(100);

        when(accountRepository.findById(ACCOUNT_1_ID)).thenReturn(Optional.empty());

        Exception exception = assertThrows(FinancialServiceException.class, () -> transferService.transferFunds(ACCOUNT_1_ID, ACCOUNT_2_ID, transferAmount, USD));

        assertEquals("From account not found", exception.getMessage());
        verifyNoRepositoryOperations();
    }

    @Test
    @DisplayName("Throw exception when transfer currency does not match receiver's account currency")
    void testTransferCurrencyMismatch() {
        BigDecimal transferAmount = BigDecimal.valueOf(100);
        mockAccountRetrieval();

        FinancialServiceException exception = assertThrows(FinancialServiceException.class, () -> transferService.transferFunds(ACCOUNT_1_ID, ACCOUNT_2_ID, transferAmount, USD));

        assertEquals(String.format(
                "Transfer currency %s does not match receiver's account currency %s", USD, EUR), exception.getMessage());
        verifyNoRepositoryOperations();
    }

    private void mockAccountRetrieval() {
        when(accountRepository.findById(ACCOUNT_1_ID)).thenReturn(Optional.of(account1));
        when(accountRepository.findById(ACCOUNT_2_ID)).thenReturn(Optional.of(account2));
    }

    private void verifyBalanceUpdates(BigDecimal initialFromBalance, BigDecimal initialToBalance, BigDecimal transferAmount) {
        assertEquals(0, account1.getBalance()
                .compareTo(initialFromBalance.subtract(transferAmount)));
        assertEquals(0, account2.getBalance()
                .compareTo(initialToBalance.add(transferAmount)));
    }

    private void verifyRepositoryOperations() {
        verify(accountRepository).save(account1);
        verify(accountRepository).save(account2);
        verify(transactionRepository, times(2)).save(any(Transaction.class));
    }

    private void verifyNoRepositoryOperations() {
        verify(accountRepository, never()).save(any(Account.class));
        verify(transactionRepository, never()).save(any(Transaction.class));
    }
}

package com.fintech.financialservice.util.mapper;

import com.fintech.financialservice.dto.TransactionDTO;
import com.fintech.financialservice.persistance.model.Account;
import com.fintech.financialservice.persistance.model.Client;
import com.fintech.financialservice.persistance.model.Transaction;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.Instant;

import static com.fintech.financialservice.FinancialServiceTestHelper.*;
import static com.fintech.financialservice.util.enums.Currency.USD;
import static org.junit.jupiter.api.Assertions.*;

class TransactionMapperTest {

    @Test
    @DisplayName("Convert Transaction to TransactionDTO")
    void testToDtoNonNull() {
        Account account = buildAccount(ACCOUNT_1_ID, 1000, new Client(), USD);

        Transaction transaction = new Transaction();
        transaction.setAccount(account);
        transaction.setAmount(BigDecimal.valueOf(1000));
        transaction.setCurrency(USD);
        transaction.setTransactionAccount(ACCOUNT_2_ID);
        transaction.setTimestamp(Instant.parse("2024-05-19T10:15:30.00Z"));

        TransactionDTO result = TransactionMapper.toDTO(transaction);

        assertNotNull(result);
        assertAll("Ensure mapping is correct",
                () -> assertEquals(transaction.getAccount().getId(), result.getAccountId()),
                () -> assertEquals(transaction.getAmount(), result.getAmount()),
                () -> assertEquals(transaction.getCurrency(), result.getCurrency()),
                () -> assertEquals(transaction.getTransactionAccount(), result.getTransactionAccountId()),
                () -> assertEquals(transaction.getTimestamp(), result.getTimestamp())
        );
    }

    @Test
    @DisplayName("Convert a null Transaction to null TransactionDTO")
    void testToDtoNull() {
        assertNull(TransactionMapper.toDTO(null), "The result should be null for null input.");
    }
}

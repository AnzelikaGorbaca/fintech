package com.fintech.financialservice.service;

import com.fintech.financialservice.persistance.model.Account;
import com.fintech.financialservice.persistance.model.Client;
import com.fintech.financialservice.persistance.model.Transaction;
import com.fintech.financialservice.persistance.repository.AccountRepository;
import com.fintech.financialservice.persistance.repository.ClientRepository;
import com.fintech.financialservice.persistance.repository.TransactionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.Mock;

import java.math.BigDecimal;

import static com.fintech.financialservice.FinancialServiceTestHelper.*;
import static com.fintech.financialservice.util.enums.Currency.EUR;
import static com.fintech.financialservice.util.enums.Currency.USD;

public abstract class ServiceTestBase {

    @Mock
    protected AccountRepository accountRepository;

    @Mock
    protected TransactionRepository transactionRepository;

    @Mock
    protected ClientRepository clientRepository;

    protected Account account1, account2;
    protected Transaction transaction1, transaction2;
    protected Client client;

    @BeforeEach
    public void setUpCommon() {
        client = new Client();
        client.setId(CLIENT_1_ID);

        account1 = buildAccount(ACCOUNT_1_ID, 1000, client, USD);
        account2 = buildAccount(ACCOUNT_2_ID, 2000, client, EUR);

        transaction1 = new Transaction(account1, BigDecimal.valueOf(100), USD, 2L);
        transaction2 = new Transaction(account1, BigDecimal.valueOf(200), USD, 2L);
    }
}

package com.fintech.financialservice.util.mapper;

import com.fintech.financialservice.dto.AccountDTO;
import com.fintech.financialservice.persistance.model.Account;
import com.fintech.financialservice.persistance.model.Client;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static com.fintech.financialservice.FinancialServiceTestHelper.ACCOUNT_1_ID;
import static com.fintech.financialservice.FinancialServiceTestHelper.buildAccount;
import static com.fintech.financialservice.util.enums.Currency.USD;
import static com.fintech.financialservice.util.mapper.AccountMapper.toDTO;
import static org.junit.jupiter.api.Assertions.*;

class AccountMapperTest {

    @Test
    @DisplayName("Convert Account to AccountDTO")
    void testToDtoNonNull() {
        Account account = buildAccount(ACCOUNT_1_ID, 1000, new Client(), USD);
        AccountDTO result = toDTO(account);

        assertNotNull(result);
        assertAll("Ensure mapping is correct",
                () -> assertEquals(account.getId(), result.getUserAccountId()),
                () -> assertEquals(account.getCurrency().getSymbol(), result.getCurrency()),
                () -> assertEquals(account.getBalance(), result.getBalance())
        );
    }

    @Test
    void testToDtoNull() {
        assertNull(toDTO(null));
    }
}
